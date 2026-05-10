# Regras de Negócio

> [[index|← Voltar ao índice]]

---

## Produtos

| Regra | Onde é aplicada |
|-------|----------------|
| Produto com `active = false` não aparece na listagem pública | `ProductRepository.findWithFilters()` — cláusula `WHERE p.active = true` |
| DELETE é soft delete — só marca `active = false` | `ProductService.deactivate()` |
| Preço deve ser > 0 | Bean Validation no DTO + CHECK constraint no banco |
| Estoque nunca negativo | CHECK constraint no banco + validação antes de criar pedido |

## Pedidos

| Regra | Onde é aplicada |
|-------|----------------|
| Validar estoque de **cada item** antes de confirmar | `OrderService.create()` — lança `InsufficientStockException` |
| `unit_price` é o preço **no momento da compra** — imutável | `OrderService.create()` — copia `product.getPrice()` para `orderItem.unitPrice` |
| Total calculado no backend — nunca confiar no frontend | `OrderService.create()` — soma `unitPrice × quantity` |
| Estoque decrementado ao criar pedido | `OrderService.create()` — `product.setStock(stock - quantity)` |
| Usuário só vê seus próprios pedidos | `OrderService.findById()` — verifica `order.user.id == currentUser.id` ou `role == ADMIN` |
| Admin vê todos os pedidos | Verificação de role em `OrderService.findById()` |

### Fluxo de status do pedido

```
PENDING → PAID → SHIPPED → DELIVERED
   ↘         ↘        ↘         ↘
    CANCELLED CANCELLED CANCELLED CANCELLED
```

Qualquer status pode ir para `CANCELLED` (admin).  
Transição normal é sempre para frente.  
⚠️ Validação do fluxo de transição **ainda não implementada** — ver [[roadmap]].

## Usuários

| Regra | Onde é aplicada |
|-------|----------------|
| Email único | `UserRepository.existsByEmail()` + `EmailAlreadyExistsException` → HTTP 409 |
| Senha mínimo 6 caracteres | Bean Validation: `@Size(min = 6)` no `RegisterRequest` |
| Senha armazenada como BCrypt hash | `AuthService.register()` — `passwordEncoder.encode()` |
| Senha NUNCA retornada na API | `UserResponse` não tem campo `password` |

## Endereços

| Regra | Onde é aplicada |
|-------|----------------|
| Um endereço por usuário | UNIQUE constraint em `addresses.user_id` |
| Criar ou atualizar com `PUT /users/me/address` | `UserService.updateAddress()` — upsert |
| CEP no formato 00000-000 | Bean Validation: `@Pattern` no `AddressRequest` |
| Estado com 2 caracteres (ex: SP) | Bean Validation: `@Size(min=2, max=2)` |

## Exceções customizadas

| Exceção | HTTP | Quando |
|---------|------|--------|
| `ResourceNotFoundException` | 404 | Entidade não encontrada por ID |
| `EmailAlreadyExistsException` | 409 | Tentativa de cadastro com email duplicado |
| `InsufficientStockException` | 422 | Estoque insuficiente ao criar pedido |
| `UnauthorizedException` | 403 | Usuário tentando ver pedido de outro usuário |

## Relacionado

- [[api-endpoints]] — onde cada regra se manifesta na API
- [[banco-de-dados]] — constraints no nível do banco
