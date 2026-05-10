# PRD: Roadmap de Execução — Do Scaffold ao Portfólio

**Status**: Em execução — Fases 1 e 2 concluídas  
**Data**: 2026-05-10  
**Última atualização**: 2026-05-10  
**Autor**: Desenvolvedor

---

## 1. Contexto e Problema

O projeto e-commerce VELN possui um scaffold completo: backend Java + Spring Boot com todas as camadas implementadas (controller, service, repository, model, dto, exception, security, config), frontend HTML/CSS/JS com 6 páginas, 2 migrations Flyway e Docker Compose configurado.

O problema é que **nada foi executado**. O código existe mas nunca foi compilado, nunca rodou, e o fluxo de ponta a ponta (registro → login → produto → carrinho → pedido) nunca foi validado manualmente. Não há garantia de que as partes se conectam corretamente.

O projeto é o principal portfólio para estágio em Engenharia de Software. Um portfólio que não funciona não demonstra competência — demonstra planejamento sem execução.

---

## 2. Objetivos

- **Objetivo principal**: Transformar o scaffold em um projeto funcional, testado e visualmente consistente com a identidade VELN, pronto para ser apresentado em processos seletivos.
- **Métricas de sucesso**:
  - O projeto sobe com um único comando (`docker-compose up`) sem erros
  - O fluxo completo funciona: registro → login → listagem de produtos → adicionar ao carrinho → finalizar pedido
  - Cobertura de testes nos services críticos (Auth, Order, Product)
  - Todas as 6 telas com identidade visual VELN dark aplicada
  - Swagger UI documenta todos os endpoints com exemplos corretos
- **Fora de escopo**:
  - Pagamento real (gateway)
  - Refresh token
  - Upload de imagens (apenas URL)
  - Cache com Redis
  - CI/CD pipeline
  - Responsividade mobile-first completa
  - Dark/light mode toggle

---

## 3. Usuários e Personas

| Persona | Necessidade | Como esse roadmap ajuda |
|---------|-------------|------------------------|
| Desenvolvedor (você) | Ter um projeto funcional e apresentável | As 4 fases garantem que o projeto vai do código não testado até deploy local funcionando |
| Recrutador técnico | Avaliar competência real em Java + Spring Boot | Um sistema rodando com testes e documentação demonstra maturidade técnica |
| Recrutador de produto | Avaliar visão além do código | Identidade VELN com design intencional mostra cuidado com UX |

---

## 4. Requisitos Funcionais

### RF-01: Fase 1 — Primeira execução e validação manual
**Como** desenvolvedor, **quero** rodar o projeto localmente pela primeira vez, **para que** eu saiba exatamente o que funciona e o que precisa de correção.

**Critérios de aceite:**
- [x] PostgreSQL disponível localmente (instalação nativa, sem Docker)
- [x] Arquivo `.env` criado a partir do `.env.example` com credenciais locais
- [x] Backend compila sem erros: `mvn clean compile`
- [x] Backend sobe sem erros de startup: `mvn spring-boot:run`
- [x] Migrations Flyway executam corretamente (tabelas e seed criados no banco)
- [x] `GET /products` retorna os 5 produtos do seed via Swagger UI
- [x] `POST /auth/register` retorna JWT válido
- [x] `POST /auth/login` retorna JWT com usuário do seed
- [x] `POST /orders` com token válido cria pedido e debita estoque
- [x] Frontend abre no browser e consome a API (produtos listados na tela de produtos)

### RF-02: Fase 2 — Testes e qualidade (Sprint 02 real)
**Como** desenvolvedor, **quero** cobertura de testes nas regras de negócio críticas, **para que** o código seja confiável e eu consiga explicar a estratégia de testes em entrevista.

**Critérios de aceite:**
- [x] `AuthServiceTest`: registro com email duplicado retorna 409, login com senha errada retorna 401, token gerado é válido
- [x] `ProductServiceTest`: listagem com filtros, criação valida campos obrigatórios, produto inativo não aparece na listagem pública
- [x] `OrderServiceTest`: criação valida estoque antes de confirmar, `unit_price` salvo é o preço do momento da compra, usuário não vê pedidos de outros usuários
- [x] Teste de integração: fluxo register → login → endpoint protegido funciona com token real
- [x] `mvn test` passa sem erros com banco H2 in-memory
- [x] Cobertura mínima nas 3 services críticas: Auth, Order, Product

### RF-03: Fase 3 — Redesign visual VELN
**Como** desenvolvedor, **quero** identidade visual dark e minimalista em todas as telas, **para que** o portfólio demonstre cuidado com UX além do código backend.

**Critérios de aceite:**
- [ ] TASK-01: Sistema de design CSS (variáveis, tipografia Cormorant + Inter, autofill override)
- [ ] TASK-02: Navbar VELN em todas as 6 páginas, sem rastro de "ModaShop", sem emojis funcionais
- [ ] TASK-03: Hero da home com tipografia editorial e CTA com hover de inversão
- [ ] TASK-04: Cards de produto com proporção 3/4 e hover suave
- [ ] TASK-05: Formulários de login e cadastro no tema dark
- [ ] TASK-06: Carrinho e pedidos com badges de status reestilizados
- [ ] TASK-07: QA visual — contraste WCAG AA validado, zero hardcoded colors fora do `:root`

