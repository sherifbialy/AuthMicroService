package com.sumerge.auth.recaptcha;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecaptchaResponse {
    private boolean success;
    private String challenge_ts;
    private String hostname;
    private double score;
    private String action;
}
