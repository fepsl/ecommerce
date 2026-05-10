# API — Endpoints

> [[index|← Voltar ao índice]]  
> Documentação completa disponível em: http://localhost:8080/swagger-ui.html

---

## Autenticação (pública)

```http
POST /auth/register
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123"
}
```
→ Retorna `AuthResponse` com `token`, `name`, `email`, `role`

```http
POST /auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "password": "senha123"
}
```
→ Retorna `AuthResponse` com `token`

---

## Produtos

### Listagem paginada com filtros (público)
```http
GET /products?page=0&size=12&name=camiseta&category={uuid}&minPrice=50&maxPrice=200
```
→ Retorna `Page<ProductResponse>` com `content`, `totalElements`, `totalPages`, `number`

### Detalhe (público)
```http
GET /products/{uuid}
```

### Criar, atualizar, desativar (ADMIN)
```http
POST   /products          Authorization: Bearer {token}
PUT    /products/{uuid}   Authorization: Bearer {token}
DELETE /products/{uuid}   Authorization: Bearer {token}  → soft delete (active=false)
```

Corpo para POST/PUT:
```json
{
  "name": "Camiseta Básica",
  "description": "...",
  "price": 59.90,
  "stock": 50,
  "imageUrl": "https://...",
  "categoryId": "{uuid}"
}
```

---

## Categorias

```http
GET    /categories          → lista todas (público)
POST   /categories          Authorization: Bearer {token} [ADMIN]
PUT    /categories/{uuid}   Authorization: Bearer {token} [ADMIN]
DELETE /categories/{uuid}   Authorization: Bearer {token} [ADMIN]
```

---

## Pedidos (autenticado)

```http
POST /orders
Authorization: Bearer {token}

{
  "items": [
    { "productId": "{uuid}", "quantity": 2 },
    { "productId": "{uuid}", "quantity": 1 }
  ]
}
```
→ Valida estoque, registra `unit_price` histórico, retorna `OrderResponse`

```http
GET /orders/me            → meus pedidos (mais recente primeiro)
GET /orders/{uuid}        → detalhe (dono ou admin)
PUT /orders/{uuid}/status → atualizar status [ADMIN]
```

Corpo do status:
```json
{ "status": "PAID" }
```
Fluxo: `PENDING → PAID → SHIPPED → DELIVERED` (ou `CANCELLED` a qualquer momento)

---

## Usuário (autenticado)

```http
GET /users/me             → perfil + endereço
PUT /users/me/address     → criar ou atualizar endereço

{
  "street": "Rua das Flores",
  "number": "123",
  "city": "São Paulo",
  "state": "SP",
  "zip": "01001-000"
}
```

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

### Códigos HTTP usados

| Código | Quando |
|--------|--------|
| 200 | Sucesso |
| 201 | Criado |
| 204 | Deletado (sem corpo) |
| 400 | Validação falhou (Bean Validation) |
| 401 | Token ausente ou inválido |
| 403 | Autorizado mas sem permissão (ex: USER tentando rota ADMIN) |
| 404 | Recurso não encontrado |
| 409 | Email já cadastrado |
| 422 | Estoque insuficiente |
| 500 | Erro interno |

## Relacionado

- [[autenticacao-jwt]] — como o token funciona
- [[regras-de-negocio]] — validações de negócio por endpoint
