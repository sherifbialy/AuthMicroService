//package com.sumerge.auth.recaptcha;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class RecaptchaService {
//    @Value("${recaptcha.secretKey}")
//    private String secretKey;
//    @Value("${recaptcha.verifyUrl}")
//    private String verifyUrl;
//    private final RestTemplate restTemplate;
//
//    public RecaptchaService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public RecaptchaResponse validateToken(String recaptchaToken) {
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
//        map.add("secret", secretKey);
//        map.add("response",recaptchaToken);
//        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map,headers);
//
//        ResponseEntity<RecaptchaResponse> response = restTemplate.exchange(verifyUrl,
//                HttpMethod.POST,
//                entity,
//                RecaptchaResponse.class);
//
//        return response.getBody();
//    }
//}
