# Visão Geral do Projeto

> [[index|← Voltar ao índice]]

---

## O que é

E-commerce de roupas desenvolvido como projeto de portfólio para demonstrar domínio real de:
- Backend com Java + Spring Boot
- Banco de dados relacional + migrations versionadas
- Autenticação segura com JWT
- Arquitetura limpa e boas práticas

## Objetivo

Conseguir **estágio em Engenharia de Software** com um projeto que demonstre competência técnica real — não apenas conhecimento teórico.

## Stack completa

### Backend
- Java 17 + Spring Boot 3.2.5
- Spring Security 6 + JWT (jjwt 0.11.5)
- Spring Data JPA + Hibernate 6
- PostgreSQL 15 + Flyway (migrations)
- Bean Validation + Lombok
- SpringDoc OpenAPI / Swagger UI
- Maven

### Testes
- JUnit 5 + Mockito
- H2 in-memory (banco de testes)

### Frontend
- HTML5, CSS3, JavaScript puro (ES Modules)
- Fetch API para consumo da API REST
- localStorage para JWT e carrinho

### Infraestrutura
- Docker + Docker Compose
- Nginx (frontend)
- Adminer (interface web do banco)

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

## Links relacionados

- [[arquitetura]] — estrutura de pacotes e princípios
- [[banco-de-dados]] — schema completo
- [[api-endpoints]] — todos os endpoints documentados
- [[frontend]] — módulos JS e páginas
- [[docker]] — como rodar o projeto localmente
- [[testes]] — estratégia de testes
- [[roadmap]] — o que vem a seguir