### RF-04: Fase 4 — Docker e validação final
**Como** desenvolvedor, **quero** o projeto rodando com um único comando, **para que** qualquer recrutador consiga subir e testar localmente.

**Critérios de aceite:**
- [ ] `docker-compose up --build` sobe todos os 4 serviços sem erros (postgres, adminer, backend, frontend)
- [ ] Frontend em `http://localhost:3000` consome a API em `http://localhost:8080` corretamente
- [ ] Swagger UI disponível em `http://localhost:8080/swagger-ui.html`
- [ ] Adminer disponível em `http://localhost:8081`
- [ ] Fluxo completo testado no ambiente Docker (não apenas local Maven)
- [ ] README atualizado com instruções de execução claras

---

## 5. Requisitos Não-Funcionais

| Categoria | Requisito |
|-----------|-----------|
| Segurança | Senhas armazenadas com BCrypt, JWT expirado em 24h, `.env` nunca commitado |
| Acessibilidade | Contraste WCAG AA (4.5:1) nos textos principais do frontend VELN |
| Performance | Google Fonts carregado com `display=swap`, sem bloquear renderização |
| Testabilidade | Testes rodam com H2 in-memory, sem precisar de PostgreSQL externo |
| Manutenibilidade | Todo o tema VELN controlado por variáveis CSS no `:root` |
| Reprodutibilidade | Qualquer máquina consegue rodar com `docker-compose up --build` |

---

## 6. Design Técnico (alto nível)

### Ordem de execução das fases

```
Fase 1 (funcionar) → Fase 2 (testes) → Fase 3 (redesign) → Fase 4 (Docker final)
```

Fase 1 bloqueia tudo. Sem o projeto rodando, não faz sentido escrever testes nem aplicar redesign.

### Endpoints a validar na Fase 1

```
POST /auth/register     → retorna { token, userId, role }
POST /auth/login        → retorna { token, userId, role }
GET  /products          → retorna Page<ProductResponse> com dados do seed
GET  /categories        → retorna List<CategoryResponse>
POST /orders            → cria pedido, valida estoque
GET  /orders/me         → lista pedidos do usuário autenticado
GET  /users/me          → retorna perfil do usuário autenticado
```

### Mudanças no banco de dados

Nenhuma prevista. As migrations V1 e V2 já cobrem o schema completo e os dados de seed.

### Impacto em outros módulos

- Fase 1 pode revelar bugs nos services ou na configuração do Spring Security — correções afetam o backend
- Fase 3 afeta exclusivamente os arquivos CSS e HTML do frontend — sem impacto no backend
- Fase 4 pode exigir ajustes no `nginx.conf` se o proxy reverso não estiver encaminhando corretamente para `http://backend:8080`

---

## 7. Riscos e Dependências

| Risco | Probabilidade | Impacto | Mitigação |
|-------|--------------|---------|-----------|
| Backend não compila por imports ou beans incorretos no scaffold | Alta | Alto | Rodar `mvn clean compile` antes de qualquer outra coisa e corrigir erro por erro |
| Spring Security bloqueando endpoints que deveriam ser públicos | Média | Alto | Validar `SecurityConfig` contra a lista de endpoints públicos do CLAUDE.md |
| Flyway falhando por conflito entre migrations e schema existente | Média | Alto | Garantir banco limpo antes do primeiro run; `spring.flyway.clean-on-validation-error=true` em dev |
| Testes falhando por configuração incorreta do H2 | Média | Médio | Verificar `application-test.yml`: `spring.jpa.hibernate.ddl-auto=create-drop`, Flyway disabled |
| Autofill do Chrome quebrando tema escuro nos formulários | Alta | Médio | Override com `:-webkit-autofill` via `box-shadow: inset 0 0 0 1000px #0a0a0a` |
| Docker Compose com networking incorreto entre frontend e backend | Média | Alto | Testar `curl http://backend:8080/products` de dentro do container frontend |

---

## 8. Plano de Testes

- [x] **Fase 1 — Manual via Swagger**: todos os endpoints críticos testados com request/response corretos
- [x] **Fase 1 — Manual via browser**: fluxo completo do frontend com API rodando
- [x] **Fase 2 — Unitários (JUnit 5 + Mockito)**: AuthService, ProductService, OrderService
- [x] **Fase 2 — Integração**: register → login → endpoint protegido com token real
- [ ] **Fase 3 — Visual manual**: cada uma das 6 páginas abertas no Chrome e Firefox
- [ ] **Fase 3 — Acessibilidade**: DevTools > Accessibility > Color Contrast em textos principais
- [ ] **Fase 4 — Docker end-to-end**: fluxo completo no ambiente Docker sem Maven local

---

## 9. Critérios de Done (DoD)

- [x] `mvn test` passa sem erros
- [ ] `docker-compose up --build` sobe todos os serviços sem erros
- [ ] Fluxo completo funcional: registro → login → produtos → carrinho → pedido
- [ ] Todas as 6 telas com identidade visual VELN dark aplicada
- [ ] Zero rastros de "ModaShop" no frontend
- [ ] Contraste WCAG AA confirmado nos textos principais
- [ ] Swagger UI documenta todos os endpoints
- [ ] `.env` nunca commitado (`.gitignore` configurado)
- [ ] README com instruções de execução claras
