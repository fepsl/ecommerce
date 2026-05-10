# Sprint 02 — Testes e Qualidade

> [[roadmap|← Roadmap]]  
> Data planejada: 2026-05-17  
> Status: 🟡 Em andamento

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
- [x] `AuthServiceTest`
  - Lança `EmailAlreadyExistsException` em email duplicado
  - Senha armazenada como BCrypt hash (não texto puro)
  - Token JWT retornado no login é válido
- [ ] `UserServiceTest`
  - `updateAddress()` cria endereço se não existe (upsert)
  - `updateAddress()` atualiza se já existe
- [ ] `CategoryServiceTest`
  - CRUD básico
  - Não lança erro ao listar categorias vazias

### Validação de transição de status (dívida técnica)

- [ ] Implementar lógica de transição em `OrderService.updateStatus()`
- [ ] Mapa de transições válidas:
  ```
  PENDING  → PAID, CANCELLED
  PAID     → SHIPPED, CANCELLED
  SHIPPED  → DELIVERED, CANCELLED
  DELIVERED → (nenhuma)
  CANCELLED → (nenhuma)
  ```
- [ ] Lançar `InvalidStatusTransitionException` (HTTP 422) em transição inválida
- [ ] Testar todas as transições válidas e inválidas

### Qualidade geral

- [x] Cobertura de testes > 60% nos services (meta Sprint 02)
- [ ] Validação de URL em `image_url` do produto (`@Pattern` ou `@URL`)
- [ ] Tratamento de erro básico no frontend (exibir mensagem amigável em caso de erro de API)

---

## Critérios de aceite

1. `mvn test` passa sem falhas
2. Todos os casos listados acima têm pelo menos um teste cobrindo o caminho feliz e um cobrindo o erro
3. Transição de status inválida retorna HTTP 422 com mensagem clara
4. Produto com `image_url` inválida retorna HTTP 400

---

## Relacionado

- [[sprint-01]] — Sprint anterior (concluída)
- [[roadmap]] — visão das fases
- [[testes]] — como os testes são estruturados
- [[regras-de-negocio]] — o que os testes de OrderService devem validar
