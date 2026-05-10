# E-commerce de Roupas — Contexto do Projeto

## Sobre o projeto

E-commerce de roupas desenvolvido como projeto de portfólio para estágio em Engenharia de
Software. O objetivo é demonstrar domínio real de backend (Java + Spring Boot), banco de dados
relacional, autenticação com JWT, frontend básico e boas práticas de engenharia.

Este é um projeto solo, desenvolvido por um estudante de Engenharia de Software em fase de
aprendizado. As decisões técnicas devem ser didáticas e bem justificadas, não apenas funcionais.

---

## Stack tecnológica

### Backend
- Java 17
- Spring Boot 3
- Spring Security + JWT (autenticação e autorização)
- Spring Data JPA + Hibernate (ORM)
- PostgreSQL (banco de dados principal)
- Flyway (migrations versionadas)
- Bean Validation (validação de DTOs)
- SpringDoc OpenAPI 3 / Swagger (documentação da API)
- Maven (gerenciamento de dependências)
- Lombok (redução de boilerplate)
- JUnit 5 + Mockito (testes unitários)
- H2 in-memory (banco nos testes)

### Infraestrutura
- Docker + Docker Compose (app + banco + adminer)
- Variáveis de ambiente via `.env`

### Frontend
- HTML, CSS e JavaScript puro (sem framework)
- Fetch API para consumo da API REST
- localStorage para persistência do token JWT e carrinho

### Projeto secundário (repositório separado)
- Python com requests + BeautifulSoup + pandas (scraper)

---

## Arquitetura

### Estrutura de pacotes (backend)

```
src/main/java/com/ecommerce/
├── controller/      → endpoints REST, recebe requisições, delega ao service
├── service/         → regras de negócio, nunca acessa o repositório diretamente do controller
├── repository/      → interfaces JPA, acesso ao banco
├── model/           → entidades JPA (@Entity)
├── dto/             → objetos de transferência (request/response), nunca expor entidade direto
├── exception/       → exceções customizadas + handler global (@ControllerAdvice)
├── security/        → JWT filter, UserDetailsService, SecurityFilterChain
└── config/          → Swagger, CORS, beans de configuração
```

### Princípios arquiteturais
- Separação estrita de responsabilidades entre camadas
- Regra de negócio sempre no `@Service`, nunca no `@Controller`
- Entidades JPA nunca retornadas diretamente na API — sempre usar DTOs
- Injeção de dependência sempre por construtor (campo `final` + `@RequiredArgsConstructor`)
- Tratamento de erros centralizado com `@ControllerAdvice`
- Respostas de erro sempre no mesmo formato JSON padronizado

---

## Modelagem do banco de dados

```
users
  id          UUID PK
  name        VARCHAR NOT NULL
  email       VARCHAR UNIQUE NOT NULL
  password    VARCHAR NOT NULL         -- hash BCrypt, nunca texto puro
  role        ENUM(USER, ADMIN)
  created_at  TIMESTAMP

categories
  id          UUID PK
  name        VARCHAR NOT NULL
  description TEXT

products
  id          UUID PK
  name        VARCHAR NOT NULL
  description TEXT
  price       DECIMAL(10,2) NOT NULL
  stock       INTEGER NOT NULL DEFAULT 0
  image_url   VARCHAR
  active      BOOLEAN DEFAULT true
  category_id UUID FK → categories.id
  created_at  TIMESTAMP

orders
  id            UUID PK
  user_id       UUID FK → users.id
  status        ENUM(PENDING, PAID, SHIPPED, DELIVERED, CANCELLED)
  total_amount  DECIMAL(10,2) NOT NULL
  created_at    TIMESTAMP

order_items
  id          UUID PK
  order_id    UUID FK → orders.id
  product_id  UUID FK → products.id
  quantity    INTEGER NOT NULL
  unit_price  DECIMAL(10,2) NOT NULL   -- preço no momento da compra

addresses
  id        UUID PK
  user_id   UUID FK → users.id
  street    VARCHAR NOT NULL
  number    VARCHAR NOT NULL
  city      VARCHAR NOT NULL
  state     VARCHAR(2) NOT NULL
  zip       VARCHAR(9) NOT NULL
```

---

## Endpoints da API

### Autenticação (pública)
```
POST /auth/register    → cadastro de usuário, retorna JWT
POST /auth/login       → login, retorna JWT
```

### Produtos (leitura pública, escrita apenas ADMIN)
```
GET    /products                → listagem paginada com filtros
GET    /products/{id}           → detalhe do produto
POST   /products                → criar produto           [ADMIN]
PUT    /products/{id}           → atualizar produto       [ADMIN]
DELETE /products/{id}           → desativar produto       [ADMIN]
```

