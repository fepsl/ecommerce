# Docker e Docker Compose

> [[index|← Voltar ao índice]]

---

## Como rodar

```bash
# Clonar o repositório e criar o .env
cp .env.example .env
# editar .env com suas configurações

# Subir todos os serviços
docker-compose up --build

# Rodar em background
docker-compose up -d --build

# Parar
docker-compose down

# Parar e remover volumes (apaga o banco)
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

### docker-compose.yml resumido

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB:       ${POSTGRES_DB}
      POSTGRES_USER:     ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER}"]
      interval: 5s
      timeout: 5s
      retries: 5

  adminer:
    image: adminer
    ports:
      - "8081:8080"
    depends_on:
      - postgres

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    env_file: .env
    depends_on:
      postgres:
        condition: service_healthy

  frontend:
    build: ./frontend
    ports:
      - "3000:80"
    depends_on:
      - backend

volumes:
  postgres_data:
```

O `depends_on` com `condition: service_healthy` garante que o backend só sobe depois que o PostgreSQL estiver pronto para aceitar conexões.

---

## Dockerfile — Backend (multi-stage)

```dockerfile
# Estágio 1: Build com Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline       # cache de dependências em layer separado
COPY src ./src
RUN mvn package -DskipTests

# Estágio 2: Imagem de execução (JRE mínimo)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Por que multi-stage?**  
A imagem final não inclui Maven, código-fonte nem dependências de build — apenas o JRE e o `.jar`. A imagem de produção fica ~200 MB em vez de ~600 MB.

**Por que `dependency:go-offline` em layer separado?**  
O Docker cacheia por layer. Se apenas o código mudar (e não o `pom.xml`), o Maven não baixa as dependências novamente — rebuild muito mais rápido.

---

## Dockerfile — Frontend

```dockerfile
FROM node:20-alpine AS build
# (sem build step — JS puro, sem bundler)

FROM nginx:alpine
COPY . /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
```

### nginx.conf

```nginx
server {
    listen 80;

    location / {
        root   /usr/share/nginx/html;
        index  index.html;
        try_files $uri $uri/ /index.html;   # SPA fallback
    }

    location /api {
        proxy_pass http://backend:8080;     # proxy para a API
    }
}
```

---

## Adminer — acesso ao banco

1. Acesse: http://localhost:8081
2. Sistema: `PostgreSQL`
3. Servidor: `postgres` (nome do serviço Docker)
4. Usuário/senha/banco: valores do `.env`

---

## Relacionado

- [[variaveis-ambiente]] — todas as variáveis do .env
- [[banco-de-dados]] — schema e migrations
- [[spring-boot]] — como o app lê as variáveis de ambiente
