# PRD: Redesign Visual — Identidade VELN

**Status**: Rascunho  
**Data**: 2026-05-10  
**Autor**: Desenvolvedor

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
- [ ] Paleta definida com: preto base (`#000`), superfícies em cinza escuro (`#0d0d0d`, `#141414`, `#1c1c1c`), texto branco/off-white, accent neutro (bege ou branco puro)
- [ ] Tipografia: fonte serifada via Google Fonts para headings (ex: *Playfair Display* ou *Cormorant Garamond*), sans-serif para corpo (ex: *Inter*)
- [ ] Variáveis de espaçamento, border-radius mínimo, transições definidas
- [ ] Override de `:-webkit-autofill` para inputs escuros

### RF-02: Navbar VELN
**Como** visitante, **quero** uma navbar discreta e elegante, **para que** a marca seja reconhecível sem competir com o conteúdo.

**Critérios de aceite:**
- [ ] Logo "VELN" em tipografia serifada, sem span colorido
- [ ] Fundo preto puro, links em branco com underline animado no hover
- [ ] Sem bordas ou sombras pesadas — separação por contraste de cor
- [ ] Contador do carrinho sem badge colorido agressivo

### RF-03: Hero da home
**Como** visitante, **quero** uma seção inicial impactante, **para que** a proposta da marca seja comunicada em segundos.

**Critérios de aceite:**
- [ ] Fundo preto com tipografia serifada grande (headline tipo editorial)
- [ ] Subtítulo em peso leve, cor off-white com opacidade reduzida
- [ ] Máximo dois CTAs: primário (borda branca com hover fill) e nenhum secundário colorido
- [ ] Sem gradientes — cor sólida ou imagem de fundo escura

### RF-04: Cards de produto
**Como** cliente, **quero** ver os produtos de forma elegante, **para que** a atenção fique no produto, não na interface.

**Critérios de aceite:**
- [ ] Card com fundo `#141414`, sem border-radius exagerado (máx 4px)
- [ ] Imagem ocupa área generosa (aspect-ratio 3/4, proporção moda)
- [ ] Nome do produto em serifada, preço em sans-serif, categoria em uppercase tracking-wide
- [ ] Hover: revelação suave do botão (sem translateY agressivo)
- [ ] Sem box-shadow — separação por cor de fundo

### RF-05: Formulários (login e cadastro)
**Como** visitante, **quero** um formulário limpo e legível no tema escuro, **para que** o preenchimento seja confortável.

**Critérios de aceite:**
- [ ] Fundo do card de formulário: `#0d0d0d` ou `#141414`
- [ ] Input com borda `1px solid #2a2a2a`, fundo `#0a0a0a`, texto branco
- [ ] Focus: borda branca, sem outline colorido
- [ ] Label em uppercase, tamanho pequeno, letter-spacing
- [ ] Override de autofill do Chrome

### RF-06: Páginas de carrinho e pedidos
**Como** cliente, **quero** ver meu carrinho e pedidos no mesmo tema, **para que** a experiência seja coerente do início ao fim.

**Critérios de aceite:**
- [ ] Itens do carrinho com separação por linha sutil (`#1f1f1f`)
- [ ] Resumo do pedido em superfície levemente diferente (`#141414`)
- [ ] Badges de status reestilizados para o tema escuro (sem fundos pastel)
- [ ] Botão de finalizar compra em branco sólido com texto preto (inversão intencional)

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

- [ ] **Teste visual manual**: abrir cada página no browser e verificar coerência do tema
- [ ] **Contraste**: usar Chrome DevTools > Accessibility para verificar ratio em textos principais
- [ ] **Autofill**: testar login/cadastro com preenchimento automático do browser
- [ ] **Hover states**: verificar todos os botões, links e cards interativos
- [ ] **Sem backend**: confirmar que o redesign funciona mesmo sem a API rodando (dados mockados ou estados vazios estilizados)

---

## 9. Critérios de Done (DoD)

- [ ] Todas as 6 telas com tema VELN dark aplicado
- [ ] Nome "ModaShop" substituído por "VELN" em todos os arquivos
- [ ] Zero emojis como ícones funcionais
- [ ] Variáveis CSS centralizadas — nenhum valor de cor hardcoded fora do `:root`
- [ ] Google Fonts carregado corretamente com `display=swap`
- [ ] Contraste WCAG AA validado nos textos principais
- [ ] Autopreenchimento do Chrome não quebra o tema
- [ ] Testado no Chrome e Firefox
