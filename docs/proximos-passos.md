# Próximos Passos — Portfólio

> [[index|← Voltar ao índice]]

---

## Estado atual (29/05/2026)

| Campo | Valor |
|-------|-------|
| Status | 🟢 Pronto para divulgação — falta só deploy |
| Fases concluídas | 1 (MVP) · 2 (Qualidade) · 3 (Redesign VELN) · 4 (Docker + QA) |
| Testes | 78 passando (13 arquivos — service + integração) |
| Páginas | 6 páginas verificadas via QA visual automatizado |
| Repositório remoto | ❌ Ainda não criado |
| Deploy | ❌ Sem link público |

---

## O que falta para o portfólio estar completo

| # | Tarefa | Por que fazer | Impacto | Esforço |
|---|--------|--------------|---------|---------|
| 1 | **Commitar mudanças pendentes** (CSS fixes, V3, orders.html, docs) | Base para tudo — sem commit nada mais avança | 🔴 Obrigatório | ~5 min |
| 2 | **Criar repositório no GitHub + push** | Sem isso o projeto não existe para um recrutador | 🔴 Alto | ~10 min |
| 3 | **Deploy no Railway ou Render** | Link funcionando vale mais que qualquer feature nova | 🔴 Alto | ~30 min |
| 4 | **Atualizar README com screenshots e link do deploy** | Recrutador entende em 30 segundos sem precisar clonar | 🟡 Alto | ~20 min |
| 5 | **CI/CD com GitHub Actions** (build + test) | Mostra maturidade de processo, não só de código | 🟡 Médio | ~1h |
| 6 | **Refresh token** | Feature real, boa de explicar em entrevista | 🟢 Baixo | ~2h |
| 7 | **Upload de imagem** (S3 ou MinIO) | Substitui os placeholders por imagens reais | 🟢 Baixo | ~3h |
| 8 | **Cache de produtos** (Spring Cache ou Redis) | Interessante, mas overkill para a escala do projeto | ⚪ Muito baixo | ~2h |

> Os itens 1–4 são os únicos que mudam o que um recrutador consegue fazer com o projeto hoje.  
> Os itens 5–8 são opcionais e podem ser feitos depois da primeira entrevista.

---

## Por que o projeto já está bom

O projeto cobre o que a maioria dos candidatos a estágio não chega perto de entregar:

- **Arquitetura real** — camadas separadas, DTOs, injeção por construtor, `@ControllerAdvice`
- **Segurança aplicada** — JWT stateless, BCrypt, roles USER/ADMIN, rate limiting customizado
- **Qualidade** — 78 testes (unitários + integração), Flyway com migrations versionadas
- **Infraestrutura** — Docker Compose com 4 containers, health checks, nginx
- **Acessibilidade** — WCAG AA verificado e corrigido no QA de 29/05/2026
- **Frontend desacoplado** — design system próprio, skeleton loading, responsivo

O único gap objetivo é o deploy. Com o link no ar e o README com screenshot, o projeto se sustenta sozinho.

---

## Relacionado

- [[roadmap]] — histórico completo das fases
- [[decisoes-tecnicas]] — justificativas para entrevista
