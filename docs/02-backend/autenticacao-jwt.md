# Autenticação JWT

> [[index|← Voltar ao índice]]

---

## Fluxo completo

```
1. POST /auth/register ou /auth/login
        ↓
2. Spring Security autentica via DaoAuthenticationProvider
   (compara email + BCrypt hash)
        ↓
3. JwtUtil.generateToken() — assina com HS256 + chave secreta
        ↓
4. Retorna { "token": "eyJ..." } para o cliente
        ↓
5. Cliente armazena em localStorage
        ↓
6. Próximas requisições: Header "Authorization: Bearer eyJ..."
        ↓
7. JwtAuthFilter.doFilterInternal():
   - Extrai token do header
   - JwtUtil.extractUsername() → email
   - UserDetailsServiceImpl.loadUserByUsername() → User do banco
   - JwtUtil.isTokenValid() → verifica assinatura + expiração
   - SecurityContextHolder.getContext().setAuthentication(...)
        ↓
8. Spring Security verifica @PreAuthorize / .authorizeHttpRequests()
```

## Configurações

| Parâmetro | Valor |
|-----------|-------|
| Algoritmo | HS256 |
| Expiração | 24 horas (86.400.000 ms) |
| Header | `Authorization: Bearer <token>` |
| Chave | Variável de ambiente `JWT_SECRET` (mín. 32 chars) |

## Roles

| Role | O que pode fazer |
|------|----------------|
| `USER` | Navegar produtos, criar pedidos, ver seus pedidos, gerenciar endereço |
| `ADMIN` | Tudo do USER + gerenciar produtos, categorias, atualizar status de pedidos |

## Classes envolvidas

| Classe | Responsabilidade |
|--------|----------------|
| `JwtUtil` | Gera, valida e extrai claims do token |
| `JwtAuthFilter` | `OncePerRequestFilter` — processa cada requisição |
| `UserDetailsServiceImpl` | Carrega `User` do banco pelo email |
| `SecurityConfig` | Define rotas públicas vs. protegidas, `SessionCreationPolicy.STATELESS` |

## Rotas públicas

```
POST /auth/register
POST /auth/login
GET  /products/**
GET  /categories/**
/swagger-ui/**
/v3/api-docs/**
```

## Trade-offs do JWT stateless

**Vantagens:**
- Servidor não mantém sessão → escala horizontalmente sem sticky sessions
- Simples de implementar e debugar
- Funciona bem em arquitetura de microsserviços

**Desvantagens:**
- Não dá para invalidar um token antes de expirar (ex: logout imediato)
- Para revogação, precisaria de blocklist (Redis) — feature futura

## Segurança da chave JWT

A chave é lida de `${JWT_SECRET}` (variável de ambiente). Regras:
- Mínimo 32 caracteres (256 bits para HS256)
- NUNCA commitar no git (arquivo `.env` está no `.gitignore`)
- Em produção, usar secret manager (AWS Secrets Manager, etc.)

## Relacionado

- [[api-endpoints]] — quais endpoints precisam de token
- [[arquitetura]] — onde o filtro encaixa no fluxo
