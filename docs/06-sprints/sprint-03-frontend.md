# Sprint 03 — Frontend Editorial & Página de Produto

> [[roadmap|← Roadmap]]  
> Data: 2026-05-10  
> Status: ✅ Concluída

---

## Objetivo

Elevar a percepção de qualidade do portfólio transformando a VELN numa marca de luxo crível — com conteúdo editorial na home, página de detalhe de produto completa e experiência visual polida.

---

## Backlog da sprint

### Home — Identidade de marca

- [x] Seção de manifesto da VELN abaixo do hero (`TASK-02`)
- [x] Seção de produtos em destaque com skeleton loading (`TASK-03`)
- [x] Link "Ver coleção completa" para `/pages/products.html`

### Página de produto

- [x] Criar `frontend/pages/product.html` — layout 2 colunas (imagem + info) (`TASK-04`)
- [x] Criar `frontend/js/product.js` — fetch por `?id=<uuid>`, preenche DOM, erro elegante (`TASK-04`)
- [x] Zoom na imagem via CSS `transform: scale(1.08)` no hover (`TASK-05`)
- [x] Seção de produtos relacionados (mesma categoria, excluindo o atual, máx. 3) (`TASK-06`)
- [x] Clique no card do catálogo e da home redireciona para a página de detalhe

### Qualidade visual

- [x] Skeleton loading no catálogo de produtos (`TASK-07`)
- [x] Estado vazio elegante no carrinho e pedidos (`TASK-08`)
- [x] CSS base: `@keyframes skeleton-pulse`, `.skeleton`, `.img-zoom-wrap` (`TASK-01`)

---

## Resultado final

| Arquivo | Alteração |
|---------|-----------|
| `frontend/pages/product.html` | Criado — página de detalhe completa |
| `frontend/js/product.js` | Criado — lógica da página de detalhe |
| `frontend/index.html` | Seção manifesto + seção produtos em destaque |
| `frontend/js/products.js` | Card redireciona para `product.html?id=...` |
| `frontend/css/styles.css` | Skeleton, zoom, manifesto, featured, related, empty-state |
| `frontend/pages/cart.html` | Estado vazio elegante |
| `frontend/pages/orders.html` | Estado vazio elegante |

**8 tasks — 8 concluídas. Nenhum endpoint de backend necessário.**

---

## Critérios de aceite

- [x] Home carrega manifesto e produtos em destaque sem erro no console
- [x] Clique no card leva para `/pages/product.html?id=<uuid>`
- [x] Zoom na imagem funciona no hover
- [x] Produtos relacionados aparecem (e somem se não houver)
- [x] Botão "Adicionar ao carrinho" na página de detalhe funciona
- [x] Skeleton aparece e é substituído pelos cards reais
- [x] Carrinho vazio exibe mensagem e link elegantes
- [x] Pedidos vazios exibe mensagem e link elegantes
- [x] ID inválido na URL exibe mensagem de erro sem quebrar a página
- [x] Zero rastros de "ModaShop" em qualquer arquivo
- [x] Zero cores hardcoded fora do `:root`

---

## Relacionado

- [[prd-sprint-03-frontend]] — PRD detalhado com todos os RFs
- [[tasks-sprint-03-frontend]] — Tasks com critérios de done
- [[sprint-02]] — Sprint anterior
- [[roadmap]] — visão das fases
- [[frontend]] — documentação do frontend
