# PRD: Sprint 03 — Frontend Editorial & Página de Produto

**Status**: Concluído  
**Data**: 2026-05-10  
**Concluído em**: 2026-05-10  
**Autor**: Desenvolvedor

---

## 1. Contexto e Problema

O frontend da VELN está funcional — login, catálogo, carrinho e pedidos funcionam corretamente — mas as páginas são esparsas e não comunicam o posicionamento de marca de luxo que o projeto quer transmitir. Um recrutador que abre o site hoje vê um e-commerce genérico, não uma marca com identidade própria.

Dois problemas principais:

1. **Home sem personalidade de marca**: existe apenas um hero e uma barra de valores. Não há nenhum conteúdo editorial que explique quem é a VELN, o que ela representa ou que destaque produtos de forma curada.
2. **Fluxo de produto incompleto**: clicar num produto adiciona ao carrinho diretamente, sem uma página de detalhe. Não é possível ver a descrição completa, uma imagem maior ou produtos relacionados — comportamento atípico em qualquer e-commerce.

---

## 2. Objetivos

- **Objetivo principal**: elevar a percepção de qualidade do portfólio transformando a VELN numa marca de luxo crível, com conteúdo editorial na home e uma página de produto completa.
- **Métricas de sucesso**:
  - Home com no mínimo 3 seções de conteúdo (hero, manifesto, produtos em destaque)
  - Página de detalhe existindo e acessível via clique no card
  - Zoom funcional na imagem do produto
  - Produtos relacionados carregando corretamente
  - Zero tela em branco durante carregamento (skeleton visível)
- **Fora de escopo**:
  - Upload real de imagem (continua por URL)
  - Leaderboard / ranking de compradores
  - Perfil customizável (nick, foto, efeitos)
  - Avaliações / reviews de produto
  - Integração com pagamento real

---

## 3. Usuários e Personas

| Persona | Necessidade | Como essa feature ajuda |
|---------|-------------|------------------------|
| Visitante (não logado) | Entender o que é a VELN antes de se cadastrar | Seção de manifesto e produtos em destaque na home |
| Cliente (USER logado) | Ver detalhes completos antes de comprar | Página de produto com imagem grande, descrição e relacionados |
| Recrutador | Avaliar a qualidade do projeto como portfólio | Site com identidade de marca coesa, fluxo de e-commerce completo |

---

## 4. Requisitos Funcionais

### RF-01: Seção de manifesto / sobre a VELN na home

**Como** visitante, **quero** ler sobre o que é a VELN, **para que** eu entenda a proposta da marca antes de explorar os produtos.

**Critérios de aceite:**
- [x] Seção posicionada logo abaixo do hero
- [x] Texto de manifesto em Cormorant Garamond, tom elegante e conciso
- [x] Layout minimalista — texto centralizado ou com elemento visual sutil à esquerda
- [x] Sem imagens externas obrigatórias (funciona com texto + divisor)

---

### RF-02: Seção de produtos em destaque na home

**Como** visitante, **quero** ver uma seleção curada de produtos na home, **para que** eu seja introduzido ao catálogo sem precisar ir à página de produtos.

**Critérios de aceite:**
- [x] Exibe os primeiros 3 ou 4 produtos da API (`GET /products?size=4`)
- [x] Cards no mesmo estilo da página de produtos (aspecto 3/4, hover com botão)
- [x] Título da seção em Cormorant Garamond ("Coleção" ou "Seleção")
- [x] Link "Ver coleção completa" que leva para `/pages/products.html`
- [x] Clique no card leva para a página de detalhe do produto (RF-03)

---

### RF-03: Página de detalhe do produto

**Como** cliente, **quero** ver todos os detalhes de um produto antes de comprar, **para que** eu tome uma decisão de compra informada.

**Critérios de aceite:**
- [x] Página acessível em `/pages/product.html?id=<uuid>`
- [x] Clique em qualquer card de produto (home e catálogo) redireciona para essa página
- [x] Exibe: imagem grande, nome, categoria, preço, descrição completa, estoque
- [x] Botão "Adicionar ao carrinho" funcional (mesmo comportamento do card)
- [x] Se produto inativo ou não encontrado, exibe mensagem de erro elegante
- [x] Breadcrumb ou link "← Voltar para produtos"

---

### RF-04: Zoom na imagem do produto

**Como** cliente, **quero** aproximar a imagem do produto, **para que** eu possa ver os detalhes da peça antes de comprar.

**Critérios de aceite:**
- [x] Hover na imagem ativa o zoom (CSS `transform: scale` + `overflow: hidden` no container)
- [x] Transição suave (`.3s ease`)
- [x] Cursor muda para `zoom-in` ao passar sobre a imagem
- [x] Não abre modal — zoom inline, sem interromper o fluxo da página

---

### RF-05: Produtos relacionados

**Como** cliente, **quero** ver outros produtos da mesma categoria, **para que** eu descubra mais opções sem voltar para o catálogo.

