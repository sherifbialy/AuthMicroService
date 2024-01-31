package com.sumerge.auth;


import com.sumerge.auth.config.JwtAuthenticationFilter;
import com.sumerge.auth.config.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class TestConfig {

    @Bean
    public JwtService jwtService(){
        return new JwtService();
    }


}
