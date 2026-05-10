# Roadmap

> [[index|← Voltar ao índice]]

---

## Fase 1 — MVP (em andamento)

**Objetivo**: API funcionando com autenticação, produtos, categorias e pedidos.

- [x] Scaffold do projeto (Docker, Spring Boot, estrutura de pacotes)
- [x] Migrations Flyway (tabelas + seed data)
- [x] Autenticação JWT (register + login)
- [x] CRUD de categorias (admin)
- [x] CRUD de produtos com filtros e paginação (admin/público)
- [x] Criar pedido com validação de estoque
- [x] Listagem de pedidos do usuário
- [x] Perfil e endereço do usuário
- [ ] Testes unitários completos (services)
- [ ] Teste de integração básico (controller layer)
- [ ] Validação de transição de status do pedido (ex: não pode ir de DELIVERED para PENDING)

## Fase 2 — Qualidade e robustez

- [ ] Cobertura de testes > 80% nos services
- [ ] Testes de integração com `@SpringBootTest` + TestContainers (PostgreSQL real)
- [ ] Rate limiting básico (Spring Security ou filtro customizado)
- [ ] Logs estruturados (Logback + JSON)
- [ ] Health check endpoint `/actuator/health`
- [ ] Paginação no frontend (produtos)

## Fase 3 — Features avançadas

- [ ] Refresh token (evitar re-login a cada 24h)
- [ ] Upload de imagens (S3 ou MinIO)
- [ ] Cache de listagem de produtos (Redis ou Spring Cache)
- [ ] Histórico de preços de produto
- [ ] Busca full-text nos produtos (PostgreSQL `tsvector` ou Elasticsearch)

## Fase 4 — Infraestrutura

- [ ] CI/CD com GitHub Actions (build + test + deploy)
- [ ] Deploy em cloud (Railway, Render, ou AWS Free Tier)
- [ ] Variáveis de ambiente em produção via secret manager
- [ ] Monitoramento básico (Actuator + Prometheus + Grafana)

## Dívidas técnicas conhecidas

| Item | Impacto | Esforço |
|------|---------|---------|
| Validação de transição de status de pedido | Médio | Baixo |
| Falta de testes nos controllers | Alto | Médio |
| `image_url` é string livre — sem validação de URL | Baixo | Baixo |
| Frontend sem tratamento de erro consistente | Médio | Médio |

## Relacionado

- [[sprint-01]] — primeira sprint detalhada
- [[sprint-02]] — próxima sprint (testes + qualidade)
- [[decisoes-tecnicas]] — por que algumas features foram deixadas para depois
