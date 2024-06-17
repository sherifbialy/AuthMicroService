package com.sumerge.auth;


import com.sumerge.auth.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public JwtService jwtService(){
        return new JwtService();
    }


}
