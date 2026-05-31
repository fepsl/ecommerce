# Arquitetura

> [[index|← Voltar ao índice]]

---

## Estrutura de pacotes

```
com.ecommerce/
├── controller/     → Recebe requisições HTTP, delega ao service, retorna resposta
├── service/        → Regras de negócio — ÚNICA camada que contém lógica
├── repository/     → Interfaces JPA para acesso ao banco
├── model/          → Entidades JPA (@Entity) — mapeamento do banco
├── dto/
│   ├── request/    → Dados que chegam do cliente (com Bean Validation)
│   └── response/   → Dados que saem para o cliente (nunca a entidade direta)
├── exception/      → Exceções customizadas + handler global
├── security/       → JWT filter, UserDetailsService, SecurityConfig
└── config/         → Swagger, CORS, beans de configuração
```

## Fluxo de uma requisição

```
Cliente HTTP
    ↓
JwtAuthFilter (valida token, popula SecurityContext)
    ↓
@RestController (valida DTO com @Valid, chama service)
    ↓
@Service (regra de negócio, lança exceções customizadas)
    ↓
@Repository (JPA, acessa PostgreSQL)
    ↓
@Service (monta DTO de resposta)
    ↓
@RestController (retorna ResponseEntity com DTO)
    ↓
Cliente HTTP
```

## Princípios arquiteturais

### 1. Separação estrita de responsabilidades
Cada camada tem uma responsabilidade única. O controller **nunca** tem `if` de negócio.

### 2. DTOs em ambas as direções
- **Request DTO**: valida os dados de entrada com `@NotBlank`, `@Email`, etc.
- **Response DTO**: controla o que expõe — nunca devolve a entidade JPA diretamente
- *Por quê?* Evita expor `password`, desacopla contrato da API da estrutura do banco

### 3. Injeção por construtor
```java
@Service
@RequiredArgsConstructor  // Lombok gera o construtor
public class ProductService {
    private final ProductRepository productRepository; // campo final
```
*Por quê?* `final` garante imutabilidade. Facilita testes (passa mock pelo construtor).

### 4. Tratamento de erro centralizado
`GlobalExceptionHandler` captura todas as exceções e retorna o formato padronizado.
Cada caso de negócio tem sua própria exceção: `ResourceNotFoundException`, `InsufficientStockException`, etc.

### 5. Fetch LAZY + EntityGraph
Relacionamentos `@ManyToOne` e `@OneToMany` são todos LAZY.
Quando precisa carregar relacionamentos, usa `@EntityGraph` no repositório — evita N+1.

## Arquivos críticos

| Arquivo | Papel |
|---------|-------|
| `SecurityConfig.java` | Define quais rotas são públicas vs. protegidas |
| `JwtAuthFilter.java` | Intercepta toda requisição e valida o token |
| `GlobalExceptionHandler.java` | Captura exceções e padroniza respostas de erro |
| `ProductRepository.java` | Exemplo de query JPQL com filtros opcionais |
| `OrderService.java` | Lógica mais complexa: valida estoque, calcula total, registra preço histórico |

---

## Autenticação JWT

### Fluxo completo

```
POST /auth/register ou /auth/login
    ↓
Spring Security autentica via DaoAuthenticationProvider (BCrypt)
    ↓
JwtUtil.generateToken() — assina com HS256 + chave secreta
    ↓
Retorna { "token": "eyJ..." } → cliente armazena em localStorage
    ↓
Próximas requisições: Header "Authorization: Bearer eyJ..."
    ↓
JwtAuthFilter.doFilterInternal():
  - Extrai token → JwtUtil.extractUsername() → email
  - UserDetailsServiceImpl.loadUserByUsername() → User do banco
  - JwtUtil.isTokenValid() → verifica assinatura + expiração
  - Popula SecurityContextHolder
    ↓
Spring Security verifica @PreAuthorize / .authorizeHttpRequests()
```

### Configurações

| Parâmetro | Valor |
|-----------|-------|
| Algoritmo | HS256 |
| Expiração | 24 horas (86.400.000 ms) |
| Header | `Authorization: Bearer <token>` |
| Chave | `JWT_SECRET` (mín. 32 chars) |

### Roles

| Role | Permissões |
|------|-----------|
| `USER` | Navegar produtos, criar pedidos, ver seus pedidos, gerenciar endereço |
| `ADMIN` | Tudo do USER + gerenciar produtos, categorias, atualizar status de pedidos |

### Rotas públicas

```
POST /auth/register
POST /auth/login
GET  /products/**
GET  /categories/**
/swagger-ui/**
/v3/api-docs/**
```

### Trade-offs do JWT stateless

**Vantagem**: sem sessão no servidor → escala horizontalmente sem sticky sessions.  
**Desvantagem**: não dá para invalidar token antes de expirar. Para revogação imediata precisaria de blocklist (Redis) — feature futura.

---

## Relacionado

- [[banco-de-dados]] — entidades e relacionamentos
- [[spring-boot]] — configuração, profiles, Swagger
- [[testes]] — como cada camada é testada
- [[decisoes-tecnicas]] — justificativas para cada escolha
