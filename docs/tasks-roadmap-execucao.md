# Tasks: Roadmap de Execução — Do Scaffold ao Portfólio

> Gerado a partir de: [[prd-roadmap-execucao.md]]  
> Data: 2026-05-10  
> Última atualização: 2026-05-10 (sessão de execução)

---

## Dependências entre tasks

```
TASK-01 → TASK-02 → TASK-03 → TASK-04
                             → TASK-05
                             → TASK-06
                  → TASK-07 → TASK-08
                            → TASK-09
                            → TASK-10
          TASK-02 → TASK-11 → TASK-12
                            → TASK-13
                            → TASK-14
                            → TASK-15
                    TASK-11 → TASK-16
          TASK-16 → TASK-17
```

**Fase 1** (TASK-01 a 06): ambiente e validação manual — bloqueia tudo  
**Fase 2** (TASK-07 a 10): testes — bloqueia Fase 3 e 4  
**Fase 3** (TASK-11 a 16): redesign VELN — independente dos testes, mas requer backend funcional  
**Fase 4** (TASK-17): Docker e entrega final — bloqueia nada, é o fechamento

---

## FASE 1 — Fazer funcionar

---

## TASK-01 — Ambiente local: banco e variáveis de ambiente ✅

**Tipo**: `chore`  
**Estimativa**: 30min–1h  
**Bloqueia**: TASK-02  
**Bloqueada por**: —  
**Status**: CONCLUÍDA

**O que foi feito:**
- [x] PostgreSQL 17 já estava instalado e rodando como serviço (`postgresql-x64-17`)
- [x] `.env` criado a partir do `.env.example` com `DB_URL=jdbc:postgresql://localhost:5432/ecommerce` (sem Docker)
- [x] JWT_SECRET gerado com 68 chars alfanuméricos
- [x] Banco `ecommerce` criado via psql com usuário `ecommerce`
- [x] `.env` confirmado ausente no `git status` (`.gitignore` correto)

**Desvios do plano original:**
- Docker Desktop não estava instalado — banco criado localmente via PostgreSQL nativo
- `DB_URL` usa `localhost` em vez de `postgres` (nome do container Docker)

**Critério de done:**
- [x] Banco `ecommerce` acessível com usuário `ecommerce`
- [x] `.env` não aparece no `git status`

---

## TASK-02 — Compilar e subir o backend pela primeira vez ✅

**Tipo**: `chore`  
**Estimativa**: 1–3h  
**Bloqueia**: TASK-03, TASK-04, TASK-05, TASK-06, TASK-07  
**Bloqueada por**: TASK-01  
**Status**: CONCLUÍDA

**O que foi feito:**
- [x] Maven instalado via Chocolatey (versão 3.9.15)
- [x] `JAVA_HOME` apontado para JDK 21 (Temurin 21.0.8 já instalado)
- [x] `mvn clean compile` — BUILD SUCCESS, 50 arquivos compilados
- [x] 2 bugs corrigidos antes do primeiro startup bem-sucedido (ver abaixo)
- [x] `mvn spring-boot:run` — backend sobe em ~5s na porta 8080
- [x] Flyway aplicou V1 e V2 com sucesso (tabelas + seed)
- [x] Swagger UI acessível em `http://localhost:8080/swagger-ui.html`

**Bugs corrigidos:**
1. **`CHAR(2)` vs `VARCHAR(2)` em `addresses.state`**: migration usava `CHAR(2)` mas Hibernate esperava `VARCHAR(2)`. Corrigido em `V1__create_tables.sql` e banco recriado.
2. **Hash BCrypt inválido no seed**: `V2__insert_seed_data.sql` tinha hash que não correspondia a `senha123`. Substituído pelo hash correto gerado pela própria API.

**Critério de done:**
- [x] `mvn spring-boot:run` sobe sem erros de startup
- [x] Tabelas e dados de seed visíveis no banco
- [x] Swagger UI carrega e exibe todos os endpoints

---

## TASK-03 — Validar autenticação (register e login) ✅

