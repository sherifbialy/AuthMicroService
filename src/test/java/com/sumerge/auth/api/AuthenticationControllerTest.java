package com.sumerge.auth.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.sumerge.auth.TestConfig;
import com.sumerge.auth.config.ApplicationConfig;
import com.sumerge.auth.config.JwtAuthenticationFilter;
import com.sumerge.auth.config.JwtService;
import com.sumerge.auth.user.Role;
import com.sumerge.auth.user.User;
import com.sumerge.auth.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)

class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;


    @Mock
    private JwtAuthenticationFilter jwtFilter;

    @Autowired
    JwtService service;


    @BeforeEach
    void setUp() {



    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void registerLegalRequestShouldSucceedWithToken() throws Exception {

        String jsonRequestBody = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"email\": \"john.doe@example.com\",\n" +
                "  \"password\": \"securePassword123\"\n" +
                "}";
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(new AuthenticationResponse("token"));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void registerEmptyRequestShouldFail() throws Exception {

        String jsonRequestBody = "";
       //Mockito.when(authenticationService.register(any(RegisterRequest.class))).thenThrow(new NullPointerException("No Body"));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestBody)
                )
                .andExpect(status().is4xxClientError());


    }
    @Test
    void loginEmptyRequestShouldFail() throws Exception {

        String jsonRequestBody = "";


        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestBody)
                )
                .andExpect(status().is4xxClientError());


    }

    @Test
    void loginLegalRequestShouldSucceedWithToken() throws Exception {
        String jsonRequestBody = "{\n" +
                "  \"email\": \"john.doe@example.com\",\n" +
                "  \"password\": \"securePassword123\"\n" +
                "}";
        when(authenticationService.login(any(AuthenticationRequest.class))).thenReturn(new AuthenticationResponse("token"));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }




    @Test
    void validateWithValidTokenShouldBeSucceed() throws Exception {

        //Mockito.doNothing().when(jwtFilter.doFilter(any(), any(), any()));
        var mockUser=new User(154,"Belyak","Grizzly","belyak@polya.ru","Placeholder", Role.USER);



         service = spy(service);
        doReturn(true).when(service).validateToken(anyString(), eq(mockUser));

        var token=service.generateToken(mockUser);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/validate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer "+token)

                )
                .andExpect(status().isOk());

    }

    @Test
    void validateWithoutTokenShouldBeForbidden() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/validate")
                                .contentType(MediaType.APPLICATION_JSON)


                )
                .andExpect(status().isForbidden());

    }
    }
