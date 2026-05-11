package com.ecommerce.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("IT: OrderController")
class OrderControllerIT extends BaseIntegrationTest {

    // ── POST /orders ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /orders com USER autenticado e produto com estoque retorna 201")
    void createOrder_validRequest_returns201() throws Exception {
        String token = registerAndLogin("order_user_ok@test.com");

        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson(SEED_PRODUCT_ID, 1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("POST /orders com quantidade acima do estoque retorna 422")
    void createOrder_insufficientStock_returns422() throws Exception {
        String token = registerAndLogin("order_user_nostock@test.com");

        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson(SEED_PRODUCT_ID, 99999)))
                .andExpect(status().isUnprocessableEntity());
    }

    // ── GET /orders/me ───────────────────────────────────────────

    @Test
    @DisplayName("GET /orders/me retorna apenas pedidos do usuário logado")
    void myOrders_returnsOnlyOwnOrders() throws Exception {
        String userToken  = registerAndLogin("order_isolation_user@test.com");
        String adminToken = loginAsAdmin();

        // Usuário cria um pedido
        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson(SEED_PRODUCT_ID, 1)))
                .andExpect(status().isCreated());

        // Admin também cria um pedido
        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson(SEED_PRODUCT_ID, 1)))
                .andExpect(status().isCreated());

        // Cada um vê só seus próprios pedidos
        mockMvc.perform(get("/orders/me")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").isArray());
    }

    // ── GET /orders/{id} ─────────────────────────────────────────

    @Test
    @DisplayName("GET /orders/{id} com token do dono retorna 200")
    void getOrder_asOwner_returns200() throws Exception {
        String token = registerAndLogin("order_owner@test.com");

        String body = mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson(SEED_PRODUCT_ID, 1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String orderId = objectMapper.readTree(body).get("id").asText();

        mockMvc.perform(get("/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId));
    }

    @Test
    @DisplayName("GET /orders/{id} com token de outro usuário retorna 403")
    void getOrder_asOtherUser_returns403() throws Exception {
        String owner = registerAndLogin("order_real_owner@test.com");
        String other = registerAndLogin("order_intruder@test.com");

        String body = mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + owner)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson(SEED_PRODUCT_ID, 1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String orderId = objectMapper.readTree(body).get("id").asText();

        mockMvc.perform(get("/orders/" + orderId)
                        .header("Authorization", "Bearer " + other))
                .andExpect(status().isForbidden());
    }

    // ── PUT /orders/{id}/status ──────────────────────────────────

    @Test
    @DisplayName("PUT /orders/{id}/status transição inválida retorna 422")
    void updateStatus_invalidTransition_returns422() throws Exception {
        String userToken  = registerAndLogin("order_status_invalid@test.com");
        String adminToken = loginAsAdmin();

        String body = mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson(SEED_PRODUCT_ID, 1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String orderId = objectMapper.readTree(body).get("id").asText();

        // PENDING → DELIVERED é transição inválida
        mockMvc.perform(put("/orders/" + orderId + "/status")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":"DELIVERED"}
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /orders/{id}/status transição válida PENDING → PAID retorna 200")
    void updateStatus_validTransition_returns200() throws Exception {
        String userToken  = registerAndLogin("order_status_valid@test.com");
        String adminToken = loginAsAdmin();

        String body = mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson(SEED_PRODUCT_ID, 1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String orderId = objectMapper.readTree(body).get("id").asText();

        mockMvc.perform(put("/orders/" + orderId + "/status")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":"PAID"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    // ── helpers ──────────────────────────────────────────────────

    private String orderJson(String productId, int quantity) {
        return """
                {
                  "items": [
                    {"productId": "%s", "quantity": %d}
                  ]
                }
                """.formatted(productId, quantity);
    }
}
