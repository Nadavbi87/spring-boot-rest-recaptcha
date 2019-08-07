package com.bsmoot.handlers;

import org.aspectj.lang.ProceedingJoinPoint;
import com.bsmoot.service.ReCaptchaResponse;

import javax.servlet.http.HttpServletRequest;

public class DefaultSuccessResponseHandler implements SuccessResponseHandler {
    public void handle(ProceedingJoinPoint joinPoint, ReCaptchaResponse response, HttpServletRequest request) {

    }
}
