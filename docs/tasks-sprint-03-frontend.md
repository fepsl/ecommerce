# Tasks: Sprint 03 — Frontend Editorial & Página de Produto

> Gerado a partir de: [[prd-sprint-03-frontend.md]]  
> Data: 2026-05-10

---

## Dependências entre tasks

```
TASK-01 → TASK-03
TASK-01 → TASK-04 → TASK-05
                  → TASK-06
TASK-01 → TASK-07
TASK-02 (independente)
TASK-08 (independente)
```

**Podem começar agora**: TASK-01, TASK-02, TASK-08  
**Desbloqueadas após TASK-01**: TASK-03, TASK-04, TASK-07  
**Desbloqueadas após TASK-04**: TASK-05, TASK-06

---

## TASK-01 — CSS base: skeleton + zoom (fundação da sprint)

**Tipo**: `feat`  
**Estimativa**: 30min–1h  
**Bloqueia**: TASK-03, TASK-04, TASK-07  
**Bloqueada por**: —  
**Status**: PENDENTE

**O que fazer:**
- [ ] Adicionar `@keyframes skeleton-pulse` com `opacity` oscilando entre `0.4` e `0.8`
- [ ] Criar classe `.skeleton` com fundo `var(--surface-3)`, animação `skeleton-pulse 1.4s ease infinite`, e `border-radius: var(--radius)`
- [ ] Criar classe `.skeleton-img` com `aspect-ratio: 3/4` e `width: 100%` (imita o card de produto)
- [ ] Criar classe `.skeleton-line` com `height: .85rem`, `margin-bottom: .4rem` (imita linhas de texto)
- [ ] Criar classe `.img-zoom-wrap` com `overflow: hidden` e `cursor: zoom-in`
- [ ] Criar regra `.img-zoom-wrap img` com `transition: transform .4s ease`
- [ ] Criar regra `.img-zoom-wrap:hover img` com `transform: scale(1.08)`

**Arquivos afetados:**
- `frontend/css/styles.css` (adicionar ao final)

**Critério de done:**
- [ ] Classes existem no CSS e usam apenas variáveis do `:root`
- [ ] Nenhuma cor hardcoded

---

## TASK-02 — Home: seção manifesto da marca (RF-01)

**Tipo**: `feat`  
**Estimativa**: 30min–1h  
**Bloqueia**: —  
**Bloqueada por**: —  
**Status**: PENDENTE

**O que fazer:**
- [ ] Adicionar seção `.manifesto` logo abaixo do `.features` no `index.html`
- [ ] Estrutura: `<section class="manifesto"><div class="container"><p class="manifesto-label">Manifesto</p><h2>...</h2><p>...</p></div></section>`
- [ ] Texto: escrever um parágrafo de marca de luxo para a VELN (tom: austero, elegante, não comercial — algo como "Não criamos tendências. Criamos o que permanece.")
- [ ] Estilizar `.manifesto` no CSS: `padding: 6rem 0`, `border-bottom: 1px solid var(--border)`, texto centralizado
- [ ] `.manifesto-label`: `font-size: .7rem`, `letter-spacing: .2em`, `text-transform: uppercase`, `color: var(--text-muted)`, `margin-bottom: 1rem`
- [ ] `.manifesto h2`: Cormorant Garamond, `font-size: clamp(1.8rem, 4vw, 3rem)`, `font-weight: 300`, `margin-bottom: 1.5rem`
- [ ] `.manifesto p`: `max-width: 520px`, `margin: 0 auto`, `color: var(--text-muted)`, `line-height: 1.8`

**Arquivos afetados:**
- `frontend/index.html` (adicionar seção)
- `frontend/css/styles.css` (adicionar estilos `.manifesto`)

**Critério de done:**
- [ ] Seção visível na home com fundo escuro e texto elegante
- [ ] Sem imagens externas obrigatórias — funciona só com texto
- [ ] Nenhuma cor hardcoded

---

