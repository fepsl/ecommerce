# Testes

> [[index|← Voltar ao índice]]

---

## Estratégia de testes

O projeto segue uma pirâmide de testes com foco em testes unitários de service (camada onde está toda a lógica de negócio) e testes de integração planejados para a Fase 2.

```
        /\
       /  \   E2E (futuro)
      /----\
     /      \  Integração — @SpringBootTest + TestContainers (Fase 2)
    /--------\
   /          \  Unitários — JUnit 5 + Mockito (✅ em andamento)
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

## Casos cobertos — ProductServiceTest

| Teste | Cenário |
|-------|---------|
| `deveListarProdutosAtivos` | Repository retorna lista → service retorna DTOs corretamente mapeados |
| `deveLancarExcecaoQuandoProdutoNaoEncontrado` | `findById` retorna vazio → lança `ResourceNotFoundException` |
| `deveCriarProduto` | Dados válidos → salva entidade, retorna `ProductResponse` |
| `deveDesativarProduto` | `DELETE` → `active = false`, não deleta do banco |

---

## Testes planejados — Fase 2

### Cobertura target: 80% nos services

| Service | Casos a cobrir |
|---------|---------------|
| `OrderService` | Estoque insuficiente, `unit_price` copiado corretamente, decremento de estoque |
| `AuthService` | Email duplicado → 409, senha hasheada com BCrypt |
| `UserService` | Upsert de endereço (criar e atualizar) |
| `CategoryService` | CRUD básico, category vinculada a produto (não pode deletar) |

### TestContainers (PostgreSQL real)

```java
@SpringBootTest
@Testcontainers
class OrderControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
```

*Por que TestContainers e não H2?*  
H2 não suporta todas as features do PostgreSQL (UUID nativo, `gen_random_uuid()`, etc.). Para testes de integração reais, PostgreSQL em container é o mais fiel ao ambiente de produção.

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
