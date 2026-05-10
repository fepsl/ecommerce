# Sprint 01 — Scaffold e MVP

> [[roadmap|← Roadmap]]  
> Data: 2026-05-10  
> Status: ✅ Concluída

---

## Objetivo

Ter toda a estrutura do projeto criada, com a API funcionando localmente via Docker.

## O que foi entregue

### Infraestrutura
- [x] Docker Compose (postgres, adminer, backend, frontend)
- [x] `.env.example` com todas as variáveis necessárias
- [x] `.gitignore` cobrindo Java, Node, Docker e IDE
- [x] Dockerfile multi-stage para o backend (Maven → JRE Alpine)
- [x] Dockerfile + nginx.conf para o frontend

### Backend — Spring Boot
- [x] `pom.xml` com todas as dependências configuradas
- [x] Profiles `dev` e `prod` (application-dev/prod.yml)
- [x] 8 models JPA: User, Category, Product, Order, OrderItem, Address + enums Role, OrderStatus
- [x] 8 DTOs request + 7 DTOs response
- [x] 5 repositories JPA com queries customizadas
- [x] 5 services com toda a regra de negócio
- [x] 5 controllers REST
- [x] 4 exceções customizadas + GlobalExceptionHandler
- [x] JWT completo: JwtUtil, JwtAuthFilter, SecurityConfig
- [x] SpringDoc/Swagger configurado com autenticação Bearer

### Banco de dados
- [x] V1: Criação das tabelas com constraints e índices
- [x] V2: Seed data (2 users, 4 categorias, 5 produtos)

### Frontend
- [x] Estrutura HTML/CSS/JS puro
- [x] Módulo `api.js` (HTTP client com token automático)
- [x] Módulo `auth.js` (login, register, logout, requireAuth)
- [x] Módulo `cart.js` (localStorage, checkout)
- [x] Módulo `products.js` (listagem com filtros, paginação)
- [x] Páginas: index, products, cart, orders, login, register
- [x] CSS responsivo com tema dark + variáveis CSS

### Testes
- [x] `ProductServiceTest` com 4 casos (JUnit 5 + Mockito)
- [x] `application-test.yml` (H2 in-memory)

## Números

| Métrica | Valor |
|---------|-------|
| Arquivos criados | 77 |
| Linhas de código | ~3.500 |
| Commits | 1 |
| Tempo | 1 sessão |

---

## Sprint 02 — próxima

Ver [[roadmap]] — foco em testes e validação de transição de status.
