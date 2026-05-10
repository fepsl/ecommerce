# Glossário

> [[index|← Voltar ao índice]]

Termos técnicos e conceitos usados no projeto, úteis para revisão antes de entrevistas.

---

## Spring Boot

| Termo | Significado |
|-------|------------|
| `@RestController` | Combina `@Controller` + `@ResponseBody`. Todos os métodos retornam JSON por padrão. |
| `@Service` | Marca a classe como bean de serviço no Spring IoC. Camada de lógica de negócio. |
| `@Repository` | Marca interfaces JPA. Spring Data cria implementação automaticamente. |
| `@RequiredArgsConstructor` | Lombok gera construtor com todos os campos `final`. Habilita injeção por construtor. |
| `ResponseEntity<T>` | Permite controlar HTTP status, headers e body da resposta. |
| `@Valid` | Ativa Bean Validation no parâmetro do controller. Lança exceção se inválido. |
| `@PreAuthorize` | Segurança no método. Ex: `@PreAuthorize("hasRole('ADMIN')")` |
| `@ControllerAdvice` | Captura exceções de qualquer controller. Usado no `GlobalExceptionHandler`. |
| `@ExceptionHandler` | Dentro de `@ControllerAdvice`, mapeia exceção → resposta HTTP. |
| `OncePerRequestFilter` | Base do `JwtAuthFilter`. Garante que o filtro executa exatamente uma vez por request. |

---

## JPA / Hibernate

| Termo | Significado |
|-------|------------|
| `@Entity` | Marca a classe como entidade JPA (mapeia para uma tabela). |
| `@GeneratedValue(strategy = UUID)` | PostgreSQL gera o UUID automaticamente com `gen_random_uuid()`. |
| `FetchType.LAZY` | Relacionamento não é carregado automaticamente — só quando acessado. Evita N+1. |
| `FetchType.EAGER` | Carrega o relacionamento sempre, junto com a entidade pai. Pode causar over-fetching. |
| `@EntityGraph` | Especifica quais relacionamentos LAZY carregar em uma query específica, via JOIN FETCH. |
| Problema N+1 | 1 query para buscar N entidades + N queries para buscar o relacionamento de cada uma. |
| `ddl-auto: validate` | Hibernate valida se o schema do banco bate com as entidades, mas não altera nada. |

---

## Segurança / JWT

| Termo | Significado |
|-------|------------|
| JWT | JSON Web Token. Token autocontido com claims assinados. Não precisa de sessão no servidor. |
| HS256 | HMAC com SHA-256. Algoritmo de assinatura simétrico (mesma chave para assinar e verificar). |
| `SecurityContext` | Onde Spring Security armazena a autenticação do usuário para a request atual. |
| BCrypt | Função de hash para senhas. Inclui salt automático. Resistente a ataques de força bruta. |
| Stateless | Servidor não guarda estado de sessão. Toda informação necessária está no token. |
| Bearer Token | Esquema de autenticação HTTP. O token vai no header: `Authorization: Bearer <token>`. |
| `UserDetails` | Interface Spring Security. `User` implementa para fornecer email, senha e roles. |

---

## Padrões de projeto

| Termo | Uso no projeto |
|-------|---------------|
| DTO (Data Transfer Object) | Objetos usados para trafegar dados entre camadas. Nunca expõe a entidade JPA direto. |
| ADR (Architecture Decision Record) | Documento que registra uma decisão técnica, sua justificativa e trade-offs. |
| Soft Delete | Não apaga o registro — apenas marca como inativo (`active = false`). |
| Upsert | Operação que cria o registro se não existir, ou atualiza se já existir. Usado no endereço. |
| Seed Data | Dados iniciais inseridos no banco para desenvolvimento/testes. |
| Migration | Script SQL versionado que evolui o schema do banco de forma controlada. |

---

## HTTP

| Código | Significado | Quando usar |
|--------|-------------|-------------|
| 200 | OK | Sucesso em GET, PUT |
| 201 | Created | Recurso criado com sucesso (POST) |
| 204 | No Content | Sucesso sem corpo (DELETE) |
| 400 | Bad Request | Validação de DTO falhou |
| 401 | Unauthorized | Token ausente ou inválido |
| 403 | Forbidden | Autenticado mas sem permissão |
| 404 | Not Found | Recurso não encontrado |
| 409 | Conflict | Email já cadastrado |
| 422 | Unprocessable Entity | Erro de negócio (estoque insuficiente, transição inválida) |
| 500 | Internal Server Error | Erro inesperado no servidor |

---

## Relacionado

- [[decisoes-tecnicas]] — ADRs com justificativas aprofundadas
- [[arquitetura]] — como os padrões se aplicam na estrutura do projeto
- [[autenticacao-jwt]] — JWT e Spring Security em detalhe
