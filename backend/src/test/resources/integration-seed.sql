-- Limpa tabelas na ordem correta (FK) antes de reinserir
SET REFERENTIAL_INTEGRITY FALSE;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM addresses;
DELETE FROM products;
DELETE FROM categories;
DELETE FROM users;
SET REFERENTIAL_INTEGRITY TRUE;

-- Seed para testes de integração (H2)
-- senha dos usuários: senha123
INSERT INTO users (id, name, email, password, role, created_at) VALUES
    ('00000000-0000-0000-0000-000000000001', 'Admin', 'admin@ecommerce.com',
     '$2a$10$Ojjw5GRwY9PPZnnrr2kmS.uTyu.rvy6fW3fk4u8Rg4XaHqd/heFtW', 'ADMIN', CURRENT_TIMESTAMP),
    ('00000000-0000-0000-0000-000000000002', 'Cliente Teste', 'cliente@ecommerce.com',
     '$2a$10$Ojjw5GRwY9PPZnnrr2kmS.uTyu.rvy6fW3fk4u8Rg4XaHqd/heFtW', 'USER', CURRENT_TIMESTAMP);

INSERT INTO categories (id, name, description) VALUES
    ('10000000-0000-0000-0000-000000000001', 'Camisetas', 'Camisetas masculinas e femininas');

INSERT INTO products (id, name, description, price, stock, image_url, active, category_id, created_at) VALUES
    ('20000000-0000-0000-0000-000000000001', 'Camiseta Básica Branca', 'Camiseta 100% algodão, corte regular',
     59.90, 100, 'https://via.placeholder.com/400x400?text=Camiseta', true,
     '10000000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP);
