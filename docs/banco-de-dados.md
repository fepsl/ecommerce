# Banco de Dados

> [[index|← Voltar ao índice]]

---

## Schema completo

```sql
users
  id          UUID PK DEFAULT gen_random_uuid()
  name        VARCHAR(255) NOT NULL
  email       VARCHAR(255) UNIQUE NOT NULL
  password    VARCHAR(255) NOT NULL    -- BCrypt, nunca texto puro
  role        VARCHAR(10) DEFAULT 'USER' CHECK (role IN ('USER','ADMIN'))
  created_at  TIMESTAMP DEFAULT NOW()

categories
  id          UUID PK
  name        VARCHAR(100) NOT NULL
  description TEXT

products
  id          UUID PK
  name        VARCHAR(255) NOT NULL
  description TEXT
  price       DECIMAL(10,2) NOT NULL CHECK (price > 0)
  stock       INTEGER DEFAULT 0 CHECK (stock >= 0)
  image_url   VARCHAR(500)
  active      BOOLEAN DEFAULT TRUE      -- soft delete
  category_id UUID FK → categories.id ON DELETE SET NULL
  created_at  TIMESTAMP DEFAULT NOW()

orders
  id           UUID PK
  user_id      UUID FK → users.id
  status       VARCHAR(20) DEFAULT 'PENDING'
               CHECK (status IN ('PENDING','PAID','SHIPPED','DELIVERED','CANCELLED'))
  total_amount DECIMAL(10,2) NOT NULL
  created_at   TIMESTAMP DEFAULT NOW()

order_items
  id         UUID PK
  order_id   UUID FK → orders.id ON DELETE CASCADE
  product_id UUID FK → products.id
  quantity   INTEGER NOT NULL CHECK (quantity > 0)
  unit_price DECIMAL(10,2) NOT NULL   -- preço histórico, imutável

addresses
  id      UUID PK
  user_id UUID FK → users.id ON DELETE CASCADE UNIQUE  -- 1 endereço por usuário
  street  VARCHAR(255) NOT NULL
  number  VARCHAR(20) NOT NULL
  city    VARCHAR(100) NOT NULL
  state   CHAR(2) NOT NULL
  zip     VARCHAR(9) NOT NULL
```

## Índices criados

```sql
idx_products_active    ON products(active)
idx_products_category  ON products(category_id)
idx_products_price     ON products(price)
idx_orders_user        ON orders(user_id)
idx_orders_status      ON orders(status)
idx_order_items_order  ON order_items(order_id)
```

*Por quê?* Consultas mais frequentes: listar produtos ativos por categoria/preço, listar pedidos por usuário.

## Migrations Flyway

| Arquivo | Conteúdo |
|---------|---------|
| `V1__create_tables.sql` | Criação de todas as tabelas + índices |
| `V2__insert_seed_data.sql` | 2 usuários, 4 categorias, 5 produtos de exemplo |

Flyway garante que toda mudança de schema é versionada e repetível em qualquer ambiente.

## Decisões de modelagem

### UUID como PK
- Não vaza informação sequencial para o cliente (segurança)
- PostgreSQL gera com `gen_random_uuid()` (nativo desde PG 13)
- JPA 3.1: `@GeneratedValue(strategy = GenerationType.UUID)`

### `unit_price` em `order_items`
Preço registrado no momento da compra. Se o admin mudar o preço do produto depois, o valor do pedido **não muda**. Essencial para integridade financeira.

### `active` em `products` (soft delete)
Produtos nunca são deletados fisicamente — apenas marcados como `active = false`.
*Por quê?* `order_items` referencia o produto. Deletar causaria erro de FK ou perda de histórico.

### Um endereço por usuário
Relação `OneToOne` entre `users` e `addresses`. UNIQUE constraint em `user_id`.
Para suportar múltiplos endereços no futuro, bastaria remover a constraint e mudar para `OneToMany`.

## Relacionado

- [[arquitetura]] — como as entidades JPA mapeiam este schema
- [[regras-de-negocio]] — constraints de negócio além do banco
