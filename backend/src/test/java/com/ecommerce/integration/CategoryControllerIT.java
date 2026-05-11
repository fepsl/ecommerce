package com.ecommerce.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("IT: CategoryController")
class CategoryControllerIT extends BaseIntegrationTest {

    @Test
    @DisplayName("GET /categories retorna 200 com lista")
    void listCategories_returns200() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("POST /categories sem auth retorna 401")
    void createCategory_noAuth_returns401() throws Exception {
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Nova Cat","description":"desc"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /categories com ADMIN retorna 201")
    void createCategory_asAdmin_returns201() throws Exception {
        String token = loginAsAdmin();
        mockMvc.perform(post("/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Moda Praia","description":"Biquinis e shorts"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Moda Praia"));
    }

    @Test
    @DisplayName("PUT /categories/{id} com ADMIN retorna 200 com dados atualizados")
    void updateCategory_asAdmin_returns200() throws Exception {
        String token = loginAsAdmin();

        // Cria a categoria para atualizar
        String body = mockMvc.perform(post("/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Para Atualizar","description":"antiga"}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(body).get("id").asText();

        mockMvc.perform(put("/categories/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Atualizada","description":"nova desc"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atualizada"));
    }

    @Test
    @DisplayName("PUT /categories/{id} com id inexistente retorna 404")
    void updateCategory_unknownId_returns404() throws Exception {
        String token = loginAsAdmin();
        mockMvc.perform(put("/categories/00000000-0000-0000-0000-000000000000")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"X","description":"y"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /categories/{id} com ADMIN remove a categoria")
    void deleteCategory_asAdmin_removes() throws Exception {
        String token = loginAsAdmin();

        String body = mockMvc.perform(post("/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Para Deletar","description":"x"}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(body).get("id").asText();

        mockMvc.perform(delete("/categories/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /categories/{id} com id inexistente retorna 404")
    void deleteCategory_unknownId_returns404() throws Exception {
        String token = loginAsAdmin();
        mockMvc.perform(delete("/categories/00000000-0000-0000-0000-000000000000")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
