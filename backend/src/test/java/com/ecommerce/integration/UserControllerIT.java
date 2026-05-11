package com.ecommerce.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("IT: UserController")
class UserControllerIT extends BaseIntegrationTest {

    @Test
    @DisplayName("GET /users/me sem auth retorna 401")
    void getProfile_noAuth_returns401() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /users/me autenticado retorna 200 com perfil")
    void getProfile_authenticated_returns200() throws Exception {
        String token = loginAsUser();
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(USER_EMAIL))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DisplayName("PUT /users/me/address com dados válidos cria endereço e aparece no GET")
    void updateAddress_validData_createsAndReflectsInProfile() throws Exception {
        String token = registerAndLogin("user_address_create@test.com");

        mockMvc.perform(put("/users/me/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson("Rua das Flores", "123", "São Paulo", "SP", "01310-100")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Rua das Flores"))
                .andExpect(jsonPath("$.zip").value("01310-100"));
    }

    @Test
    @DisplayName("PUT /users/me/address segunda chamada atualiza sem criar novo registro")
    void updateAddress_calledTwice_upserts() throws Exception {
        String token = registerAndLogin("user_address_upsert@test.com");

        mockMvc.perform(put("/users/me/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson("Rua A", "1", "Cidade A", "RJ", "20040-020")))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users/me/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson("Rua B", "2", "Cidade B", "SP", "01310-100")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Rua B"))
                .andExpect(jsonPath("$.city").value("Cidade B"));
    }

    @Test
    @DisplayName("PUT /users/me/address com CEP inválido retorna 400")
    void updateAddress_invalidZip_returns400() throws Exception {
        String token = registerAndLogin("user_address_invalid@test.com");

        mockMvc.perform(put("/users/me/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson("Rua X", "10", "São Paulo", "SP", "99999999")))
                .andExpect(status().isBadRequest());
    }

    // ── helper ───────────────────────────────────────────────────

    private String addressJson(String street, String number, String city, String state, String zip) {
        return """
                {
                  "street": "%s",
                  "number": "%s",
                  "city": "%s",
                  "state": "%s",
                  "zip": "%s"
                }
                """.formatted(street, number, city, state, zip);
    }
}
