-- ─────────────────────────────────────────────────────────────
-- V1: Criação das tabelas principais
-- ─────────────────────────────────────────────────────────────

CREATE TABLE users (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(10)  NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE categories (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE products (
    id          UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    stock       INTEGER        NOT NULL DEFAULT 0 CHECK (stock >= 0),
    image_url   VARCHAR(500),
    active      BOOLEAN        NOT NULL DEFAULT TRUE,
    category_id UUID           REFERENCES categories (id) ON DELETE SET NULL,
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE TABLE orders (
    id           UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      UUID           NOT NULL REFERENCES users (id),
    status       VARCHAR(20)    NOT NULL DEFAULT 'PENDING'
                     CHECK (status IN ('PENDING', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELLED')),
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    created_at   TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE TABLE order_items (
    id         UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id   UUID           NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id UUID           NOT NULL REFERENCES products (id),
    quantity   INTEGER        NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price > 0)
);

CREATE TABLE addresses (
    id      UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID        NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    street  VARCHAR(255) NOT NULL,
    number  VARCHAR(20)  NOT NULL,
    city    VARCHAR(100) NOT NULL,
    state   VARCHAR(2)   NOT NULL,
    zip     VARCHAR(9)   NOT NULL
);

-- Índices para performance em consultas frequentes
CREATE INDEX idx_products_active        ON products (active);
CREATE INDEX idx_products_category      ON products (category_id);
CREATE INDEX idx_products_price         ON products (price);
CREATE INDEX idx_orders_user            ON orders (user_id);
CREATE INDEX idx_orders_status          ON orders (status);
CREATE INDEX idx_order_items_order      ON order_items (order_id);
