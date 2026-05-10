# Decisões Técnicas (ADRs)

> [[index|← Voltar ao índice]]  
> ADR = Architecture Decision Record — justificativas para explicar em entrevista

---

## ADR-01 — DTO em vez de entidade na API

**Decisão**: Nunca retornar entidades JPA diretamente nos controllers. Sempre mapear para DTOs de resposta.

**Justificativa**:
1. **Segurança**: evita expor campos como `password`, mesmo que o campo seja `@JsonIgnore` (fácil de esquecer)
2. **Desacoplamento**: o contrato da API pode evoluir independente do schema do banco
3. **Flexibilidade**: um DTO pode combinar dados de várias entidades sem expor a estrutura interna

**Trade-off**: mais boilerplate (código de mapeamento nos services)

---

## ADR-02 — Injeção de dependência por construtor

**Decisão**: Todos os beans usam `@RequiredArgsConstructor` + campos `final`.

```java
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository; // final!
```

**Justificativa**:
1. `final` garante que o campo nunca seja nulo após construção
2. Facilita testes unitários — passa o mock direto no construtor, sem reflexão
3. Imutabilidade: o service não pode "trocar" o repositório depois de criado

**Alternativa descartada**: `@Autowired` no campo — menos seguro e dificulta testes

---

## ADR-03 — JWT stateless (sem sessão)

**Decisão**: Autenticação 100% baseada em token JWT, `SessionCreationPolicy.STATELESS`.

**Justificativa**:
1. Servidor não precisa de memória compartilhada para sessões
2. Escala horizontalmente sem sticky sessions ou Redis de sessão
3. Simples de integrar com SPAs e mobile

**Trade-off**: não dá para invalidar um token antes de expirar.  
*Mitigação futura*: blocklist em Redis para logout seguro — ver [[roadmap]]

---

## ADR-04 — Flyway para migrations

**Decisão**: Toda mudança de schema via arquivo SQL versionado em `db/migration/`.

**Justificativa**:
1. Banco e código ficam sempre sincronizados (sem "esqueci de rodar o ALTER TABLE")
2. Histórico de mudanças rastreável no git
3. Reproducível em qualquer ambiente: dev, CI, produção

**Alternativa descartada**: `ddl-auto: create` ou `update` do Hibernate — perigoso em produção, sem histórico

---

## ADR-05 — LAZY loading com EntityGraph

**Decisão**: Todos os relacionamentos JPA com `FetchType.LAZY`. Quando precisa dos dados, usa `@EntityGraph`.

**Justificativa**:
1. EAGER carregaria dados desnecessários em toda consulta
2. LAZY com `@EntityGraph` resolve o N+1 na query, não em código Java
3. Explícito: fica claro no repositório quando os relacionamentos são carregados

**Exemplo**:
```java
@EntityGraph(attributePaths = {"items", "items.product"})
Optional<Order> findById(UUID id);
// → Um JOIN FETCH, não N+1 queries
```

---

## ADR-06 — `unit_price` imutável em `order_items`

**Decisão**: Ao criar um pedido, copia `product.price` para `order_item.unit_price`. O campo nunca é atualizado.

**Justificativa**:
1. Se o admin atualizar o preço depois, pedidos antigos não mudam
2. Integridade financeira: o valor que o cliente pagou fica registrado
3. Auditoria: é possível reconstruir o total de qualquer pedido histórico

---

## ADR-07 — Soft delete em produtos

**Decisão**: `DELETE /products/{id}` não deleta o registro — apenas marca `active = false`.

**Justificativa**:
1. `order_items` referencia `products.id` — deletar causaria violação de FK
2. Histórico de pedidos permanece íntegro
3. Admin pode "reativar" um produto se necessário

---

## ADR-08 — HTML/CSS/JS puro no frontend

**Decisão**: Frontend sem framework (sem React, Vue, etc.).

**Justificativa**:
1. Demonstra conhecimento dos fundamentos web
2. Sem build step — arquivos servidos diretamente pelo Nginx
3. Foco do portfólio é backend — frontend existe para demonstrar consumo de API REST
4. Menor complexidade de configuração para um projeto solo

**Quando reconsiderar**: se o frontend crescer muito em complexidade, migrar para React + Vite faria sentido.

## Relacionado

- [[arquitetura]] — como as decisões se manifestam no código
- [[banco-de-dados]] — decisões específicas do schema