Parâmetros de listagem: `?page=0&size=12&category=&minPrice=&maxPrice=&name=`

### Categorias
```
GET    /categories              → listar todas
POST   /categories              → criar categoria         [ADMIN]
PUT    /categories/{id}         → atualizar categoria     [ADMIN]
DELETE /categories/{id}         → remover categoria       [ADMIN]
```

### Pedidos
```
POST   /orders                  → criar pedido            [USER autenticado]
GET    /orders/me               → meus pedidos            [USER autenticado]
GET    /orders/{id}             → detalhe do pedido       [dono ou ADMIN]
PUT    /orders/{id}/status      → atualizar status        [ADMIN]
```

### Usuário
```
GET    /users/me                → perfil do usuário autenticado
PUT    /users/me/address        → atualizar endereço
```

---

## Autenticação

- JWT com expiração de 24h
- Token enviado no header: `Authorization: Bearer <token>`
- Roles: `USER` (cliente) e `ADMIN` (gestão da loja)
- Rotas protegidas verificadas via `JwtAuthFilter` (OncePerRequestFilter)
- Senhas armazenadas com BCrypt

---

## Regras de negócio importantes

- Ao criar pedido: validar estoque de cada item antes de confirmar
- `unit_price` em `order_items` é o preço no momento da compra (não muda se o produto mudar de preço depois)
- Produto com `active = false` não aparece na listagem pública
- Fluxo de status do pedido: `PENDING → PAID → SHIPPED → DELIVERED` (ou `CANCELLED` a qualquer momento pelo admin)
- Usuário só pode ver seus próprios pedidos; admin vê todos
- Email único por usuário — retornar `409 Conflict` se já existir

---

## Formato de erro padronizado

Toda resposta de erro segue este formato:

```json
{
  "timestamp": "2025-04-30T14:32:00",
  "status": 404,
  "error": "Not Found",
  "message": "Produto não encontrado com id: abc-123",
  "path": "/products/abc-123"
}
```

---

## Decisões técnicas (para entrevista)

Estas são as justificativas reais para as escolhas feitas no projeto:

- **DTO em vez de entidade na API**: evita expor campos sensíveis (como `password`), desacopla o
  contrato da API da estrutura interna do banco e facilita evoluir os dois independentemente.

- **Injeção por construtor**: campo `final` garante imutabilidade após construção e facilita testes
  unitários, pois permite passar mocks diretamente pelo construtor sem reflexão.

- **JWT sem estado (stateless)**: o servidor não precisa manter sessão, o que facilita escalar
  horizontalmente. A desvantagem é que não dá para invalidar um token antes de expirar sem
  mecanismos adicionais (como blocklist).

- **Flyway para migrations**: versionamento das mudanças do banco junto com o código, garantindo
  que banco e aplicação estejam sempre sincronizados em qualquer ambiente.

- **Fetch type LAZY nos relacionamentos**: evita carregar dados desnecessários. O problema N+1
  é mitigado usando `@EntityGraph` ou `JOIN FETCH` quando necessário.

- **`unit_price` em `order_items`**: preço histórico garante que o valor do pedido não mude se
  o produto for atualizado depois da compra.

---

## O que este projeto ainda não tem (melhorias futuras)

- Pagamento real (integração com gateway)
- Refresh token
- Upload de imagens (atualmente apenas URL)
- Cache com Redis
- Testes de integração mais completos
- Rate limiting
- CI/CD pipeline

---

## Como rodar o projeto

```bash
# Subir banco + app
docker-compose up --build

# Endpoints disponíveis em:
# API:     http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# Adminer: http://localhost:8081
```

---

## Estilo de código

- Commits em português ou inglês seguindo Conventional Commits:
  `feat:`, `fix:`, `test:`, `docs:`, `refactor:`, `chore:`
- Nomes de classes em inglês (padrão Java/Spring)
- Comentários e README em português (projeto nacional)
- Sem lógica no controller — apenas receber, delegar e responder
- Exceções customizadas para cada caso de erro de negócio

---

## Contexto do desenvolvedor

- Estudante de Engenharia de Software
- Já cursou: Java OO, Git, Banco de Dados, HTML/CSS/JS (em andamento)
- Aprendendo: Spring Boot, JPA, JWT, Docker
- Objetivo: conseguir estágio em desenvolvimento de software
- Este é o projeto principal do portfólio — deve ser bem feito, não rápido