## TASK-03 — Home: produtos em destaque com skeleton (RF-02, RF-06 parcial)

**Tipo**: `feat`  
**Estimativa**: 1–2h  
**Bloqueia**: —  
**Bloqueada por**: TASK-01  
**Status**: PENDENTE

**O que fazer:**
- [ ] Adicionar seção `.featured` no `index.html` após `.manifesto`:
  ```html
  <section class="featured">
    <div class="container">
      <header class="section-header">
        <h2>Coleção</h2>
        <a href="/pages/products.html">Ver tudo →</a>
      </header>
      <div class="products-grid" id="featured-grid"></div>
    </div>
  </section>
  ```
- [ ] Criar `frontend/js/featured.js` (ou adicionar em bloco `<script type="module">` no index):
  - `GET /products?size=4` via `api.js`
  - Renderizar 4 skeletons imediatamente antes do fetch
  - Substituir pelos cards reais quando a resposta chegar
  - Cada card tem `href="/pages/product.html?id=${p.id}"` no clique (link no nome + imagem)
  - Botão "Adicionar ao carrinho" mantém comportamento original
- [ ] Estilizar `.featured` no CSS: `padding: 4rem 0`, `border-bottom: 1px solid var(--border)`
- [ ] Estilizar `.section-header`: `display: flex`, `justify-content: space-between`, `align-items: baseline`, `margin-bottom: 2rem`
- [ ] `.section-header h2`: Cormorant Garamond, `font-size: 1.8rem`, `font-weight: 300`
- [ ] `.section-header a`: `font-size: .75rem`, `letter-spacing: .1em`, `text-transform: uppercase`, `color: var(--text-muted)`

**Arquivos afetados:**
- `frontend/index.html` (adicionar seção + script)
- `frontend/css/styles.css` (adicionar `.featured`, `.section-header`)

**Critério de done:**
- [ ] 4 skeletons aparecem durante o carregamento
- [ ] 4 produtos reais substituem os skeletons após fetch
- [ ] Clique na imagem ou nome do produto redireciona para `product.html?id=...`
- [ ] Botão "Adicionar ao carrinho" continua funcionando
- [ ] Link "Ver tudo" leva para `/pages/products.html`

---

## TASK-04 — Página de produto: estrutura completa + add to cart (RF-03)

**Tipo**: `feat`  
**Estimativa**: 2–3h  
**Bloqueia**: TASK-05, TASK-06  
**Bloqueada por**: TASK-01  
**Status**: PENDENTE

**O que fazer:**
- [ ] Criar `frontend/pages/product.html` com navbar, container `.product-detail` e footer
- [ ] Layout `.product-detail`: `display: grid`, `grid-template-columns: 1fr 1fr`, `gap: 3rem`, `padding: 3rem 0`
- [ ] Coluna esquerda: container `.img-zoom-wrap` > `<img id="product-img">`
- [ ] Coluna direita: breadcrumb, categoria (uppercase muted), nome (Cormorant Garamond `2rem`), preço, descrição, estoque, botão "Adicionar ao carrinho"
- [ ] Breadcrumb: `← Produtos` linkando para `/pages/products.html`
- [ ] Criar `frontend/js/product.js`:
  - Ler `id` da query string: `new URLSearchParams(window.location.search).get('id')`
  - `GET /products/{id}` via `api.js`
  - Preencher todos os campos do DOM
  - Se `!id` ou erro 404: exibir mensagem elegante ("Este produto não está disponível.")
  - Botão "Adicionar ao carrinho": mesma lógica do `products.js` (salvar no localStorage)
  - Atualizar `#cart-count` na navbar
- [ ] Atualizar `products.js`: clique no card (imagem + nome) redireciona para `product.html?id=...` em vez de direto ao carrinho; botão explícito "Adicionar ao carrinho" permanece
- [ ] Mobile (≤768px): `.product-detail` com `grid-template-columns: 1fr`

