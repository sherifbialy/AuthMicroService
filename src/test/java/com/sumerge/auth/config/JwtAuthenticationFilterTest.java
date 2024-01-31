package com.sumerge.auth.config;

import com.sumerge.auth.config.JwtAuthenticationFilter;
import com.sumerge.auth.config.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class JwtAuthenticationFilterTest {


    private MockMvc mockMvc;

    @Mock
    private JwtService jwtService;

    @Autowired
    WebApplicationContext webApplicationContext;
    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
     @BeforeEach
     void setUp(){
         mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                 .addFilters(jwtAuthenticationFilter)
                 .build();
     }
    @Test
    void testJwtAuthenticationFilterValidToken() throws Exception {

        UserDetails userDetails = mock(UserDetails.class);
        Mockito.when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(userDetails);
        Mockito.when(jwtService.validateToken(any(String.class), eq(userDetails))).thenReturn(true);





        String mockToken = "yourMockedToken";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/validate")
                .header("Authorization", "Bearer " + mockToken);


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }


}
