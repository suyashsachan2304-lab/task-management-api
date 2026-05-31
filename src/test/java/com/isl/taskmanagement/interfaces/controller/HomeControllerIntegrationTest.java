package com.isl.taskmanagement.interfaces.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Root redirects to swagger")
    void rootRedirectsToSwagger()
            throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(
                        status()
                                .is3xxRedirection()
                )
                .andExpect(
                        redirectedUrl(
                                "/swagger-ui.html"
                        )
                );
    }

    @Test
    @DisplayName("Favicon returns JSON 404")
    void faviconReturnsJson404()
            throws Exception {

        mockMvc.perform(
                        get("/favicon.ico")
                                .accept(
                                        MediaType.APPLICATION_JSON
                                )
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.path")
                        .value("/favicon.ico"))
                .andExpect(jsonPath("$.timestamp")
                        .exists())
                .andExpect(jsonPath("$.message")
                        .exists());
    }

    @Test
    @DisplayName("Unknown endpoint returns JSON 404")
    void unknownEndpointReturns404()
            throws Exception {

        mockMvc.perform(
                        get("/does-not-exist")
                                .accept(
                                        MediaType.APPLICATION_JSON
                                )
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.path")
                        .value("/does-not-exist"));
    }
}