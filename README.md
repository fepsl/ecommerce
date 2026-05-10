# 👕 E-commerce de Roupas

Projeto de portfólio desenvolvido para demonstrar domínio real de backend com Java + Spring Boot,
banco de dados relacional, autenticação JWT e frontend vanilla.

---

## Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 17, Spring Boot 3, Spring Security, JPA/Hibernate |
| Banco | PostgreSQL 15, Flyway |
| Auth | JWT (jjwt 0.11.5), BCrypt |
| Docs | SpringDoc OpenAPI 3 / Swagger UI |
| Frontend | HTML5, CSS3, JavaScript puro (Fetch API) |
| Infra | Docker, Docker Compose |
| Testes | JUnit 5, Mockito, H2 in-memory |

---

## Como rodar

### Pré-requisitos
- Docker e Docker Compose instalados

### 1. Clonar e configurar variáveis de ambiente

```bash
git clone <url-do-repositorio>
cd ecommerce
cp .env.example .env
# Edite o .env e troque os valores padrão, especialmente o JWT_SECRET
```

### 2. Subir os containers

```bash
docker-compose up --build
```

### 3. Acessar

| Serviço | URL |
|---------|-----|
| API REST | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Frontend | http://localhost:3000 |
| Adminer (banco) | http://localhost:8081 |

---

## Estrutura do projeto

```
ecommerce/
├── backend/                  → API REST (Spring Boot)
│   ├── src/main/java/com/ecommerce/
│   │   ├── controller/       → endpoints REST
│   │   ├── service/          → regras de negócio
│   │   ├── repository/       → acesso ao banco (JPA)
│   │   ├── model/            → entidades JPA
│   │   ├── dto/              → request/response DTOs
│   │   ├── exception/        → exceções + handler global
│   │   ├── security/         → JWT filter + Spring Security
│   │   └── config/           → Swagger, CORS
│   └── src/main/resources/
│       ├── application.yml
│       └── db/migration/     → Flyway SQL
└── frontend/                 → HTML/CSS/JS puro
    ├── pages/
    ├── css/
    └── js/
```

---

## Endpoints da API

### Autenticação (pública)
```
POST /auth/register   → cadastro, retorna JWT
POST /auth/login      → login, retorna JWT
```

### Produtos
```
GET    /products              → listagem paginada + filtros
GET    /products/{id}         → detalhe
POST   /products              → criar [ADMIN]
PUT    /products/{id}         → atualizar [ADMIN]
DELETE /products/{id}         → desativar [ADMIN]
```

### Categorias
```
GET    /categories            → listar todas
POST   /categories            → criar [ADMIN]
PUT    /categories/{id}       → atualizar [ADMIN]
DELETE /categories/{id}       → remover [ADMIN]
```

### Pedidos
```
POST   /orders                → criar pedido [USER]
GET    /orders/me             → meus pedidos [USER]
GET    /orders/{id}           → detalhe [dono ou ADMIN]
PUT    /orders/{id}/status    → atualizar status [ADMIN]
```

### Usuário
```
GET    /users/me              → perfil [autenticado]
PUT    /users/me/address      → atualizar endereço [autenticado]
```

---

## Regras de negócio

- Estoque validado no momento da criação do pedido
- `unit_price` em `order_items` é o preço histórico — não muda se o produto for atualizado
- Produtos com `active = false` não aparecem na listagem pública
- Fluxo de status: `PENDING → PAID → SHIPPED → DELIVERED` (ou `CANCELLED` a qualquer momento)
- Email único — retorna `409 Conflict` se já existir

---

## Decisões técnicas

- **DTOs em vez de entidades na API**: evita expor campos sensíveis (como `password`), desacopla contrato da API do banco
- **Injeção por construtor**: campo `final` garante imutabilidade + facilita testes com mocks
- **JWT stateless**: sem sessão no servidor, escala horizontalmente. Trade-off: não dá para invalidar token antes de expirar
- **Flyway para migrations**: banco e código sempre sincronizados em qualquer ambiente
- **LAZY loading**: evita carregar dados desnecessários, N+1 mitigado com `JOIN FETCH`

---

## Rodando testes

```bash
cd backend
./mvnw test
```

Os testes usam banco H2 in-memory — nenhuma dependência externa necessária.

---

## Melhorias futuras

- [ ] Refresh token
- [ ] Upload de imagens (atualmente apenas URL)
- [ ] Cache com Redis
- [ ] Pagamento real (gateway)
- [ ] Rate limiting
- [ ] CI/CD pipeline
