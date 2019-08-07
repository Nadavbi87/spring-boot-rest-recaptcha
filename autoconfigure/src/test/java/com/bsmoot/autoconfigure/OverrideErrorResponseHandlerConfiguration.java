package com.bsmoot.autoconfigure;

import com.bsmoot.exceptions.ReCaptchaException;
import com.bsmoot.handlers.ErrorResponseHandler;
import com.bsmoot.service.ReCaptchaResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class OverrideErrorResponseHandlerConfiguration {

    @Bean
    ErrorResponseHandler overrideErrorResponseHandler(){
        return new OverrideErrorResponseHandler();
    }

    public static class OverrideErrorResponseHandler implements ErrorResponseHandler{

        @Override
        public void handle(ProceedingJoinPoint joinPoint, ReCaptchaResponse response, HttpServletRequest request) throws ReCaptchaException {

        }
    }
}
