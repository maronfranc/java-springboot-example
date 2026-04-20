package com.example.springexample.java.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthcheckController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldReturnPingMessage() throws Exception {
        mockMvc.perform(get("/healthcheck/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Pong"));
    }

    @Test
    void shouldReturnDatabaseConnectedMessage() throws Exception {
        mockMvc.perform(get("/healthcheck/db"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Database is reachable."));
    }

}
