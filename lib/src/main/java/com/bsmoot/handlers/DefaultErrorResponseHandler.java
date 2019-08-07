package com.bsmoot.handlers;

import com.bsmoot.exceptions.ClientReCaptchaException;
import com.bsmoot.exceptions.ReCaptchaException;
import com.bsmoot.exceptions.ServerReCaptchaException;
import org.aspectj.lang.ProceedingJoinPoint;
import com.bsmoot.service.ReCaptchaResponse;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class DefaultErrorResponseHandler implements ErrorResponseHandler {

    public void handle(ProceedingJoinPoint joinPoint, ReCaptchaResponse response, HttpServletRequest request) throws ReCaptchaException {
        ReCaptchaResponse.ErrorCode[] errorCodes = response.getErrorCodes();
        String errorMessage = getFirstErrorMessage(errorCodes);
        if(response.hasServerError()){
            throw new ServerReCaptchaException(errorMessage);
        }else if(response.hasClientError() || StringUtils.isEmpty(errorMessage)){
            throw new ClientReCaptchaException(errorMessage);
        }
    }

    private String getFirstErrorMessage(ReCaptchaResponse.ErrorCode[] errorCodes) {
        return errorCodes != null && errorCodes[0] != null ? errorCodes[0].getErrorCodeKey() : "";
    }
}
