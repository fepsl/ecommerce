# Variáveis de Ambiente

> [[index|← Voltar ao índice]]

---

## Referência completa (.env)

| Variável | Exemplo | Obrigatória | Descrição |
|----------|---------|-------------|-----------|
| `POSTGRES_DB` | `ecommerce` | ✅ | Nome do banco PostgreSQL |
| `POSTGRES_USER` | `ecommerce_user` | ✅ | Usuário do banco |
| `POSTGRES_PASSWORD` | `senha_forte_aqui` | ✅ | Senha do banco |
| `JWT_SECRET` | `minha-chave-com-32-chars-minimo!` | ✅ | Chave de assinatura do JWT (min. 32 chars) |
| `SPRING_PROFILES_ACTIVE` | `dev` ou `prod` | ❌ | Profile ativo (padrão: `dev`) |
| `FRONTEND_URL` | `http://localhost:3000` | ❌ | URL do frontend para CORS |

### .env.example (commitar este, não o .env)

```env
POSTGRES_DB=ecommerce
POSTGRES_USER=ecommerce_user
POSTGRES_PASSWORD=troque_aqui
JWT_SECRET=troque-por-chave-aleatoria-com-32-chars-min
SPRING_PROFILES_ACTIVE=dev
FRONTEND_URL=http://localhost:3000
```

---

## Regras de segurança

- O arquivo `.env` **nunca é commitado** (está no `.gitignore`)
- O arquivo `.env.example` é commitado com valores de placeholder
- `JWT_SECRET` deve ter no mínimo 32 caracteres (256 bits para HS256)
- Em produção, usar um secret manager (ex: AWS Secrets Manager, Railway Secrets, Render Environment)

### Gerar um JWT_SECRET seguro

```bash
# Linux/macOS
openssl rand -base64 32

# Windows PowerShell
[System.Web.Security.Membership]::GeneratePassword(40, 5)

# Ou online: https://generate-secret.vercel.app/32
```

---

## Como o Spring Boot lê as variáveis

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}

jwt:
  secret: ${JWT_SECRET}
```

O Docker Compose injeta as variáveis do `.env` no container automaticamente via `env_file: .env`.

Fora do Docker, as variáveis precisam estar no ambiente do shell ou declaradas em `application-dev.yml` (nunca commitar valores reais).

---

## Relacionado

- [[docker]] — como o Docker Compose usa o .env
- [[autenticacao-jwt]] — por que JWT_SECRET é crítico
- [[spring-boot]] — como os profiles usam as variáveis