**Tipo**: `test`  
**Estimativa**: 30min–1h  
**Bloqueia**: TASK-04, TASK-05  
**Bloqueada por**: TASK-02  
**Status**: CONCLUÍDA

**Resultados dos testes:**
- [x] `POST /auth/register` com body válido → retorna `{ token, name, email, role }`
- [x] `POST /auth/register` com email duplicado → `409 Conflict` com mensagem correta
- [x] `POST /auth/login` com admin do seed (`admin@ecommerce.com` / `senha123`) → JWT válido, role=ADMIN
- [x] `POST /auth/login` com cliente do seed (`cliente@ecommerce.com` / `senha123`) → JWT válido, role=USER
- [x] `POST /auth/login` com senha errada → `401 Unauthorized`

**Critério de done:**
- [x] Register retorna JWT em caso de sucesso
- [x] Register retorna 409 em email duplicado
- [x] Login retorna JWT em caso de sucesso
- [x] Login retorna 401 em senha errada

---

## TASK-04 — Validar produtos, categorias e pedidos ✅

**Tipo**: `test`  
**Estimativa**: 1–2h  
**Bloqueia**: TASK-06  
**Bloqueada por**: TASK-03  
**Status**: CONCLUÍDA

**Resultados dos testes:**
- [x] `GET /products` sem token → lista paginada com 6 produtos (5 seed + 1 criado no teste)
- [x] `GET /products?name=camiseta` → filtro retorna 2 resultados corretamente
- [x] `GET /categories` sem token → 4 categorias do seed
- [x] `POST /products` com token USER → `403 Forbidden`
- [x] `POST /products` com token ADMIN → produto criado com sucesso
- [x] `POST /orders` com quantidade válida → pedido criado, status=PENDING, total correto
- [x] `POST /orders` com quantidade > estoque → `422 Unprocessable Entity`
- [x] `GET /orders/me` → lista apenas pedidos do usuário autenticado
- [x] `GET /users/me` → retorna perfil correto

**Bug corrigido:**
- **`lower(bytea)` no PostgreSQL**: query JPQL em `ProductRepository.findWithFilters` usava `LOWER(:name)` com parâmetro nulo, e PostgreSQL não conseguia inferir o tipo. Corrigido com `CAST(:name AS String)`.

**Critério de done:**
- [x] Todos os endpoints respondem com status HTTP correto
- [x] Validação de estoque funciona
- [x] Controle de acesso funciona (USER não cria produto, pedidos isolados por usuário)

---

## TASK-05 — Validar frontend conectado com API ✅

**Tipo**: `test`  
**Estimativa**: 1–2h  
**Bloqueia**: TASK-06  
**Bloqueada por**: TASK-03  
**Status**: CONCLUÍDA

**O que foi feito:**
- [x] CORS ausente em `SecurityConfig.java` — adicionado `CorsFilter` com `allowedOriginPatterns("*")`
- [x] Frontend servido com `python -m http.server 3000` a partir de `frontend/`
- [x] Produtos carregam na home e na página de produtos sem erro no console
- [x] Login com `cliente@ecommerce.com` / `senha123` funcionou
- [x] Cadastro de novo usuário funcionou
- [x] Produto adicionado ao carrinho — contador atualiza corretamente
- [x] Finalizar compra criou pedido e redirecionou para `orders.html`
- [x] Pedidos aparecem listados com status correto

**Bug corrigido:**
- **CORS não configurado**: `SecurityConfig` não tinha `.cors(...)`. Adicionado `corsConfigurationSource()` bean com `allowedOriginPatterns("*")`, métodos HTTP e headers liberados.

**Critério de done:**
- [x] Produtos aparecem na tela sem erro no console
- [x] Login e cadastro funcionam pelo frontend
- [x] Carrinho persiste no localStorage
- [x] Pedidos aparecem após compra

---

## TASK-06 — Corrigir bugs encontrados na Fase 1

