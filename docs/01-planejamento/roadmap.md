# Roadmap

> [[index|← Voltar ao índice]]

---

## Fase 1 — MVP ✅

**Objetivo**: API funcionando com autenticação, produtos, categorias e pedidos.

- [x] Scaffold do projeto (Docker, Spring Boot, estrutura de pacotes)
- [x] Migrations Flyway (tabelas + seed data)
- [x] Autenticação JWT (register + login)
- [x] CRUD de categorias (admin)
- [x] CRUD de produtos com filtros e paginação (admin/público)
- [x] Criar pedido com validação de estoque
- [x] Listagem de pedidos do usuário
- [x] Perfil e endereço do usuário
- [x] Testes unitários completos (services críticos: Auth, Product, Order)
- [x] Teste de integração básico (controller layer — AuthIntegrationTest)
- [x] Validação de transição de status do pedido (ex: não pode ir de DELIVERED para PENDING)

## Fase 2 — Qualidade e robustez ✅

- [x] Cobertura de testes > 80% nos services
- [x] Testes de integração com `@SpringBootTest` + H2 in-memory (78 testes, 0 falhas)
- [x] Rate limiting básico (filtro customizado em `/auth/**` — 20 req/min, retorna 429)
- [x] Logs estruturados (Logback + JSON no profile `prod`, texto legível no `dev`)
- [x] Health check endpoint `/actuator/health`
- [x] Paginação no frontend (produtos)

## Fase 3 — Redesign visual VELN ✅

- [x] Sistema de design CSS global (paleta dark, Cormorant Garamond + Inter, variáveis no `:root`)
- [x] Navbar VELN em todas as 6 páginas — zero "ModaShop", zero emojis
- [x] Hero da home — headline serifada, CTA com hover de inversão
- [x] Cards de produto — proporção 3/4, hover suave, filtros e paginação dark
- [x] Formulários dark — inputs escuros, autofill Chrome correto
- [x] Carrinho e pedidos — separadores sutis, summary sticky, badges com borda
- [x] Zero cores hardcoded fora do `:root`
- [ ] QA visual manual no Chrome/Firefox (pendente)
- [ ] Contraste WCAG AA verificado (pendente)

## Fase 4 — Docker e entrega final

- [x] Docker Compose configurado (postgres, adminer, backend, frontend/nginx)
- [x] README atualizado com seed users e instrução de `.env`
- [ ] `docker-compose up --build` validado (requer Docker Desktop)
- [ ] Fluxo completo testado no ambiente Docker

## Fase 5 — Features avançadas (futuro)

- [ ] Refresh token (evitar re-login a cada 24h)
- [ ] Upload de imagens (S3 ou MinIO)
- [ ] Cache de listagem de produtos (Redis ou Spring Cache)
- [ ] CI/CD com GitHub Actions (build + test + deploy)
- [ ] Deploy em cloud (Railway, Render, ou AWS Free Tier)
- [ ] Monitoramento básico (Actuator + Prometheus + Grafana)

## Dívidas técnicas conhecidas

| Item | Impacto | Esforço | Status |
|------|---------|---------|--------|
| ~~Validação de transição de status de pedido~~ | Médio | Baixo | ✅ Resolvido na Sprint 02 |
| ~~Falta de testes nos controllers~~ | Alto | Médio | ✅ Resolvido na Fase 2 (integração com H2) |
| ~~`image_url` é string livre — sem validação de URL~~ | Baixo | Baixo | ✅ Resolvido na Sprint 02 |
| ~~Frontend sem tratamento de erro consistente~~ | Médio | Médio | ✅ Resolvido na Sprint 03 |
| Docker Desktop não instalado — testes end-to-end pendentes | Médio | Baixo | Pendente |

## Relacionado

- [[sprint-01]] — primeira sprint detalhada
- [[sprint-02]] — próxima sprint (testes + qualidade)
- [[decisoes-tecnicas]] — por que algumas features foram deixadas para depois
