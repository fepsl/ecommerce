-- ─────────────────────────────────────────────────────────────
-- V3: Fix de dados seed
--   - Remove produto e categoria de teste criados via Swagger no QA
--   - Corrige URLs de imagem (via.placeholder.com → placehold.co, 3:4)
--   - Restaura estoque aos valores originais do seed
-- ─────────────────────────────────────────────────────────────

-- Remove dados do produto de teste (criado manualmente no QA em 13/05/2026).
-- Deleta itens de pedido primeiro para respeitar FK, depois o produto e a categoria.
DELETE FROM order_items WHERE product_id = '9f0a42c1-4187-48aa-b6f8-192182c2b209';
DELETE FROM orders      WHERE id NOT IN (SELECT DISTINCT order_id FROM order_items);
DELETE FROM products    WHERE id = '9f0a42c1-4187-48aa-b6f8-192182c2b209';
DELETE FROM categories  WHERE id = '21f99879-f0b2-41e2-989d-4ff8fdbaceae';

-- Corrige URLs de imagem para placehold.co (via.placeholder.com foi desativado)
UPDATE products SET image_url = 'https://placehold.co/400x533/f2f2f2/111111?text=Camiseta+Branca'
    WHERE id = '20000000-0000-0000-0000-000000000001';

UPDATE products SET image_url = 'https://placehold.co/400x533/111111/f2f2f2?text=Camiseta+Preta'
    WHERE id = '20000000-0000-0000-0000-000000000002';

UPDATE products SET image_url = 'https://placehold.co/400x533/1c3d5a/f2f2f2?text=Calça+Jeans'
    WHERE id = '20000000-0000-0000-0000-000000000003';

UPDATE products SET image_url = 'https://placehold.co/400x533/2e1a2f/f2f2f2?text=Vestido+Floral'
    WHERE id = '20000000-0000-0000-0000-000000000004';

UPDATE products SET image_url = 'https://placehold.co/400x533/2e1a0e/f2f2f2?text=Cinto+de+Couro'
    WHERE id = '20000000-0000-0000-0000-000000000005';

-- Restaura estoque (decrementado durante sessão de QA em 13/05/2026)
UPDATE products SET stock = 30 WHERE id = '20000000-0000-0000-0000-000000000002';
UPDATE products SET stock = 25 WHERE id = '20000000-0000-0000-0000-000000000003';
UPDATE products SET stock = 15 WHERE id = '20000000-0000-0000-0000-000000000004';
UPDATE products SET stock = 40 WHERE id = '20000000-0000-0000-0000-000000000005';