**Tipo**: `fix`  
**Estimativa**: variável  
**Bloqueia**: TASK-07  
**Bloqueada por**: TASK-04, TASK-05  
**Status**: CONCLUÍDA

**Bugs já corrigidos (durante TASK-02, TASK-04 e TASK-05):**
- [x] `CHAR(2)` → `VARCHAR(2)` em `V1__create_tables.sql` (`addresses.state`)
- [x] Hash BCrypt incorreto em `V2__insert_seed_data.sql`
- [x] `lower(bytea)` em `ProductRepository` → `CAST(:name AS String)`
- [x] CORS ausente em `SecurityConfig.java` → adicionado `corsConfigurationSource()` bean

**Critério de done:**
- [x] Fluxo completo funciona sem erros: registro → login → produtos → carrinho → pedido
- [x] Console do browser sem erros durante o fluxo principal

---

## FASE 2 — Testes

---

## TASK-07 — Configurar ambiente de testes (H2 + application-test.yml) ✅

**Tipo**: `chore`  
**Estimativa**: 30min  
**Bloqueia**: TASK-08, TASK-09, TASK-10  
**Bloqueada por**: TASK-06  
**Status**: CONCLUÍDA

**O que foi feito:**
- [x] `application-test.yml` já estava correto: H2 em modo PostgreSQL, Flyway desabilitado, JWT secret definido
- [x] `mvn test` — 6 testes, 0 falhas, BUILD SUCCESS

**Critério de done:**
- [x] `mvn test` passa sem erros
- [x] `EcommerceApplicationTests` e `ProductServiceTest` passam

---

## TASK-08 — Testes unitários: AuthService ✅

**Tipo**: `test`  
**Estimativa**: 1–2h  
**Bloqueia**: —  
**Bloqueada por**: TASK-07  
**Status**: CONCLUÍDA

**O que foi feito:**
- [x] `AuthServiceTest.java` criado com 5 testes Mockito
- [x] `register()` com email novo → retorna `AuthResponse` com token
- [x] `register()` com email duplicado → lança `EmailAlreadyExistsException`, não chama `save()`
- [x] `register()` codifica senha com BCrypt antes de salvar
- [x] `login()` com credenciais válidas → retorna `AuthResponse` com token
- [x] `login()` com senha errada → lança `BadCredentialsException`, não busca usuário
- [x] `mvn test` — 11 testes, 0 falhas, BUILD SUCCESS

**Critério de done:**
- [x] Todos os testes do arquivo passam
- [x] `mvn test` continua passando no total

---

## TASK-09 — Testes unitários: ProductService ✅

**Tipo**: `test`  
**Estimativa**: 1–2h  
**Bloqueia**: —  
**Bloqueada por**: TASK-07  
**Status**: CONCLUÍDA

**O que foi feito:**
- [x] `findAll()` sem filtros → delega ao repositório com todos os parâmetros nulos
- [x] `findAll()` com categoria → repassa UUID correto ao repositório
- [x] `findById()` produto ativo → retorna `ProductResponse`
- [x] `findById()` produto inativo → lança `ResourceNotFoundException`
- [x] `findById()` inexistente → lança `ResourceNotFoundException`
- [x] `create()` com dados válidos → salva e retorna resposta
- [x] `create()` com categoria inexistente → lança `ResourceNotFoundException`, não chama `save()`
- [x] `deactivate()` → seta `active = false`, chama `save()`
- [x] `mvn test` — 14 testes, 0 falhas, BUILD SUCCESS

**Critério de done:**
- [x] Todos os testes do arquivo passam
- [x] `mvn test` continua passando no total

---

## TASK-10 — Testes unitários: OrderService + integração de autenticação ✅

**Tipo**: `test`  
**Estimativa**: 2–3h  
**Bloqueia**: —  
**Bloqueada por**: TASK-07  
**Status**: CONCLUÍDA

