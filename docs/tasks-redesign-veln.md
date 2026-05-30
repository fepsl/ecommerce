# Tasks: Redesign Visual — Identidade VELN

> Gerado a partir de: [[prd-redesign-veln.md]]  
> Data: 2026-05-10  
> Status: ✅ Concluído — todas as 7 tasks entregues

---

## Dependências entre tasks

```
TASK-01 → TASK-02 → TASK-03
                  → TASK-04
                  → TASK-05
                  → TASK-06
          TASK-02 → TASK-07
```

TASK-01 (CSS base) bloqueia tudo. TASK-02 (navbar + layout) bloqueia as páginas internas. As tasks de página (03–06) são independentes entre si após TASK-02. TASK-07 (QA) bloqueia tudo.

---

## TASK-01 — Sistema de design: CSS global VELN

**Tipo**: `refactor`  
**Estimativa**: 1–2h  
**Bloqueia**: TASK-02, TASK-03, TASK-04, TASK-05, TASK-06  
**Bloqueada por**: —  
**Status**: ✅ CONCLUÍDO

**O que fazer:**
- [x] Substituir todas as variáveis CSS em `:root` pela paleta VELN dark
- [x] Adicionar `@import` do Google Fonts (Cormorant Garamond + Inter) com `display=swap`
- [x] Definir `font-family` base: Inter no `body`, Cormorant Garamond nos headings via classe ou seletor
- [x] Reescrever estilos base: `body`, `a`, `::selection`
- [x] Adicionar override de autofill: `input:-webkit-autofill` com `box-shadow: inset 0 0 0 1000px #0a0a0a`
- [x] Remover variáveis antigas (`--primary`, `--accent` vermelho, `--bg` claro)

**Paleta a aplicar:**
```css
--bg: #000;
--surface-1: #0d0d0d;
--surface-2: #141414;
--surface-3: #1c1c1c;
--border: #2a2a2a;
--text: #f0f0f0;
--text-muted: #888;
--accent: #ffffff;
--accent-hover: #e0e0e0;
--radius: 4px;
--transition: .25s ease;
```

**Arquivos afetados:**
- `frontend/css/styles.css` (reescrita do topo: reset, variáveis, tipografia base)

**Critério de done:**
- [x] Abrindo qualquer página, o fundo é preto e o texto é branco/off-white
- [x] Google Fonts carregando (verificar na aba Network do DevTools)
- [x] Nenhum valor de cor hardcoded fora do `:root`

---

## TASK-02 — Navbar e layout base

**Tipo**: `feat`  
**Estimativa**: 1h  
**Bloqueia**: TASK-03, TASK-04, TASK-05, TASK-06  
**Bloqueada por**: TASK-01  
**Status**: ✅ CONCLUÍDO

**O que fazer:**
- [x] Reescrever estilos da `.navbar`: fundo `#000`, sem sombra, border-bottom `1px solid var(--border)`
- [x] Logo "VELN" em Cormorant Garamond, sem `<span>` colorido, letter-spacing amplo
- [x] Links da navbar: cor `var(--text-muted)`, hover `var(--text)` com underline que cresce via `::after` (transform scaleX)
- [x] Contador do carrinho: remover badge vermelho — usar parênteses ou texto simples `(3)`
- [x] Atualizar `index.html`: substituir `Moda<span>Shop</span>` por `VELN` em todas as referências
- [x] Substituir emojis do carrinho (🛒) por texto `Bolsa` ou SVG inline simples
- [x] Reescrever `.container` e `.page-header` para o novo sistema

**Arquivos afetados:**
- `frontend/css/styles.css` (seção navbar, layout)
- `frontend/index.html` (navbar markup)
- `frontend/pages/products.html` (navbar markup)
- `frontend/pages/login.html` (navbar markup)
- `frontend/pages/register.html` (navbar markup)
- `frontend/pages/cart.html` (navbar markup)
- `frontend/pages/orders.html` (navbar markup)

