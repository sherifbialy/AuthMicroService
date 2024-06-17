package com.sumerge.auth.common.exceptions;

public class AuthException extends Throwable {
    String message;
    public AuthException(String message) {
        this.message = message;
    }
}
