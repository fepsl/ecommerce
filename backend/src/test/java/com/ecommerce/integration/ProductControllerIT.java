package com.ecommerce.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("IT: ProductController")
class ProductControllerIT extends BaseIntegrationTest {

    @Test
    @DisplayName("GET /products retorna 200 com lista paginada")
    void listProducts_returns200() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").isNumber());
    }

    @Test
    @DisplayName("GET /products?name=Camiseta filtra corretamente")
    void listProducts_withNameFilter_returnsFiltered() throws Exception {
        mockMvc.perform(get("/products").param("name", "Camiseta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value(org.hamcrest.Matchers.containsStringIgnoringCase("Camiseta")));
    }

    @Test
    @DisplayName("GET /products/{id} existente retorna 200")
    void getProduct_existingId_returns200() throws Exception {
        mockMvc.perform(get("/products/" + SEED_PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SEED_PRODUCT_ID));
    }

    @Test
    @DisplayName("GET /products/{id} inexistente retorna 404")
    void getProduct_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/products/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /products sem auth retorna 401")
    void createProduct_noAuth_returns401() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProductJson("Produto IT")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /products com USER retorna 403")
    void createProduct_asUser_returns403() throws Exception {
        String token = loginAsUser();
        mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProductJson("Produto IT")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /products com ADMIN retorna 201 e produto aparece no GET")
    void createProduct_asAdmin_returns201AndAppearsInList() throws Exception {
        String token = loginAsAdmin();
        String body = mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProductJson("Produto Novo IT")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Produto Novo IT"))
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(body).get("id").asText();

        mockMvc.perform(get("/products/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Novo IT"));
    }

    @Test
    @DisplayName("DELETE /products/{id} com ADMIN desativa o produto")
    void deleteProduct_asAdmin_productDisappearsFromList() throws Exception {
        String token = loginAsAdmin();

        // Cria produto para deletar
        String body = mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProductJson("Produto Para Deletar")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(body).get("id").asText();

        mockMvc.perform(delete("/products/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // Produto inativo não aparece na listagem pública
        mockMvc.perform(get("/products").param("name", "Produto Para Deletar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    private String validProductJson(String name) {
        return """
                {
                  "name": "%s",
                  "description": "Descrição de teste",
                  "price": 99.90,
                  "stock": 10,
                  "categoryId": "%s"
                }
                """.formatted(name, SEED_CATEGORY_ID);
    }
}