**Arquivos afetados:**
- `frontend/pages/product.html` (criar)
- `frontend/js/product.js` (criar)
- `frontend/js/products.js` (modificar clique do card)
- `frontend/css/styles.css` (adicionar `.product-detail`, `.product-detail-info`)

**Critério de done:**
- [ ] `product.html?id=<uuid-válido>` carrega e exibe todos os campos
- [ ] `product.html?id=invalido` exibe mensagem de erro sem quebrar a página
- [ ] Botão "Adicionar ao carrinho" adiciona ao localStorage e atualiza contador
- [ ] Clique no card do catálogo redireciona para a página de detalhe
- [ ] Layout funciona em mobile (≤768px)

---

## TASK-05 — Zoom na imagem do produto (RF-04)

**Tipo**: `feat`  
**Estimativa**: 30min  
**Bloqueia**: —  
**Bloqueada por**: TASK-04  
**Status**: PENDENTE

**O que fazer:**
- [ ] Confirmar que a imagem do produto em `product.html` está dentro de `.img-zoom-wrap` (criado em TASK-04)
- [ ] Verificar que o CSS de TASK-01 (`.img-zoom-wrap:hover img { transform: scale(1.08) }`) está aplicado corretamente
- [ ] Testar que o zoom não quebra o layout (container tem `overflow: hidden`)
- [ ] Testar em imagem com `object-fit: cover` — garantir que o zoom é visível e suave
- [ ] Verificar cursor `zoom-in` ao passar sobre a imagem

**Arquivos afetados:**
- `frontend/css/styles.css` (ajuste fino se necessário)
- `frontend/pages/product.html` (verificar estrutura HTML)

**Critério de done:**
- [ ] Hover na imagem ativa zoom `scale(1.08)` com transição `.4s`
- [ ] Nenhum overflow visível durante o zoom
- [ ] Cursor muda para `zoom-in`

---

## TASK-06 — Produtos relacionados (RF-05)

**Tipo**: `feat`  
**Estimativa**: 1h  
**Bloqueia**: —  
**Bloqueada por**: TASK-04  
**Status**: PENDENTE

**O que fazer:**
- [ ] Em `product.js`, após renderizar o produto principal, buscar `GET /products?category=<categoryId>&size=4`
- [ ] Filtrar o produto atual do resultado: `products.filter(p => p.id !== currentId)`
- [ ] Se `length === 0`, não renderizar a seção (esconder `#related-section`)
- [ ] Se `length > 0`, renderizar até 3 cards no container `#related-grid`
- [ ] Cards dos relacionados: mesma estrutura dos cards do catálogo, com link para `product.html?id=...`
- [ ] Adicionar seção no final de `product.html`:
  ```html
  <section id="related-section" class="related">
    <div class="container">
      <h2>Você também pode gostar</h2>
      <div class="products-grid" id="related-grid"></div>
    </div>
  </section>
  ```
- [ ] Estilizar `.related` no CSS: `padding: 4rem 0`, `border-top: 1px solid var(--border)`
- [ ] `.related h2`: Cormorant Garamond, `font-size: 1.5rem`, `font-weight: 300`, `margin-bottom: 2rem`

**Arquivos afetados:**
- `frontend/js/product.js` (adicionar fetch de relacionados)
- `frontend/pages/product.html` (adicionar seção `#related-section`)
- `frontend/css/styles.css` (adicionar `.related`)

**Critério de done:**
- [ ] Produtos da mesma categoria aparecem (excluindo o atual)
- [ ] Seção não aparece se não houver relacionados
- [ ] Clique nos cards relacionados navega para o produto correto
- [ ] Máximo de 3 cards exibidos

---

## TASK-07 — Skeleton no catálogo de produtos (RF-06)

**Tipo**: `feat`  
**Estimativa**: 1h  
**Bloqueia**: —  
**Bloqueada por**: TASK-01  
**Status**: PENDENTE

