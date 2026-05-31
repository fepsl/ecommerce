# VELN — Base de Conhecimento

> Vault Obsidian do projeto de portfólio e-commerce de roupas.  
> Abra esta pasta como vault no Obsidian para navegação com links internos.

---

## Estado do projeto

| Campo | Valor |
|-------|-------|
| Status | 🟢 No ar — deploy feito em 30/05/2026 |
| Fases concluídas | 1 (MVP) · 2 (Qualidade) · 3 (Redesign VELN) · 4 (Deploy + CI/CD) |
| Testes | 78 passando (unitários + integração) |
| Stack | Java 17 + Spring Boot 3 + PostgreSQL + HTML/CSS/JS |
| Objetivo | Portfólio para estágio em Engenharia de Software |

## Links de produção

| Serviço | URL |
|---------|-----|
| Frontend | https://incredible-embrace-production-264a.up.railway.app |
| Swagger UI | https://ecommerce-production-4adf.up.railway.app/swagger-ui.html |
| Repositório | https://github.com/fepsl/ecommerce |

---

## Mapa de navegação

### 01 · Planejamento
| Nota | O que cobre |
|------|------------|
| [[projeto]] | Visão geral, stack, roadmap de fases, links de produção |

### 02 · Backend
| Nota | O que cobre |
|------|------------|
| [[arquitetura]] | Estrutura de pacotes, fluxo de requisição, JWT, princípios |
| [[api-endpoints]] | Todos os endpoints com exemplos de request/response |
| [[regras-de-negocio]] | Validações por domínio, exceções, fluxo de status de pedido |
| [[spring-boot]] | Configuração, profiles, Bean Validation, Swagger |
| [[testes]] | Estratégia de testes, JUnit 5, Mockito, H2 |

### 03 · Frontend
| Nota | O que cobre |
|------|------------|
| [[frontend]] | Módulos JS, páginas, CSS, integração com a API |

### 04 · Banco de Dados
| Nota | O que cobre |
|------|------------|
| [[banco-de-dados]] | Schema completo, índices, migrations Flyway |

### 05 · DevOps
| Nota | O que cobre |
|------|------------|
| [[infra]] | Docker Compose, Dockerfiles, variáveis de ambiente, deploy Railway |

### 07 · Decisões Técnicas
| Nota | O que cobre |
|------|------------|
| [[decisoes-tecnicas]] | ADRs: DTO, injeção, JWT, Flyway, LAZY, soft delete |

---

## Como usar este vault

- Links `[[nota]]` funcionam no Obsidian como hiperlinks
- Abra: Obsidian → "Open folder as vault" → selecione a pasta `docs/`
- `Ctrl+P` para busca rápida · Graph View para visualizar conexões
