package com.bsmoot.handlers;

import com.bsmoot.exceptions.ReCaptchaException;
import org.aspectj.lang.ProceedingJoinPoint;
import com.bsmoot.service.ReCaptchaResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Represent the response handlers interface
 * @author Nadav Bismuth
 */
public interface ResponseHandler {
    void handle(ProceedingJoinPoint joinPoint, ReCaptchaResponse response, HttpServletRequest request)
            throws ReCaptchaException;
}
