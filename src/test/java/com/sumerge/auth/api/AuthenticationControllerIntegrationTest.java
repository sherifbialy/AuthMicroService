package com.sumerge.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.sumerge.auth.config.filters.JwtAuthenticationFilter;
import com.sumerge.auth.config.filters.RecaptchaFilter;
import com.sumerge.auth.recaptcha.RecaptchaResponse;
import com.sumerge.auth.recaptcha.RecaptchaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableConfigurationProperties

public class AuthenticationControllerIntegrationTest {



        @Autowired

        private ObjectMapper objectMapper;

        private MockMvc mockMvc;

        @Mock
    RecaptchaService recaptchaService;

        @Autowired
        WebApplicationContext webApplicationContext;

        @Autowired
        JwtAuthenticationFilter jwtAuthenticationFilter;

        @Autowired
    RecaptchaFilter recaptchaFilter;


        @BeforeEach
        void setUp() {

            mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(recaptchaFilter).build();

        }
        @RegisterExtension
        static WireMockExtension wireMockServer=
                WireMockExtension.newInstance().options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();
        @DynamicPropertySource
        static  void configureProperties(DynamicPropertyRegistry registry){
            String customPath = "/recaptcha/api/siteverify";
            String fullPath = wireMockServer.baseUrl() + customPath;


            registry.add("recaptcha.verifyUrl", () -> fullPath);
        }


        @Test
        void recaptchaFailedSoRegisterFails() throws Exception {
            String jsonRequestBody = "{\n" +
                    "  \"firstName\": \"John\",\n" +
                    "  \"lastName\": \"Doe\",\n" +
                    "  \"email\": \"john.doe@example.com\",\n" +
                    "  \"password\": \"securePassword123\"\n" +
                    "}";
            RecaptchaResponse recaptchaResponse=new RecaptchaResponse(false,"","",0.0,"");
           Mockito.when(recaptchaService.validateToken(Mockito.any())).thenReturn(recaptchaResponse);
           // recaptchaResponse.setSuccess(false);
            wireMockServer.stubFor(post("/recaptcha/api/siteverify").willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(objectMapper.writeValueAsString(recaptchaResponse))));
            System.out.println(wireMockServer.baseUrl());
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequestBody)
                            .header("recaptcha","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWx5YWtAcG9seWEucnUiLCJpYXQiOjE3MDY2MzM4NjYsImV4cCI6MTcwNjYzNTY2Nn0.6qbCOt0furCWL8oS9OGhc8XkEUGPWw7t5C4tXDlgtoY")
            ).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isUnauthorized())
            ;
        }
    @Test
    void recaptchaSuccessSoRegisterSuccess() throws Exception {
        String jsonRequestBody = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"email\": \"john.doe@example.com\",\n" +
                "  \"password\": \"securePassword123\"\n" +
                "}";
       RecaptchaResponse recaptchaResponse=new RecaptchaResponse(true,"","",0.0,"");

        Mockito.when(recaptchaService.validateToken(Mockito.any())).thenReturn(recaptchaResponse);
        wireMockServer.stubFor(post("/recaptcha/api/siteverify").willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(objectMapper.writeValueAsString(recaptchaResponse))));
        System.out.println(wireMockServer.baseUrl());
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestBody)
                               // .header("recaptcha","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWx5YWtAcG9seWEucnUiLCJpYXQiOjE3MDY2MzM4NjYsImV4cCI6MTcwNjYzNTY2Nn0.6qbCOt0furCWL8oS9OGhc8XkEUGPWw7t5C4tXDlgtoY")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
        ;
    }









}
