# PRD: Fase 2 — Qualidade e Robustez

**Status**: Planejado  
**Data**: 2026-05-10  
**Autor**: Desenvolvedor

---

## 1. Contexto e Problema

O MVP da VELN está completo e com testes unitários dos services bem cobertos (51 testes, 0 falhas). O frontend tem identidade de marca e fluxo de e-commerce completo. O que falta para o projeto parecer "produção real" são as camadas invisíveis que separam um portfólio amador de um projeto sério:

- **Testes rodando contra H2**: a `AuthIntegrationTest` usa banco em memória que emula PostgreSQL, mas não é o mesmo — dialetos diferentes, sem suporte completo a constraints e comportamentos específicos.
- **Sem observabilidade**: não há health check, não há logs estruturados. Em produção, isso significa não saber se a aplicação está saudável ou debugar por texto livre.
- **Sem proteção contra abuso**: `/auth/login` pode ser atacado por força bruta sem nenhuma defesa.
- **Paginação sem UI**: o backend já suporta paginação, mas o frontend não exibe controles de navegação entre páginas.

---

## 2. Objetivos

- Elevar a confiabilidade dos testes usando PostgreSQL real via TestContainers
- Adicionar observabilidade básica (health check + logs JSON)
- Proteger endpoints sensíveis com rate limiting
- Fechar a lacuna de UX da paginação no frontend

---

## 3. Requisitos Funcionais

### RF-01: Health check via Actuator

**Critérios de aceite:**
- [ ] `GET /actuator/health` retorna `{"status":"UP"}` quando a aplicação e o banco estão ok
- [ ] Endpoint público (sem autenticação)
- [ ] Detalhes do banco visíveis apenas para ADMIN autenticado

---

### RF-02: Logs estruturados JSON no perfil de produção

**Critérios de aceite:**
- [ ] Perfil `dev`: logs em texto legível no console (comportamento atual mantido)
- [ ] Perfil `prod`: logs em JSON estruturado, uma linha por evento
- [ ] Campos obrigatórios no JSON: `@timestamp`, `level`, `logger`, `message`
- [ ] Campos de contexto adicionados nos pontos críticos: `userId`, `orderId`, `productId` onde aplicável

---

### RF-03: Testes de integração com TestContainers

**Critérios de aceite:**
- [ ] Testes de integração rodam contra PostgreSQL 17 real (Docker via TestContainers)
- [ ] Flyway executa as migrations reais antes de cada suite
- [ ] Classe base `BaseIntegrationTest` reutilizável por todos os controllers
- [ ] Cobertura dos endpoints críticos de cada controller (ver RF-03a–d)

#### RF-03a: ProductControllerIT
- [ ] `GET /products` retorna lista paginada
- [ ] `GET /products?name=X` filtra corretamente
- [ ] `GET /products/{id}` válido → 200; inválido → 404
- [ ] `POST /products` sem auth → 401; com USER → 403; com ADMIN → 201
- [ ] `DELETE /products/{id}` com ADMIN → produto desaparece da listagem pública

#### RF-03b: CategoryControllerIT
- [ ] `GET /categories` → 200
- [ ] `POST /categories` sem auth → 401; com ADMIN → 201
- [ ] `PUT /categories/{id}` → atualiza; id inválido → 404
- [ ] `DELETE /categories/{id}` → remove; id inválido → 404

#### RF-03c: OrderControllerIT
- [ ] `POST /orders` com USER autenticado e estoque disponível → 201
- [ ] `POST /orders` com produto sem estoque → 422
- [ ] `GET /orders/me` → retorna apenas pedidos do usuário logado
- [ ] `GET /orders/{id}` de outro usuário → 403
- [ ] `PUT /orders/{id}/status` transição inválida → 422

#### RF-03d: UserControllerIT
- [ ] `GET /users/me` autenticado → 200 com perfil
- [ ] `PUT /users/me/address` → cria; segunda chamada → atualiza sem duplicar

---

### RF-04: Rate limiting nos endpoints de autenticação

**Critérios de aceite:**
- [ ] Máximo de 20 requisições por minuto por IP em `/auth/login` e `/auth/register`
- [ ] A 21ª requisição retorna `429 Too Many Requests`
- [ ] Header `Retry-After` presente na resposta 429
- [ ] Implementação sem biblioteca externa (filtro customizado em memória)
- [ ] Limite configurável via `application.yml`

---

### RF-05: Paginação com UI no frontend

**Critérios de aceite:**
- [ ] Controles de navegação visíveis abaixo do grid de produtos (Anterior / números / Próximo)
- [ ] Clique em página → skeleton aparece → produtos da página carregam
- [ ] Botão "Anterior" desabilitado na primeira página; "Próximo" desabilitado na última
- [ ] Estilo segue design system VELN (zero cores hardcoded)

---

## 4. Requisitos Não-Funcionais

| Categoria | Requisito |
|-----------|-----------|
| Testes | `mvn test` passa com todos os testes (unitários + integração) |
| Compatibilidade | TestContainers requer Docker instalado e rodando |
| Performance | Testes de integração podem ser mais lentos — aceitável (CI pipeline) |
| Segurança | Rate limit protege contra brute-force; não substitui autenticação robusta |

---

## 5. Design Técnico

### Arquivos novos (backend)
```
src/main/resources/logback-spring.xml
src/main/java/com/ecommerce/security/RateLimitFilter.java
src/main/java/com/ecommerce/config/RateLimitConfig.java
src/test/java/com/ecommerce/integration/BaseIntegrationTest.java
src/test/java/com/ecommerce/integration/ProductControllerIT.java
src/test/java/com/ecommerce/integration/CategoryControllerIT.java
src/test/java/com/ecommerce/integration/OrderControllerIT.java
src/test/java/com/ecommerce/integration/UserControllerIT.java
```

### Arquivos modificados (backend)
```
backend/pom.xml                          → actuator, logstash-logback-encoder, testcontainers
backend/src/main/resources/application.yml       → management.endpoints config, rate-limit config
backend/src/main/java/com/ecommerce/security/SecurityConfig.java  → permitir /actuator/health
```

### Arquivos modificados (frontend)
```
frontend/js/products.js    → renderPagination(page, totalPages)
frontend/css/styles.css    → estilos .pagination
```

### Dependências a adicionar no pom.xml
```xml
<!-- Actuator -->
spring-boot-starter-actuator

<!-- Logs JSON -->
net.logstash.logback:logstash-logback-encoder:7.4

<!-- TestContainers -->
org.testcontainers:testcontainers-bom:1.19.7 (BOM)
org.testcontainers:junit-jupiter
org.testcontainers:postgresql
```

---

## 6. Riscos

| Risco | Probabilidade | Mitigação |
|-------|---------------|-----------|
| Docker não disponível no ambiente de desenvolvimento | Baixa | TestContainers detecta e falha com mensagem clara |
| Testes de integração lentos atrasam feedback local | Média | Separar em perfil Maven separado (`-P integration`) |
| Rate limit por IP quebra em ambiente de proxy reverso | Média | Usar `X-Forwarded-For` se presente; documentar limitação |

---

## 7. Plano de Testes

- [ ] `mvn test` passa completo (unitários + integração)
- [ ] `GET http://localhost:8080/actuator/health` → `{"status":"UP"}`
- [ ] Log em perfil prod exibe JSON válido no console
- [ ] 21ª requisição ao `/auth/login` retorna 429 com `Retry-After`
- [ ] Frontend navega entre páginas de produtos com skeleton visível
