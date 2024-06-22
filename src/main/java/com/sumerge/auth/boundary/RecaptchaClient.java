package com.sumerge.auth.boundary;

import com.sumerge.auth.entity.RecaptchaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "recaptcha-client", url = "${recaptcha.verifyUrl}")
public interface RecaptchaClient {
    @PostMapping("")
    ResponseEntity<RecaptchaResponse> verifyToken(
            @RequestParam("secret") String secretKey,
            @RequestParam("response") String recaptchaToken
    );
}