**O que fazer:**
- [ ] Em `products.js`, antes de fazer o fetch, renderizar 8 skeletons no `#products-grid`:
  ```js
  function renderSkeletons(count = 8) {
      grid.innerHTML = Array(count).fill(`
          <div class="product-card">
              <div class="skeleton skeleton-img"></div>
              <div class="product-info">
                  <div class="skeleton skeleton-line" style="width:70%"></div>
                  <div class="skeleton skeleton-line" style="width:40%"></div>
              </div>
          </div>
      `).join('');
  }
  ```
- [ ] Chamar `renderSkeletons()` no início de `loadProducts()`, antes do `await`
- [ ] Após o fetch, substituir pelo conteúdo real normalmente (já funciona pois usa `innerHTML =`)
- [ ] Aplicar o mesmo padrão na função de troca de página (paginação)

**Arquivos afetados:**
- `frontend/js/products.js` (adicionar `renderSkeletons` e chamada)

**Critério de done:**
- [ ] Skeletons aparecem antes dos produtos reais carregarem
- [ ] Skeletons também aparecem ao trocar de página na paginação
- [ ] Animação de pulse visível e suave

---

## TASK-08 — Estado vazio elegante no carrinho e pedidos (RF-07)

**Tipo**: `feat`  
**Estimativa**: 30min–1h  
**Bloqueia**: —  
**Bloqueada por**: —  
**Status**: PENDENTE

**O que fazer:**
- [ ] Em `cart.js` (ou no script de `cart.html`), quando o carrinho estiver vazio, renderizar:
  ```html
  <div class="empty-state">
      <p class="empty-title">Seu carrinho está vazio</p>
      <p class="empty-sub">Descubra nossa coleção e encontre algo que vale a pena.</p>
      <a href="/pages/products.html" class="btn btn-secondary">Ver coleção</a>
  </div>
  ```
- [ ] Em `orders.js` (ou no script de `orders.html`), quando não houver pedidos:
  ```html
  <div class="empty-state">
      <p class="empty-title">Nenhum pedido ainda</p>
      <p class="empty-sub">Sua primeira compra está a um clique de distância.</p>
      <a href="/pages/products.html" class="btn btn-secondary">Explorar produtos</a>
  </div>
  ```
- [ ] Estilizar `.empty-title` no CSS: Cormorant Garamond, `font-size: 1.5rem`, `font-weight: 300`, `margin-bottom: .5rem`
- [ ] Estilizar `.empty-sub` no CSS: `color: var(--text-muted)`, `font-size: .9rem`, `margin-bottom: 1.5rem`
- [ ] `.empty-state` já existe no CSS — apenas ajustar se necessário para incluir os novos elementos filhos

**Arquivos afetados:**
- `frontend/pages/cart.html` ou `frontend/js/cart.js` (modificar estado vazio)
- `frontend/pages/orders.html` ou `frontend/js/orders.js` (modificar estado vazio)
- `frontend/css/styles.css` (adicionar `.empty-title`, `.empty-sub`)

**Critério de done:**
- [ ] Carrinho vazio exibe mensagem elegante + link para produtos
- [ ] Pedidos vazios exibe mensagem elegante + link para produtos
- [ ] Sem ícones genéricos — texto puro no estilo VELN
- [ ] Link funciona corretamente

---

## Resumo da sprint

| Task | Descrição | Estimativa | Depende de |
|------|-----------|------------|------------|
| TASK-01 | CSS: skeleton + zoom | 30min–1h | — |
| TASK-02 | Home: seção manifesto | 30min–1h | — |
| TASK-03 | Home: produtos em destaque + skeleton | 1–2h | TASK-01 |
| TASK-04 | Página de produto completa | 2–3h | TASK-01 |
| TASK-05 | Zoom na imagem | 30min | TASK-04 |
| TASK-06 | Produtos relacionados | 1h | TASK-04 |
| TASK-07 | Skeleton no catálogo | 1h | TASK-01 |
| TASK-08 | Estado vazio (carrinho + pedidos) | 30min–1h | — |

**Total estimado**: 7–11h de desenvolvimento focado
