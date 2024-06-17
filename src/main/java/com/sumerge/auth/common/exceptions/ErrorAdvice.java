package com.sumerge.auth.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorAdvice {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationHandler(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldErrors().
                stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).
                collect(Collectors.joining(" | "));
        message = "Arguments not valid " + message;
        return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
    }
}
