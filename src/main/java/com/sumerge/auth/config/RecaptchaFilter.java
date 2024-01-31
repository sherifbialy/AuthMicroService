package com.sumerge.auth.config;

import com.sumerge.auth.recaptcha.RecaptchaResponse;
import com.sumerge.auth.recaptcha.RecaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Import({RecaptchaService.class, RecaptchaResponse.class})
public class RecaptchaFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RecaptchaFilter.class);
    private final RecaptchaService recaptchaService;

    public RecaptchaFilter(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equals("POST") ) {
            String recaptcha = request.getHeader("recaptcha");
            RecaptchaResponse recaptchaResponse = recaptchaService.validateToken(recaptcha);

            if(!recaptchaResponse.isSuccess()) {
                response.setStatus(403);
            }
        }
        filterChain.doFilter(request,response);
    }

}