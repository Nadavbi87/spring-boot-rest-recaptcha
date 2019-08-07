package com.bsmoot.sampleapp;

import com.bsmoot.exceptions.MissingReCaptchaException;
import com.bsmoot.exceptions.ReCaptchaException;
import com.bsmoot.exceptions.ServerReCaptchaException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {

    @ExceptionHandler(ReCaptchaException.class)
    protected ResponseEntity<Object> ReCaptchaException(ReCaptchaException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String message = ex.getMessage();
        if(ex instanceof ServerReCaptchaException){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }else if (ex instanceof MissingReCaptchaException){
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(message, status);
    }
}
