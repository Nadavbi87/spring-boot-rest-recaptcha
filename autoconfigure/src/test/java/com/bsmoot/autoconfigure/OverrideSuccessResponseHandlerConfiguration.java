package com.bsmoot.autoconfigure;

import com.bsmoot.exceptions.ReCaptchaException;
import com.bsmoot.handlers.SuccessResponseHandler;
import com.bsmoot.service.ReCaptchaResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class OverrideSuccessResponseHandlerConfiguration {

    @Bean
    public OverrideSuccessResponseHandler overrideSuccessResponseHandler(){
        return new OverrideSuccessResponseHandler();
    }

    public static class OverrideSuccessResponseHandler implements SuccessResponseHandler{

        @Override
        public void handle(ProceedingJoinPoint joinPoint, ReCaptchaResponse response, HttpServletRequest request) throws ReCaptchaException {

        }
    }
}