**O que foi feito:**
- [x] `OrderServiceTest.java` — 6 testes Mockito:
  - `create()` lança `InsufficientStockException` se quantidade > stock
  - `create()` salva `unit_price` igual ao preço atual do produto
  - `create()` decrementa estoque e chama `productRepository.save()`
  - `findByUser()` retorna apenas pedidos do usuário informado
  - `findById()` USER tentando pedido alheio → lança `UnauthorizedException`
  - `findById()` ADMIN pode ver pedido de qualquer usuário
- [x] `AuthIntegrationTest.java` — 4 testes `@SpringBootTest` + `MockMvc`:
  - `POST /auth/register` → 201 com token no corpo
  - `POST /auth/login` → 200 com token no corpo
  - Endpoint protegido sem token → 401 (corrigido via `AuthenticationEntryPoint`)
  - Endpoint protegido com token válido → 200
- [x] Bug corrigido: `SecurityConfig` retornava 403 para anônimos — adicionado `authenticationEntryPoint` que retorna 401 (correto para API REST)
- [x] `mvn test` — 24 testes, 0 falhas, BUILD SUCCESS

**Critério de done:**
- [x] Todos os testes passam
- [x] `mvn test` passa no total com H2

---

## FASE 3 — Redesign Visual VELN

---

## TASK-11 — Sistema de design CSS global VELN

**Tipo**: `refactor`  
**Estimativa**: 1–2h  
**Bloqueia**: TASK-12, TASK-13, TASK-14, TASK-15, TASK-16  
**Bloqueada por**: TASK-06  
**Status**: PENDENTE

**O que fazer:**
- [ ] Substituir variáveis CSS em `:root` pela paleta VELN dark
- [ ] Adicionar `@import` do Google Fonts (Cormorant Garamond + Inter) com `display=swap`
- [ ] Definir `font-family` base: Inter no `body`, Cormorant Garamond nos headings
- [ ] Reescrever estilos base: `body`, `a`, `::selection`
- [ ] Adicionar override de autofill Chrome
- [ ] Remover variáveis antigas

**Arquivos afetados:**
- `frontend/css/styles.css`

**Critério de done:**
- [ ] Fundo preto e texto off-white em qualquer página
- [ ] Google Fonts carregando
- [ ] Nenhum valor de cor hardcoded fora do `:root`

---

## TASK-12 — Navbar VELN e substituição de "ModaShop"

**Tipo**: `feat`  
**Estimativa**: 1h  
**Bloqueia**: TASK-13, TASK-14, TASK-15, TASK-16  
**Bloqueada por**: TASK-11  
**Status**: PENDENTE

**O que fazer:**
- [ ] Reescrever estilos da `.navbar` no tema dark
- [ ] Logo "VELN" em Cormorant Garamond com letter-spacing amplo
- [ ] Links com hover underline via `::after` (scaleX)
- [ ] Remover emojis — substituir por texto ou SVG
- [ ] Substituir `ModaShop` por `VELN` em todos os 6 HTMLs

**Arquivos afetados:**
- `frontend/css/styles.css`
- `frontend/index.html` e todos os `pages/*.html`

**Critério de done:**
- [ ] Navbar coerente com tema dark nas 6 páginas
- [ ] Zero rastros de "ModaShop"
- [ ] Nenhum emoji funcional visível

---

## TASK-13 — Home: hero e seção de valores

**Tipo**: `feat`  
**Estimativa**: 1–2h  
**Bloqueia**: TASK-16  
**Bloqueada por**: TASK-11, TASK-12  
**Status**: PENDENTE

**O que fazer:**
- [ ] Hero: fundo `#000`, headline Cormorant Garamond `clamp(2.5rem, 6vw, 5rem)` peso 300
- [ ] CTA com hover de inversão (fundo branco, texto preto)
- [ ] Substituir `.features` com emojis por linha de texto separada por `·`
- [ ] Extrair estilos inline do `index.html` para `styles.css`

**Arquivos afetados:**
- `frontend/index.html`, `frontend/css/styles.css`

**Critério de done:**
- [ ] Home com fundo preto puro e headline serifada
- [ ] Nenhum `<style>` inline no `index.html`
- [ ] CTA com hover de inversão funcionando

