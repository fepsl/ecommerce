# VELN — Base de Conhecimento

> Vault Obsidian do projeto de portfólio e-commerce de roupas.  
> Abra esta pasta como vault no Obsidian para navegação com links internos.

---

## Estado do projeto

| Campo      | Valor                                                                  |
| ---------- | ---------------------------------------------------------------------- |
| Status     | 🟢 Pronto para divulgação — falta deploy                               |
| Fase atual | Fases 1–4 concluídas — QA visual feito em 29/05/2026                   |
| Stack      | Java 17 + Spring Boot 3 + PostgreSQL + HTML/CSS/JS                     |
| Objetivo   | Portfólio para estágio em Engenharia de Software                       |

---

## Mapa de navegação

### 01 · Planejamento
| Nota            | O que cobre                                            |
| --------------- | ------------------------------------------------------ |
| [[visao-geral]] | Stack completa, acessos locais, usuários de seed       |
| [[roadmap]]     | Fases do projeto, features pendentes, dívidas técnicas |

### 02 · Backend
| Nota | O que cobre |
|------|------------|
| [[arquitetura]] | Estrutura de pacotes, fluxo de requisição, princípios |
| [[api-endpoints]] | Todos os endpoints com exemplos de request/response |
| [[autenticacao-jwt]] | Fluxo JWT, classes envolvidas, trade-offs |
| [[regras-de-negocio]] | Validações por domínio, exceções, fluxo de status |
| [[spring-boot]] | Configuração, profiles, variáveis, Swagger |
| [[testes]] | Estratégia de testes, JUnit 5, Mockito, H2 |

### 03 · Frontend
| Nota | O que cobre |
|------|------------|
| [[frontend]] | Módulos JS, páginas, CSS, integração com a API |

### 04 · Banco de Dados
| Nota | O que cobre |
|------|------------|
| [[banco-de-dados]] | Schema completo, índices, migrations Flyway, decisões |

### 05 · DevOps
| Nota | O que cobre |
|------|------------|
| [[docker]] | Docker Compose, serviços, Dockerfiles |
| [[variaveis-ambiente]] | Referência completa de variáveis de ambiente |

### 06 · Sprints
| Nota | O que cobre |
|------|------------|
| [[sprint-01]] | Scaffold completo — ✅ Concluída |
| [[sprint-02]] | Testes e qualidade — ✅ Concluída |
| [[sprint-03-frontend]] | Frontend editorial + página de produto — ✅ Concluída |

### PRDs e Tasks
| Nota | O que cobre |
|------|------------|
| [[prd-roadmap-execucao]] | Roadmap do scaffold ao portfólio — Fases 1 e 2 concluídas |
| [[tasks-roadmap-execucao]] | Tasks de execução das 4 fases (TASK-01 a TASK-17) |
| [[prd-redesign-veln]] | Redesign visual dark VELN — ✅ Concluído |
| [[tasks-redesign-veln]] | Tasks do redesign VELN (TASK-01 a TASK-07) — ✅ Todas concluídas |
| [[prd-sprint-03-frontend]] | Frontend editorial + página de produto — ✅ Concluído |
| [[tasks-sprint-03-frontend]] | Tasks da Sprint 03 (TASK-01 a TASK-08) — ✅ Todas concluídas |
| [[prd-fase-2-qualidade]] | Qualidade e robustez — Actuator, Logs, TestContainers, Rate limit, Paginação |
| [[tasks-fase-2-qualidade]] | Tasks da Fase 2 (TASK-01 a TASK-09) — ✅ Todas concluídas (78 testes) |

### Próximos Passos
| Nota | O que cobre |
|------|------------|
| [[proximos-passos]] | O que falta para o portfólio estar completo — tabela priorizada |

### 07 · Decisões Técnicas
| Nota | O que cobre |
|------|------------|
| [[decisoes-tecnicas]] | ADRs: DTO, injeção, JWT, Flyway, LAZY, soft delete |

### 08 · Documentação
| Nota | O que cobre |
|------|------------|
| [[glossario]] | Termos técnicos e conceitos usados no projeto |

---

## Tecnologias usadas

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 17, Spring Boot 3, Spring Security 6 |
| ORM | Spring Data JPA + Hibernate 6 |
| Banco | PostgreSQL 17, Flyway |
| Auth | JWT (jjwt 0.11.5) + BCrypt |
| Testes | JUnit 5, Mockito, H2 |
| Frontend | HTML5, CSS3, JavaScript ES Modules |
| Infra | Docker, Docker Compose, Nginx |
| Docs | SpringDoc OpenAPI / Swagger UI |

---

## Acessos locais

| Serviço | URL |
|---------|-----|
| API REST | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Frontend | http://localhost:3000 |
| Adminer | http://localhost:8081 |

---

## Como usar este vault

- Links `[[nota]]` funcionam no Obsidian como hiperlinks
- Abra o Obsidian → "Open folder as vault" → selecione a pasta `docs/`
- Use `Ctrl+P` para busca rápida em qualquer nota
- Use o Graph View para visualizar as conexões entre temas
