package com.sumerge.auth.config;

import com.sumerge.auth.recaptcha.RecaptchaResponse;
import com.sumerge.auth.recaptcha.RecaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Import({RecaptchaService.class, RecaptchaResponse.class})
public class RecaptchaFilter extends OncePerRequestFilter {

    private final RecaptchaService recaptchaService;

    public RecaptchaFilter(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equals("POST") ) {
            String recaptcha = request.getHeader("recaptcha");

            RecaptchaResponse recaptchaResponse = recaptchaService.validateToken(recaptcha);
            recaptchaResponse.setSuccess(true);
            if(!recaptchaResponse.isSuccess()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid reCAPTCHA token");
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

}