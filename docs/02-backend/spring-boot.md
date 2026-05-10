# Configuração Spring Boot

> [[index|← Voltar ao índice]]

---

## Profiles

O projeto usa dois profiles: `dev` (local) e `prod` (produção).

### application.yml (base)

```yaml
spring:
  application:
    name: ecommerce-api
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}
  jpa:
    hibernate:
      ddl-auto: validate          # Flyway controla o schema; Hibernate só valida
    open-in-view: false           # evita LazyInitializationException silenciosa
  flyway:
    locations: classpath:db/migration

server:
  port: 8080

jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000            # 24h em ms

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

### application-dev.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
  jpa:
    show-sql: true                # loga SQLs no console durante dev
    properties:
      hibernate:
        format_sql: true
```

### application-prod.yml

```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}
  jpa:
    show-sql: false
logging:
  level:
    root: WARN
    com.ecommerce: INFO
```

---

## Dependências principais (pom.xml)

| Dependência | Versão | Função |
|-------------|--------|--------|
| `spring-boot-starter-web` | 3.2.5 | REST API, Jackson, Tomcat |
| `spring-boot-starter-security` | 3.2.5 | Filtros, autenticação, autorização |
| `spring-boot-starter-data-jpa` | 3.2.5 | Hibernate, repositórios JPA |
| `spring-boot-starter-validation` | 3.2.5 | Bean Validation (JSR-380) |
| `postgresql` | 42.x | Driver JDBC |
| `flyway-core` | 9.x | Migrations versionadas |
| `jjwt-api` + `jjwt-impl` | 0.11.5 | Geração e validação de JWT |
| `lombok` | 1.18.x | `@Data`, `@Builder`, `@RequiredArgsConstructor` |
| `springdoc-openapi-starter-webmvc-ui` | 2.x | Swagger UI automático |

---

## Swagger / OpenAPI

Acesse em: http://localhost:8080/swagger-ui.html

Para testar endpoints protegidos no Swagger:
1. Faça login em `POST /auth/login`
2. Copie o `token` da resposta
3. Clique em "Authorize" (cadeado) no topo
4. Cole: `Bearer <token>` no campo `bearerAuth`
5. Agora todos os requests incluirão o header automaticamente

### Configuração do Swagger com auth Bearer

```java
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("ModaShop API").version("1.0"))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
```

---

## Bean Validation — anotações usadas

| Anotação | Onde usa | Exemplo |
|----------|---------|---------|
| `@NotBlank` | Strings obrigatórias | `name`, `email` |
| `@NotNull` | Objetos obrigatórios | `categoryId` |
| `@Email` | Formato de email | `email` |
| `@Size` | Comprimento mínimo/máximo | `@Size(min=6)` em `password` |
| `@DecimalMin` | Valores numéricos | `@DecimalMin("0.01")` em `price` |
| `@Pattern` | Regex | `@Pattern(regexp="\\d{5}-\\d{3}")` em `zip` |
| `@Positive` | Inteiros positivos | `quantity`, `stock` |

O `@Valid` no parâmetro do controller ativa a validação. Falha de validação lança `MethodArgumentNotValidException`, capturada pelo `GlobalExceptionHandler`.

---

## CORS

```java
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",      // frontend dev
            "${FRONTEND_URL:}"           // produção via env
        ));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        // ...
    }
}
```

---

## Relacionado

- [[arquitetura]] — estrutura de camadas e pacotes
- [[autenticacao-jwt]] — configuração do SecurityFilterChain
- [[variaveis-ambiente]] — referência completa de variáveis
- [[docker]] — como o app roda em container
