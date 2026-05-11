package com.ecommerce.integration;

import com.ecommerce.dto.request.LoginRequest;
import com.ecommerce.dto.request.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test-integration")
@Sql(scripts = "/integration-seed.sql", executionPhase = BEFORE_TEST_CLASS)
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    // ── IDs do seed (integration-seed.sql) ─────────────────────
    protected static final String ADMIN_EMAIL    = "admin@ecommerce.com";
    protected static final String USER_EMAIL     = "cliente@ecommerce.com";
    protected static final String SEED_PASSWORD  = "senha123";

    protected static final String SEED_PRODUCT_ID  = "20000000-0000-0000-0000-000000000001";
    protected static final String SEED_CATEGORY_ID = "10000000-0000-0000-0000-000000000001";

    // ── Helpers de autenticação ─────────────────────────────────

    protected String loginAsAdmin() throws Exception {
        return doLogin(ADMIN_EMAIL, SEED_PASSWORD);
    }

    protected String loginAsUser() throws Exception {
        return doLogin(USER_EMAIL, SEED_PASSWORD);
    }

    protected String registerAndLogin(String email) throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setName("Test User");
        req.setEmail(email);
        req.setPassword("senha123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        return doLogin(email, "senha123");
    }

    private String doLogin(String email, String password) throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail(email);
        req.setPassword(password);

        String body = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(body).get("token").asText();
    }
}
