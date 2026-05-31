# Infraestrutura — Docker e Variáveis de Ambiente

> [[index|← Voltar ao índice]]

---

## Como rodar localmente

```bash
# Clonar e configurar variáveis
git clone https://github.com/fepsl/ecommerce.git
cd ecommerce
cp .env.example .env
# Editar .env e trocar JWT_SECRET por uma string de 32+ chars

# Subir todos os serviços
docker-compose up --build

# Rodar em background
docker-compose up -d --build

# Parar (mantém dados)
docker-compose down

# Parar e apagar banco
docker-compose down -v
```

---

## Serviços do Docker Compose

| Serviço | Imagem | Porta | Descrição |
|---------|--------|-------|-----------|
| `postgres` | `postgres:15-alpine` | 5432 | Banco de dados principal |
| `adminer` | `adminer` | 8081 | Interface web para o banco |
| `backend` | Build local | 8080 | API Spring Boot |
| `frontend` | Build local | 3000 | Frontend servido pelo Nginx |

O `depends_on` com `condition: service_healthy` garante que o backend só sobe depois que o PostgreSQL estiver pronto para aceitar conexões.

---

## Dockerfiles

### Backend (multi-stage)

```dockerfile
# Estágio 1: Build com Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline   # layer de cache de dependências
COPY src ./src
RUN mvn package -DskipTests

# Estágio 2: Imagem de execução (JRE mínimo)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Multi-stage mantém a imagem final ~200 MB (sem Maven, código-fonte ou dependências de build).

### Frontend

```dockerfile
FROM nginx:1.25-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY . /usr/share/nginx/html
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
EXPOSE 80
ENTRYPOINT ["/docker-entrypoint.sh"]
```

O `docker-entrypoint.sh` injeta `API_BASE_URL` no `config.js` em runtime — permite trocar a URL do backend sem rebuild da imagem.

---

## Variáveis de ambiente

### Referência completa (.env)

| Variável | Exemplo | Obrigatória | Descrição |
|----------|---------|-------------|-----------|
| `POSTGRES_DB` | `ecommerce` | ✅ | Nome do banco PostgreSQL |
| `POSTGRES_USER` | `ecommerce_user` | ✅ | Usuário do banco |
| `POSTGRES_PASSWORD` | `senha_forte_aqui` | ✅ | Senha do banco |
| `JWT_SECRET` | `string-aleatoria-32-chars-min` | ✅ | Chave de assinatura JWT (mín. 32 chars para HS256) |
| `SPRING_PROFILES_ACTIVE` | `dev` ou `prod` | ❌ | Profile ativo (padrão: `dev`) |
| `API_BASE_URL` | `https://backend.railway.app` | ❌ | URL do backend para o frontend em produção |

### Regras de segurança

- `.env` nunca é commitado (está no `.gitignore`)
- `.env.example` é commitado com valores de placeholder
- `JWT_SECRET` deve ter mínimo 32 caracteres

### Gerar JWT_SECRET seguro

```bash
# Linux/macOS
openssl rand -base64 32

# Windows PowerShell
[System.Web.Security.Membership]::GeneratePassword(40, 5)
```

---

## Deploy em produção (Railway)

O projeto roda no Railway com 3 serviços:

| Serviço | URL |
|---------|-----|
| Backend | https://ecommerce-production-4adf.up.railway.app |
| Frontend | https://incredible-embrace-production-264a.up.railway.app |
| PostgreSQL | Gerenciado pelo Railway (sem URL pública) |

Variáveis do backend no Railway usam referências entre serviços:
```
SPRING_DATASOURCE_URL = jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
SPRING_DATASOURCE_USERNAME = ${{Postgres.PGUSER}}
SPRING_DATASOURCE_PASSWORD = ${{Postgres.PGPASSWORD}}
```

---

## Adminer — acesso ao banco local

1. Acesse: http://localhost:8081
2. Sistema: `PostgreSQL`
3. Servidor: `postgres` (nome do serviço Docker)
4. Usuário/senha/banco: valores do `.env`

---

## Relacionado

- [[banco-de-dados]] — schema e migrations
- [[spring-boot]] — como o app lê as variáveis
- [[arquitetura]] — onde a segurança encaixa