**Critérios de aceite:**
- [x] Exibe até 3 produtos da mesma categoria, excluindo o produto atual
- [x] Usa `GET /products?category=<id>&size=4` e filtra o atual no frontend
- [x] Layout em linha horizontal, mesmo estilo dos cards do catálogo
- [x] Título "Você também pode gostar" ou similar em Cormorant Garamond
- [x] Se não houver produtos relacionados, seção não aparece

---

### RF-06: Loading skeleton nos cards de produto

**Como** visitante, **quero** ver um indicador visual enquanto os produtos carregam, **para que** a página não pareça quebrada durante o fetch.

**Critérios de aceite:**
- [x] Skeleton com mesmas dimensões do card real (aspecto 3/4 + área de texto)
- [x] Animação de pulse (`@keyframes` com `opacity` oscilando)
- [x] Fundo `var(--surface-3)` para contrastar com `var(--surface-2)` do card
- [x] Exibe 4 skeletons por padrão enquanto carrega
- [x] Substituído pelos cards reais assim que a resposta chega

---

### RF-07: Estado vazio elegante no carrinho e pedidos

**Como** cliente, **quero** ver uma mensagem adequada quando não há itens no carrinho ou pedidos, **para que** a página não pareça quebrada.

**Critérios de aceite:**
- [x] Carrinho vazio: mensagem + link direto para `/pages/products.html`
- [x] Pedidos vazios: mensagem + link para `/pages/products.html`
- [x] Texto em Cormorant Garamond, tom elegante (não "Nenhum item encontrado")
- [x] Sem ícones genéricos — manter o minimalismo VELN

---

## 5. Requisitos Não-Funcionais

| Categoria    | Requisito |
|--------------|-----------|
| Performance  | Página de produto carrega em menos de 2s em conexão local |
| Estética     | Toda nova UI usa exclusivamente variáveis CSS do `:root` — zero cores hardcoded |
| Consistência | Tipografia, espaçamentos e interações seguem o padrão já estabelecido nas outras páginas |
| Acessibilidade | Imagens com `alt` descritivo; links com texto significativo (não "clique aqui") |

---

## 6. Design Técnico (alto nível)

### Novos arquivos
```
frontend/pages/product.html          → página de detalhe do produto
frontend/js/product.js               → lógica da página de detalhe
```

### Arquivos modificados
```
frontend/index.html                  → seção manifesto + seção produtos em destaque
frontend/js/products.js              → clique no card redireciona para product.html
frontend/pages/products.html         → clique no card redireciona para product.html (idem)
frontend/css/styles.css              → skeleton, zoom, estilos novos
frontend/pages/cart.html             → estado vazio
frontend/pages/orders.html           → estado vazio
```

### Endpoints da API usados (sem mudanças no backend)
```
GET /products?size=4                     → produtos em destaque na home
GET /products/{id}                       → detalhe do produto
GET /products?category={id}&size=4       → produtos relacionados
```

Nenhum endpoint novo é necessário — o backend já suporta tudo.

### Mudanças no banco de dados
Nenhuma. Esta sprint é 100% frontend.

---

## 7. Riscos e Dependências

| Risco                                                                                            | Probabilidade | Impacto | Mitigação                                                                                           |
| ------------------------------------------------------------------------------------------------ | ------------- | ------- | --------------------------------------------------------------------------------------------------- |
| Imagens de produto com URLs quebradas deixam a home feia                                         | Alta          | Alto    | Placeholder CSS elegante com `object-fit: cover` e fundo `var(--surface-3)` para imagens que falham |
| Zoom inline pode quebrar o layout em mobile                                                      | Média         | Médio   | Testar em viewport 375px; desabilitar zoom em mobile se necessário                                  |
| Produtos relacionados com apenas 1 produto na categoria retorna array vazio após filtrar o atual | Média         | Baixo   | Verificar `length > 0` antes de renderizar a seção                                                  |
| Alterar o comportamento do clique no card pode quebrar o "Adicionar ao carrinho" existente       | Baixa         | Alto    | Card redireciona para produto; botão de carrinho continua funcionando dentro da página de detalhe   |

---

## 8. Plano de Testes

- [x] **Testes manuais (frontend):**
  - Home carrega manifesto e produtos em destaque sem erro no console
  - Clique no card da home leva para `/pages/product.html?id=<uuid>`
  - Clique no card do catálogo leva para a mesma página
  - Zoom na imagem funciona no hover
  - Produtos relacionados aparecem (e somem se não houver)
  - Botão "Adicionar ao carrinho" na página de detalhe funciona
  - Skeleton aparece e é substituído pelos cards
  - Carrinho vazio exibe mensagem e link
  - Pedidos vazios exibe mensagem e link
  - ID de produto inválido na URL exibe mensagem de erro

- [x] **Nenhum teste de backend necessário** — sprint é 100% frontend, API não muda

---

## 9. Critérios de Done (DoD)

- [x] Todos os RFs implementados e testados manualmente
- [x] Zero cores hardcoded fora do `:root` nos arquivos novos/editados
- [x] Zero rastros de "ModaShop" em qualquer arquivo
- [x] Console do browser sem erros durante o fluxo completo
- [x] Responsividade básica: home e página de produto navegáveis em mobile (375px)
- [x] `git status` limpo (`.env` ausente)