---

## TASK-14 — Página de produtos e cards

**Tipo**: `feat`  
**Estimativa**: 1–2h  
**Bloqueia**: TASK-16  
**Bloqueada por**: TASK-11, TASK-12  
**Status**: PENDENTE

**O que fazer:**
- [ ] Cards: `aspect-ratio: 3/4`, fundo `var(--surface-2)`, sem box-shadow
- [ ] Nome em Cormorant Garamond, categoria em uppercase muted, preço em Inter
- [ ] Hover: botão aparece com `opacity 0→1` + `translateY`
- [ ] Filtros e paginação no tema dark

**Arquivos afetados:**
- `frontend/css/styles.css`, `frontend/pages/products.html`

**Critério de done:**
- [ ] Cards 3/4 com fundo escuro
- [ ] Hover suave sem elevar o card
- [ ] Filtros no tema dark

---

## TASK-15 — Formulários de login e cadastro

**Tipo**: `feat`  
**Estimativa**: 1h  
**Bloqueia**: TASK-16  
**Bloqueada por**: TASK-11, TASK-12  
**Status**: PENDENTE

**O que fazer:**
- [ ] Form: fundo `var(--surface-1)`, border `1px solid var(--border)`
- [ ] Inputs dark com focus `border-color: var(--accent)`
- [ ] Botão primário: fundo branco, texto preto
- [ ] Autofill Chrome não quebra o tema

**Arquivos afetados:**
- `frontend/css/styles.css`, `frontend/pages/login.html`, `frontend/pages/register.html`

**Critério de done:**
- [ ] Formulário dark com inputs corretos
- [ ] Autofill sem fundo amarelo
- [ ] Botão primário branco com texto preto

---

## TASK-16 — Carrinho e pedidos

**Tipo**: `feat`  
**Estimativa**: 1h  
**Bloqueia**: TASK-17  
**Bloqueada por**: TASK-11, TASK-12  
**Status**: PENDENTE

**O que fazer:**
- [ ] Cart items separados por `border-bottom`, sem card com fundo
- [ ] Summary com fundo `var(--surface-2)`
- [ ] Botão "Finalizar Compra": fundo branco, texto preto
- [ ] Badges de status dark (pending/paid/shipped/delivered/cancelled)
- [ ] Order cards com fundo `var(--surface-1)`

**Arquivos afetados:**
- `frontend/css/styles.css`, `frontend/pages/cart.html`, `frontend/pages/orders.html`

**Critério de done:**
- [ ] Carrinho com separadores sutis e checkout branco
- [ ] Badges legíveis no tema dark
- [ ] Visual coerente com o restante

---

## FASE 4 — Docker e entrega final

---

## TASK-17 — QA visual VELN + Docker end-to-end + README

**Tipo**: `test`  
**Estimativa**: 2–3h  
**Bloqueia**: —  
**Bloqueada por**: TASK-13, TASK-14, TASK-15, TASK-16, TASK-10  
**Status**: PENDENTE

**O que fazer:**
- [ ] QA visual nas 6 páginas no Chrome e Firefox
- [ ] Contraste WCAG AA (≥ 4.5:1) nos textos principais
- [ ] `grep` zero hits de hardcoded colors fora do `:root`
- [ ] `grep` zero hits de "ModaShop"
- [ ] `docker-compose up --build` sobe todos os 4 serviços sem erro
- [ ] Fluxo completo testado no ambiente Docker
- [ ] Networking: frontend em `:3000` chama API em `:8080` sem erro
- [ ] README com instruções claras de execução e usuários de seed
- [ ] `.env` fora do git confirmado

**Arquivos afetados:**
- `README.md` (criar/atualizar)

**Critério de done:**
- [ ] `docker-compose up --build` sobe tudo sem erros
- [ ] Fluxo completo no Docker
- [ ] Contraste WCAG AA confirmado
- [ ] Zero "ModaShop" e zero hardcoded colors
- [ ] README claro
- [ ] `.env` fora do git