**Critério de done:**
- [x] Navbar visualmente coerente com tema dark em todas as 6 páginas
- [x] Nome "VELN" aparece em todas as telas, sem rastro de "ModaShop"
- [x] Nenhum emoji funcional visível

---

## TASK-03 — Home: hero e feature cards

**Tipo**: `feat`  
**Estimativa**: 1–2h  
**Bloqueia**: TASK-07  
**Bloqueada por**: TASK-01, TASK-02  
**Status**: ✅ CONCLUÍDO

**O que fazer:**
- [x] Reescrever seção `.hero` em `index.html`: remover gradiente, fundo `#000`, adicionar linha fina decorativa antes do headline
- [x] Headline em Cormorant Garamond, tamanho `clamp(2.5rem, 6vw, 5rem)`, peso 300
- [x] Subtítulo em Inter 300, `var(--text-muted)`, máx 480px de largura, centralizado
- [x] Reescrever botões hero: um único CTA com borda `1px solid var(--accent)`, fundo transparente, hover inverte (fundo branco, texto preto)
- [x] Remover seção `.features` com emojis — substituir por uma linha de 4 textos simples em uppercase/tracking (ex: `FRETE GRÁTIS · TROCA EM 30 DIAS · COMPRA SEGURA · PARCELAMENTO`)
- [x] Extrair estilos inline do `<style>` do `index.html` para `styles.css`

**Arquivos afetados:**
- `frontend/index.html` (hero markup, remoção de `<style>` inline, feature section)
- `frontend/css/styles.css` (estilos `.hero`, `.hero-tagline`, `.btn-outline`)

**Critério de done:**
- [x] Home abre com fundo preto puro, headline serifada grande
- [x] Nenhum `<style>` inline no `index.html`
- [x] CTA com hover de inversão funcionando

---

## TASK-04 — Página de produtos e cards

**Tipo**: `feat`  
**Estimativa**: 1–2h  
**Bloqueia**: TASK-07  
**Bloqueada por**: TASK-01, TASK-02  
**Status**: ✅ CONCLUÍDO

**O que fazer:**
- [x] Reescrever `.product-card`: fundo `var(--surface-2)`, border-radius `var(--radius)` (4px), sem box-shadow
- [x] Imagem: mudar `aspect-ratio` de `1` para `3/4` (proporção editorial de moda)
- [x] `.product-info h3`: fonte Cormorant Garamond, peso 400
- [x] `.product-info .category`: uppercase, `letter-spacing: .1em`, tamanho `.7rem`, cor `var(--text-muted)`
- [x] `.product-info .price`: Inter 500, cor `var(--text)` (remover accent vermelho)
- [x] Hover do card: revelar botão com `opacity: 0 → 1` + `translateY(4px → 0)`, sem elevação do card inteiro
- [x] Reescrever `.filters`: fundo `var(--surface-1)`, inputs com estilo dark (consistente com TASK-05)
- [x] Reescrever paginação: botões sem borda visível, apenas texto, underline no hover

**Arquivos afetados:**
- `frontend/css/styles.css` (seções produto, filtros, paginação)
- `frontend/pages/products.html` (verificar se há estilos inline a remover)

**Critério de done:**
- [x] Cards com proporção 3/4 e fundo escuro visíveis na listagem
- [x] Hover do card revela botão sem animação agressiva
- [x] Filtros com tema dark coerente

---

## TASK-05 — Formulários: login e cadastro

**Tipo**: `feat`  
**Estimativa**: 1h  
**Bloqueia**: TASK-07  
**Bloqueada por**: TASK-01, TASK-02  
**Status**: ✅ CONCLUÍDO

**O que fazer:**
- [x] Reescrever `.form-card`: fundo `var(--surface-1)`, sem box-shadow, border `1px solid var(--border)`
- [x] `.form-card h1`: Cormorant Garamond, peso 300, letter-spacing amplo
- [x] `.form-group label`: uppercase, `.75rem`, `letter-spacing: .08em`, `var(--text-muted)`
- [x] `input`, `select`: fundo `#0a0a0a`, borda `1px solid var(--border)`, texto `var(--text)`, border-radius `var(--radius)`
- [x] `input:focus`: `border-color: var(--accent)`, sem outline colorido
- [x] Confirmar que o override de autofill do TASK-01 resolve o fundo amarelo do Chrome
- [x] `.btn-primary` global: reescrever para fundo `var(--accent)` (branco), texto `#000`, hover `var(--accent-hover)`
- [x] `.btn-secondary`: borda `var(--border)`, texto `var(--text-muted)`, hover borda/texto `var(--accent)`

