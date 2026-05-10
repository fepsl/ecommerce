# Testes

> [[index|← Voltar ao índice]]

---

## Estratégia de testes

O projeto segue uma pirâmide de testes com foco em testes unitários de service (camada onde está toda a lógica de negócio) e testes de integração planejados para a Fase 2.

```
        /\
       /  \   E2E (futuro)
      /----\
     /      \  Integração — @SpringBootTest + MockMvc + H2 (✅ concluída)
    /--------\
   /          \  Unitários — JUnit 5 + Mockito (✅ concluída — Auth, Product, Order)
  /____________\
```

---

## Testes unitários (JUnit 5 + Mockito)

### Configuração

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <!-- inclui: JUnit 5, Mockito, AssertJ, Hamcrest -->
</dependency>
```

### Onde ficam

```
src/test/java/com/ecommerce/
└── service/
    └── ProductServiceTest.java   ← 4 casos, exemplo de referência
```

### Padrão de um teste de service

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
            () -> productService.findById(id));
    }
}
```

*Por que `@ExtendWith(MockitoExtension.class)` e não `@SpringBootTest`?*  
Não sobe o contexto Spring → teste rodas muito mais rápido. O service só precisa do repositório mockado.

---

## Banco de testes (H2)

```yaml
# src/test/resources/application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop   # recria o schema a cada execução de teste
  flyway:
    enabled: false            # H2 usa o DDL do Hibernate, não as migrations
```

O profile `test` é ativado automaticamente quando `@SpringBootTest` ou `@DataJpaTest` são usados nos testes de integração.

---

## Casos cobertos

### ProductServiceTest (8 testes)

| Cenário |
|---------|
| `findAll()` sem filtros → delega ao repositório com parâmetros nulos |
| `findAll()` com categoria → repassa UUID correto |
| `findById()` produto ativo → retorna `ProductResponse` |
| `findById()` produto inativo → lança `ResourceNotFoundException` |
| `findById()` inexistente → lança `ResourceNotFoundException` |
| `create()` com dados válidos → salva e retorna resposta |
| `create()` com categoria inexistente → lança `ResourceNotFoundException`, não chama `save()` |
| `deactivate()` → seta `active = false`, chama `save()` |

### AuthServiceTest (5 testes)

| Cenário |
|---------|
| `register()` com email novo → retorna `AuthResponse` com token |
| `register()` com email duplicado → lança `EmailAlreadyExistsException`, não chama `save()` |
| `register()` codifica senha com BCrypt antes de salvar |
| `login()` com credenciais válidas → retorna `AuthResponse` com token |
| `login()` com senha errada → lança `BadCredentialsException` |

### OrderServiceTest (6 testes)

| Cenário |
|---------|
| `create()` com quantidade > estoque → lança `InsufficientStockException` |
| `create()` salva `unit_price` igual ao preço atual do produto |
| `create()` decrementa estoque e chama `productRepository.save()` |
| `findByUser()` retorna apenas pedidos do usuário informado |
| `findById()` por USER acessando pedido alheio → lança `UnauthorizedException` |
| `findById()` por ADMIN → pode ver pedido de qualquer usuário |

### AuthIntegrationTest (4 testes — `@SpringBootTest` + MockMvc + H2)

| Cenário |
|---------|
| `POST /auth/register` → 201 com token no corpo |
| `POST /auth/login` → 200 com token no corpo |
| Endpoint protegido sem token → 401 |
| Endpoint protegido com token válido → 200 |

---

## Status da Fase 2

| Service | Testes | Status |
|---------|--------|--------|
| `AuthService` | 5 unitários | ✅ Concluído |
| `ProductService` | 8 unitários | ✅ Concluído |
| `OrderService` | 6 unitários | ✅ Concluído |
| Integração (AuthIntegrationTest) | 4 testes MockMvc | ✅ Concluído |
| `UserService` | — | 🔵 Pendente |
| `CategoryService` | — | 🔵 Pendente |

**Total: 24 testes passando — `mvn test` BUILD SUCCESS**

### Cobertura target: 80% nos services críticos (JaCoCo — configurar na Fase 3)

### TestContainers (PostgreSQL real) — trabalho futuro

Os testes de integração atuais usam H2 in-memory com MockMvc, o que é suficiente para o portfólio. TestContainers seria o próximo passo para uma cobertura mais fiel ao PostgreSQL de produção:

```java
@SpringBootTest
@Testcontainers
class OrderControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
```

*Por que H2 e não TestContainers agora?*  
H2 é suficiente para validar a lógica de negócio nos services. TestContainers testa comportamentos específicos do PostgreSQL (UUID nativo, `gen_random_uuid()`, queries JSONB) — relevante quando a Fase 4 (Docker) estiver completa.

---

## Como rodar os testes

```bash
# Rodar todos os testes
mvn test

# Rodar um arquivo específico
mvn test -Dtest=ProductServiceTest

# Relatório de cobertura (JaCoCo — configurar na Fase 2)
mvn verify
open target/site/jacoco/index.html
```

---

## Relacionado

- [[roadmap]] — Fase 2: cobertura 80%, TestContainers
- [[arquitetura]] — onde cada camada é testada
- [[regras-de-negocio]] — o que os testes de service devem validar
