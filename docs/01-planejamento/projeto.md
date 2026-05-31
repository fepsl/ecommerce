# Projeto VELN — Visão Geral e Roadmap

> [[index|← Voltar ao índice]]

---

## O que é

E-commerce de roupas desenvolvido como projeto de portfólio para demonstrar domínio real de:
- Backend com Java + Spring Boot
- Banco de dados relacional + migrations versionadas
- Autenticação segura com JWT
- Arquitetura limpa e boas práticas de engenharia

**Objetivo**: conseguir estágio em Engenharia de Software com um projeto que demonstre competência técnica real — não apenas conhecimento teórico.

---

## Stack

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 17, Spring Boot 3.2.5, Spring Security 6 |
| ORM | Spring Data JPA + Hibernate 6 |
| Banco | PostgreSQL 17, Flyway |
| Auth | JWT (jjwt 0.11.5) + BCrypt |
| Testes | JUnit 5, Mockito, H2 in-memory |
| Frontend | HTML5, CSS3, JavaScript ES Modules, Fetch API |
| Infra | Docker, Docker Compose, Nginx, Railway |
| Docs | SpringDoc OpenAPI / Swagger UI |

---

## Links de produção

| Serviço | URL |
|---------|-----|
| Frontend | https://incredible-embrace-production-264a.up.railway.app |
| API REST | https://ecommerce-production-4adf.up.railway.app |
| Swagger UI | https://ecommerce-production-4adf.up.railway.app/swagger-ui.html |
| Repositório | https://github.com/fepsl/ecommerce |

## Acessos locais

| Serviço | URL |
|---------|-----|
| API REST | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Frontend | http://localhost:3000 |
| Adminer | http://localhost:8081 |

## Usuários do seed

| Email | Senha | Role |
|-------|-------|------|
| admin@ecommerce.com | senha123 | ADMIN |
| cliente@ecommerce.com | senha123 | USER |

---

## Roadmap de fases

### Fase 1 — MVP ✅
- [x] Scaffold (Docker, Spring Boot, estrutura de pacotes)
- [x] Migrations Flyway (tabelas + seed data)
- [x] Autenticação JWT (register + login)
- [x] CRUD de categorias e produtos com filtros e paginação
- [x] Criar pedido com validação de estoque
- [x] Listagem de pedidos, perfil e endereço do usuário
- [x] Testes unitários completos (Auth, Product, Order services)

### Fase 2 — Qualidade e robustez ✅
- [x] 78 testes passando (unitários + integração com H2, 0 falhas)
- [x] Rate limiting em `/auth/**` (20 req/min, retorna 429)
- [x] Logs estruturados (Logback + JSON no profile `prod`)
- [x] Health check `/actuator/health`
- [x] Paginação no frontend

### Fase 3 — Redesign visual VELN ✅
- [x] Design system CSS (paleta dark, Cormorant Garamond + Inter, variáveis no `:root`)
- [x] Navbar VELN em todas as 6 páginas
- [x] Hero, cards de produto, formulários dark, carrinho e pedidos
- [x] QA visual — contraste WCAG AA verificado

### Fase 4 — Docker e deploy ✅
- [x] Docker Compose (postgres, adminer, backend, frontend/nginx)
- [x] Deploy no Railway (backend + frontend + PostgreSQL)
- [x] CI/CD com GitHub Actions (78 testes a cada push)
- [x] README com links de demo

### Fase 5 — Features avançadas (futuro)
- [ ] Refresh token
- [ ] Upload de imagens (S3 ou MinIO)
- [ ] Cache de produtos (Redis ou Spring Cache)
- [ ] Pagamento real (gateway)

---

## Por que o projeto já está bom

Cobre o que a maioria dos candidatos a estágio não entrega:

- **Arquitetura real** — camadas separadas, DTOs, injeção por construtor, `@ControllerAdvice`
- **Segurança aplicada** — JWT stateless, BCrypt, roles USER/ADMIN, rate limiting customizado
- **Qualidade** — 78 testes (unitários + integração), Flyway com migrations versionadas
- **Infraestrutura** — Docker Compose com 4 containers, health checks, nginx, deploy em cloud
- **CI/CD** — GitHub Actions rodando testes a cada push
- **Frontend desacoplado** — design system próprio, skeleton loading, responsivo

---

## Relacionado

- [[arquitetura]] — estrutura de pacotes e princípios
- [[decisoes-tecnicas]] — justificativas para entrevista
- [[api-endpoints]] — todos os endpoints documentados