**Arquivos afetados:**
- `frontend/css/styles.css` (seções formulário, botões)
- `frontend/pages/login.html` (verificar estilos inline)
- `frontend/pages/register.html` (verificar estilos inline)

**Critério de done:**
- [x] Formulário de login com fundo escuro e inputs dark
- [x] Autofill do Chrome não exibe fundo amarelo
- [x] Botão primário: branco com texto preto

---

## TASK-06 — Carrinho e pedidos

**Tipo**: `feat`  
**Estimativa**: 1h  
**Bloqueia**: TASK-07  
**Bloqueada por**: TASK-01, TASK-02  
**Status**: ✅ CONCLUÍDO

**O que fazer:**
- [x] `.cart-item`: remover fundo de card — usar apenas border-bottom `1px solid var(--border)` como separador
- [x] `.cart-item-price`: cor `var(--text)` (remover accent vermelho)
- [x] `.cart-summary`: fundo `var(--surface-2)`, border `1px solid var(--border)`, sem shadow
- [x] Botão "Finalizar Compra": fundo `var(--accent)` (branco), texto `#000` — inversão proposital para destaque máximo
- [x] Reescrever badges de status para tema dark:
  - `badge-pending`: fundo `#1a1500`, texto `#c9a227`
  - `badge-paid`: fundo `#001a0d`, texto `#2eb87a`
  - `badge-shipped`: fundo `#00101a`, texto `#3a9fd6`
  - `badge-delivered`: fundo `#001a08`, texto `#27c96e`
  - `badge-cancelled`: fundo `#1a0000`, texto `#e05252`
- [x] `.order-card`: fundo `var(--surface-1)`, border `1px solid var(--border)`, sem shadow

**Arquivos afetados:**
- `frontend/css/styles.css` (seções carrinho, pedidos, badges)
- `frontend/pages/cart.html` (verificar estilos inline)
- `frontend/pages/orders.html` (verificar estilos inline)

**Critério de done:**
- [x] Página do carrinho com separadores sutis e botão de checkout branco
- [x] Badges de status legíveis no tema dark
- [x] Nenhum fundo pastel nos badges

---

## TASK-07 — QA visual e refinamentos finais

**Tipo**: `test`  
**Estimativa**: 1h  
**Bloqueia**: —  
**Bloqueada por**: TASK-03, TASK-04, TASK-05, TASK-06  
**Status**: ✅ CONCLUÍDO

**O que fazer:**
- [x] Abrir cada uma das 6 páginas no Chrome e Firefox e verificar coerência visual
- [x] Usar DevTools > Accessibility > Color Contrast para validar ratio ≥ 4.5:1 nos textos principais
- [x] Verificar hover states: navbar links, cards de produto, botões, paginação
- [x] Testar autofill no login e cadastro (Chrome e Firefox)
- [x] Buscar por valores hardcoded de cor fora do `:root` (`grep -r "#[0-9a-fA-F]" frontend/css/styles.css`)
- [x] Buscar por rastros de "ModaShop" em todos os arquivos HTML
- [x] Verificar se Google Fonts aparece na aba Network (status 200, não bloqueado)
- [x] Verificar `empty-state` e estados de loading no tema escuro

**Arquivos afetados:**
- Qualquer arquivo que apresentar problema durante a inspeção

**Critério de done:**
- [x] Todos os DoD do PRD marcados como concluídos
- [x] Nenhuma cor hardcoded fora do `:root`
- [x] Contraste WCAG AA confirmado nos textos principais
- [x] Zero ocorrências de "ModaShop" nos HTMLs
