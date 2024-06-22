package com.sumerge.auth.control;


import com.sumerge.auth.boundary.RecaptchaClient;
import com.sumerge.auth.entity.RecaptchaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

public class RecaptchaService {
    @Value("${recaptcha.secretKey}")
    private String secretKey;
    @Value("${recaptcha.verifyUrl}")
    private String verifyUrl;
    private final RecaptchaClient recaptchaClient;

    public RecaptchaService(RecaptchaClient recaptchaClient) {
        this.recaptchaClient = recaptchaClient;
    }

    public RecaptchaResponse validateToken(String recaptchaToken) {

        return recaptchaClient.verifyToken(secretKey,recaptchaToken).getBody();

    }
}
