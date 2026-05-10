-- ─────────────────────────────────────────────────────────────
-- V2: Dados iniciais (seed)
-- Senha dos usuários: "senha123" (hash BCrypt)
-- ─────────────────────────────────────────────────────────────

INSERT INTO users (id, name, email, password, role) VALUES
    ('00000000-0000-0000-0000-000000000001',
     'Admin',
     'admin@ecommerce.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'ADMIN'),
    ('00000000-0000-0000-0000-000000000002',
     'Cliente Teste',
     'cliente@ecommerce.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'USER');

INSERT INTO categories (id, name, description) VALUES
    ('10000000-0000-0000-0000-000000000001', 'Camisetas',   'Camisetas masculinas e femininas'),
    ('10000000-0000-0000-0000-000000000002', 'Calças',      'Jeans, leggings e sociais'),
    ('10000000-0000-0000-0000-000000000003', 'Vestidos',    'Vestidos casuais e de festa'),
    ('10000000-0000-0000-0000-000000000004', 'Acessórios',  'Cintos, bolsas e chapéus');

INSERT INTO products (id, name, description, price, stock, image_url, category_id) VALUES
    ('20000000-0000-0000-0000-000000000001',
     'Camiseta Básica Branca',
     'Camiseta 100% algodão, corte regular',
     59.90, 50,
     'https://via.placeholder.com/400x400?text=Camiseta+Branca',
     '10000000-0000-0000-0000-000000000001'),

    ('20000000-0000-0000-0000-000000000002',
     'Camiseta Estampada Preta',
     'Camiseta com estampa exclusiva, algodão premium',
     79.90, 30,
     'https://via.placeholder.com/400x400?text=Camiseta+Preta',
     '10000000-0000-0000-0000-000000000001'),

    ('20000000-0000-0000-0000-000000000003',
     'Calça Jeans Slim',
     'Jeans slim fit, lavagem clara',
     149.90, 25,
     'https://via.placeholder.com/400x400?text=Calca+Jeans',
     '10000000-0000-0000-0000-000000000002'),

    ('20000000-0000-0000-0000-000000000004',
     'Vestido Floral',
     'Vestido midi com estampa floral, tecido leve',
     129.90, 15,
     'https://via.placeholder.com/400x400?text=Vestido+Floral',
     '10000000-0000-0000-0000-000000000003'),

    ('20000000-0000-0000-0000-000000000005',
     'Cinto de Couro',
     'Cinto couro legítimo, fivela dourada',
     89.90, 40,
     'https://via.placeholder.com/400x400?text=Cinto',
     '10000000-0000-0000-0000-000000000004');
