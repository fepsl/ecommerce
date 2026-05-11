# Sprint 02 — Testes e Qualidade

> [[roadmap|← Roadmap]]  
> Data planejada: 2026-05-17  
> Status: ✅ Concluída

---

## Objetivo

Elevar a qualidade do código com cobertura de testes significativa, fechar as dívidas técnicas da Sprint 01 e garantir que as regras de negócio estão protegidas por testes automatizados.

---

## Backlog da sprint

### Testes unitários — Services

- [x] `OrderServiceTest` — casos críticos:
  - Lança `InsufficientStockException` quando estoque insuficiente
  - `unit_price` copiado de `product.price` no momento da criação
  - Estoque decrementado corretamente após criação do pedido
  - Usuário não pode ver pedido de outro usuário (`UnauthorizedException`)
  - ADMIN pode ver pedido de qualquer usuário
- [x] `AuthServiceTest`
  - Lança `EmailAlreadyExistsException` em email duplicado
  - Senha armazenada como BCrypt hash (não texto puro)
  - Token JWT retornado no login é válido
- [x] `UserServiceTest`
  - `getProfile()` retorna endereço quando existe
  - `getProfile()` retorna endereço nulo quando não existe
  - `updateAddress()` cria endereço se não existe (upsert)
  - `updateAddress()` atualiza sem criar novo registro se já existe
- [x] `CategoryServiceTest`
  - `findAll()` retorna lista mapeada corretamente
  - `findAll()` não lança erro em repositório vazio
  - `create()` salva e retorna response
  - `update()` altera nome e descrição
  - `delete()` chama `deleteById` quando existe
  - `findById()` e `delete()` lançam `ResourceNotFoundException` para id inexistente

### Validação de transição de status (dívida técnica)

- [x] Implementar lógica de transição em `OrderService.updateStatus()`
- [x] Mapa de transições válidas implementado via `Map<OrderStatus, Set<OrderStatus>>`:
  ```
  PENDING  → PAID, CANCELLED
  PAID     → SHIPPED, CANCELLED
  SHIPPED  → DELIVERED, CANCELLED
  DELIVERED → (nenhuma)
  CANCELLED → (nenhuma)
  ```
- [x] `InvalidStatusTransitionException` criada — HTTP 422 via `GlobalExceptionHandler`
- [x] `OrderServiceUpdateStatusTest` — 16 testes com `@ParameterizedTest`:
  - 6 transições válidas aceitas
  - 8 transições inválidas lançam exceção com mensagem clara
  - DELIVERED e CANCELLED rejeitam qualquer transição

### Qualidade geral

- [x] Cobertura de testes > 60% nos services — **51 testes, 0 falhas**
- [x] Validação de URL em `image_url` do produto — `@Pattern(regexp = "^(https?://.+)?$")` → HTTP 400
- [x] Tratamento de erro básico no frontend — `loadProducts` e `loadCategories` com try/catch e mensagem amigável

---

## Resultado final

```
Tests run: 51, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS
```

| Arquivo de teste | Testes |
|------------------|--------|
| `AuthIntegrationTest` | 4 |
| `EcommerceApplicationTests` | 1 |
| `AuthServiceTest` | 5 |
| `CategoryServiceTest` | 7 |
| `OrderServiceTest` | 6 |
| `OrderServiceUpdateStatusTest` | 16 |
| `ProductServiceTest` | 8 |
| `UserServiceTest` | 4 |

---

## Critérios de aceite

1. [x] `mvn test` passa sem falhas
2. [x] Todos os casos listados têm pelo menos um teste cobrindo o caminho feliz e um cobrindo o erro
3. [x] Transição de status inválida retorna HTTP 422 com mensagem clara
4. [x] Produto com `image_url` inválida retorna HTTP 400

---

## Relacionado

- [[sprint-01]] — Sprint anterior (concluída)
- [[roadmap]] — visão das fases
- [[testes]] — como os testes são estruturados
- [[regras-de-negocio]] — o que os testes de OrderService devem validar
