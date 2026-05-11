# PRD: Redesign Visual — Identidade VELN

**Status**: Implementado — QA visual pendente (TASK-17)  
**Data**: 2026-05-10  
**Autor**: Desenvolvedor

> [[index|← Índice]] · [[roadmap]] · [[frontend]] · [[tasks-redesign-veln]]

---

## 1. Contexto e Problema

O frontend atual do e-commerce não possui identidade visual definida. O CSS usa tema claro genérico (`#f5f5f5` de fundo, vermelho como accent) com navbar escura — resultado de um scaffold inicial funcional, mas sem coerência estética.

O projeto é o principal portfólio do desenvolvedor para estágio. Uma identidade visual forte e intencional demonstra maturidade de produto, não apenas capacidade técnica. A dor é: o site parece genérico e não reflete a proposta de uma loja de roupas de qualidade.

---

## 2. Objetivos

- **Objetivo principal**: Implementar identidade visual completa para a marca **VELN** em todas as telas do frontend, com estética dark e minimalista inspirada em marcas de moda premium (referência: Prada).
- **Métricas de sucesso**:
  - Todas as 6 telas com tema coerente aplicado
  - Contraste WCAG AA em textos principais
  - Zero emojis como ícones funcionais (substituídos por SVG ou texto)
  - Autopreenchimento do browser não quebra o tema escuro
- **Fora de escopo**:
  - Animações complexas ou transições entre páginas
  - Responsividade avançada (mobile-first completo)
  - Página de detalhe individual do produto (não existe ainda)
  - Dark/light mode toggle

---

## 3. Usuários e Personas

| Persona | Necessidade | Como essa feature ajuda |
|---------|-------------|------------------------|
| Visitante | Sentir confiança e desejo ao entrar no site | Estética premium reduz bounce e aumenta percepção de qualidade |
| Cliente (USER) | Navegar e comprar com clareza visual | Hierarquia tipográfica clara e contraste adequado facilitam a jornada |
| Recrutador de estágio | Avaliar capacidade técnica e visão de produto | Design intencional demonstra cuidado com UX além do código |

---

## 4. Requisitos Funcionais

### RF-01: Sistema de design global (CSS variables)
**Como** desenvolvedor, **quero** um conjunto de variáveis CSS centralizado, **para que** o tema seja consistente em todas as telas e fácil de ajustar.

**Critérios de aceite:**
- [x] Paleta definida: `--bg #000`, `--surface-1 #0d0d0d`, `--surface-2 #141414`, `--surface-3 #1c1c1c`, `--border #2a2a2a`, `--text #f0f0f0`, `--text-muted #888`, `--accent #fff`
- [x] Tipografia: Cormorant Garamond (headings) + Inter (corpo) via Google Fonts com `display=swap`
- [x] `--radius: 4px`, `--transition: .2s ease` definidos no `:root`
- [x] Override de `:-webkit-autofill` via `box-shadow inset 0 0 0 1000px #0a0a0a`

### RF-02: Navbar VELN
**Como** visitante, **quero** uma navbar discreta e elegante, **para que** a marca seja reconhecível sem competir com o conteúdo.

**Critérios de aceite:**
- [x] Logo "VELN" em Cormorant Garamond, `letter-spacing: .25em`, sem span colorido
- [x] Fundo preto puro, `border-bottom: 1px solid var(--border)`, links com underline `::after` scaleX no hover
- [x] Sem `box-shadow` — separação por borda sutil
- [x] Contador do carrinho em texto simples `var(--text-muted)`, sem badge colorido

### RF-03: Hero da home
**Como** visitante, **quero** uma seção inicial impactante, **para que** a proposta da marca seja comunicada em segundos.

**Critérios de aceite:**
- [x] Fundo `var(--bg)`, headline Cormorant Garamond `clamp(2.5rem, 6vw, 5rem)` peso 300
- [x] Subtítulo em Inter peso 300, `color: var(--text-muted)`
- [x] CTA primário (fundo branco → transparente no hover) e CTA outline — nenhum colorido
- [x] Sem gradientes — `background: var(--bg)` sólido, `<style>` inline removido do `index.html`

### RF-04: Cards de produto
**Como** cliente, **quero** ver os produtos de forma elegante, **para que** a atenção fique no produto, não na interface.

**Critérios de aceite:**
- [x] Card com fundo `var(--surface-2)`, `border-radius: 4px` (mínimo)
- [x] Imagem com `aspect-ratio: 3/4`, `object-fit: cover`
- [x] Nome em Cormorant Garamond, preço em Inter, categoria uppercase `letter-spacing: .1em`
- [x] Hover: botão com `opacity 0→1` + `translateY(4px→0)` — sem elevar o card
- [x] Sem `box-shadow` — grid com `gap: 1px` sobre fundo `var(--border)`

### RF-05: Formulários (login e cadastro)
**Como** visitante, **quero** um formulário limpo e legível no tema escuro, **para que** o preenchimento seja confortável.

**Critérios de aceite:**
- [x] Fundo do card: `var(--surface-1)`, `border: 1px solid var(--border)`
- [x] Input: `border: 1px solid var(--border)`, fundo `#0a0a0a`, texto `var(--text)`
- [x] Focus: `border-color: var(--accent)`, sem outline
- [x] Label: uppercase, `font-size: .7rem`, `letter-spacing: .1em`, `color: var(--text-muted)`
- [x] Override de autofill via `:-webkit-autofill` no `:root` do `styles.css`

