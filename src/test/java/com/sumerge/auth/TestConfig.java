package com.sumerge.auth;


import com.sumerge.auth.config.JwtAuthenticationFilter;
import com.sumerge.auth.config.JwtService;
import com.sumerge.auth.recaptcha.RecaptchaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {

    @Bean
    public JwtService jwtService(){
        return new JwtService();
    }


}
