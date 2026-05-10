package com.ecommerce;

import com.ecommerce.dto.request.LoginRequest;
import com.ecommerce.dto.request.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Integração: autenticação")
class AuthIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /auth/register retorna 200 com token no corpo")
    void register_returnsTokenInBody() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Teste Integração");
        request.setEmail("integracao_register@email.com");
        request.setPassword("senha123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("integracao_register@email.com"));
    }

    @Test
    @DisplayName("POST /auth/login retorna 200 com token no corpo")
    void login_returnsTokenInBody() throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setName("Login Test");
        register.setEmail("integracao_login@email.com");
        register.setPassword("senha123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        LoginRequest login = new LoginRequest();
        login.setEmail("integracao_login@email.com");
        login.setPassword("senha123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Endpoint protegido sem token retorna 401")
    void protectedEndpoint_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/orders/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Endpoint protegido com token válido retorna 200")
    void protectedEndpoint_withValidToken_returns200() throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setName("Auth Test");
        register.setEmail("integracao_authtest@email.com");
        register.setPassword("senha123");

        String body = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(body).get("token").asText();

        mockMvc.perform(get("/orders/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