### RF-06: Páginas de carrinho e pedidos
**Como** cliente, **quero** ver meu carrinho e pedidos no mesmo tema, **para que** a experiência seja coerente do início ao fim.

**Critérios de aceite:**
- [x] Itens do carrinho separados por `border-bottom: 1px solid var(--border)`, sem fundo de card
- [x] Summary: `background: var(--surface-2)`, `border: 1px solid var(--border)`, sticky
- [x] Badges: apenas borda colorida em tons de cinza, sem fundo pastel
- [x] Botão "Finalizar Compra": `.btn-primary` — fundo branco, texto preto (inversão intencional)

---

## 5. Requisitos Não-Funcionais

| Categoria      | Requisito |
|----------------|-----------|
| Acessibilidade | Contraste mínimo WCAG AA (4.5:1) em texto principal sobre fundo escuro |
| Performance    | Google Fonts carregado com `display=swap` para evitar FOIT |
| Compatibilidade | Chrome, Firefox, Edge — últimas 2 versões |
| Manutenibilidade | Todo o tema controlado por variáveis CSS em `:root`, zero valores hardcoded fora do `styles.css` |

---

## 6. Design Técnico (alto nível)

### Paleta de cores proposta

| Variável | Valor | Uso |
|----------|-------|-----|
| `--bg` | `#000000` | Fundo da página |
| `--surface-1` | `#0d0d0d` | Cards, formulários |
| `--surface-2` | `#141414` | Superfícies internas, navbar |
| `--surface-3` | `#1c1c1c` | Hover states, separadores |
| `--border` | `#2a2a2a` | Bordas de inputs e divisores |
| `--text` | `#f0f0f0` | Texto principal |
| `--text-muted` | `#888888` | Texto secundário |
| `--accent` | `#ffffff` | Accent principal (puro, minimalista) |
| `--accent-hover` | `#e0e0e0` | Hover do accent |

### Tipografia

```
Headings: 'Cormorant Garamond', serif — peso 300/400/600
Corpo: 'Inter', sans-serif — peso 300/400/500
Google Fonts: ?family=Cormorant+Garamond:wght@300;400;600&family=Inter:wght@300;400;500
```

### Arquivos afetados

| Arquivo | Tipo de mudança |
|---------|----------------|
| `frontend/css/styles.css` | Reescrita completa das variáveis e componentes |
| `frontend/index.html` | Refatoração da hero e feature cards |
| `frontend/pages/products.html` | Ajuste de classes e remoção de estilos inline |
| `frontend/pages/login.html` | Tema escuro nos formulários |
| `frontend/pages/register.html` | Tema escuro nos formulários |
| `frontend/pages/cart.html` | Reestilização de itens e resumo |
| `frontend/pages/orders.html` | Reestilização de badges de status |

### Mudanças no banco de dados
Nenhuma — mudança exclusivamente no frontend.

### Impacto em outros módulos
Nenhum impacto no backend. Os arquivos JS (`api.js`, `auth.js`, `cart.js`, `products.js`) não são modificados.

---

## 7. Riscos e Dependências

| Risco | Probabilidade | Impacto | Mitigação |
|-------|--------------|---------|-----------|
| Autofill do Chrome quebrando inputs | Alta | Médio | Override com `:-webkit-autofill` definindo background e color via `box-shadow` inset |
| Fonte serifada com legibilidade ruim em tamanhos pequenos | Média | Médio | Usar serifada apenas em headings ≥ 1.2rem; corpo sempre em sans-serif |
| Contraste insuficiente em texto muted sobre fundo escuro | Média | Alto | Validar com DevTools Accessibility checker antes de finalizar |
| Google Fonts lento em conexões ruins | Baixa | Baixo | `font-display: swap` + fallback system font definido |

---

## 8. Plano de Testes

- [ ] **Teste visual manual**: abrir cada página no browser e verificar coerência do tema (pendente TASK-17)
- [ ] **Contraste**: usar Chrome DevTools > Accessibility para verificar ratio em textos principais (pendente TASK-17)
- [x] **Autofill**: override implementado via `:-webkit-autofill` — testar manualmente com o browser
- [x] **Hover states**: navbar underline, cards, botões e links implementados
- [x] **Sem backend**: estados vazios estilizados com `.empty-state` em `var(--text-muted)`

---

## 9. Critérios de Done (DoD)

- [x] Todas as 6 telas com tema VELN dark aplicado
- [x] Nome "ModaShop" substituído por "VELN" em todos os arquivos (grep confirma zero hits)
- [x] Zero emojis como ícones funcionais (🛒 substituído por texto "Carrinho")
- [x] Variáveis CSS centralizadas — nenhum valor de cor hardcoded fora do `:root`
- [x] Google Fonts carregado com `@import` e `display=swap`
- [ ] Contraste WCAG AA validado nos textos principais (pendente TASK-17)
- [x] Autopreenchimento do Chrome não quebra o tema (override implementado)
- [ ] Testado no Chrome e Firefox (pendente TASK-17)
