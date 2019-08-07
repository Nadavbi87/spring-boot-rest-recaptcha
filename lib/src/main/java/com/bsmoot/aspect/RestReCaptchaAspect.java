package com.bsmoot.aspect;

import com.bsmoot.constants.Constants;
import com.bsmoot.exceptions.MissingReCaptchaException;
import com.bsmoot.handlers.ErrorResponseHandler;
import com.bsmoot.handlers.SuccessResponseHandler;
import com.bsmoot.properties.ReCaptchaAspectProps;
import com.bsmoot.service.ReCaptchaService;
import com.bsmoot.util.IPUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.bsmoot.service.ReCaptchaResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * This Aspect validate whether the user send valid ReCaptcha response or not.
 * @author Nadav Bismuth
 */
@Aspect
public class RestReCaptchaAspect {
     private ReCaptchaService reCaptchaService;
     private SuccessResponseHandler successResponseHandler;
     private ErrorResponseHandler errorResponseHandler;
     private ReCaptchaAspectProps reCaptchaAspectProps;

    public RestReCaptchaAspect(ReCaptchaService reCaptchaService,
                               SuccessResponseHandler successResponseHandler,
                               ErrorResponseHandler errorResponseHandler,
                               ReCaptchaAspectProps reCaptchaAspectProps) {
        this.reCaptchaService = reCaptchaService;
        this.successResponseHandler = successResponseHandler;
        this.errorResponseHandler = errorResponseHandler;
        this.reCaptchaAspectProps = reCaptchaAspectProps;
    }

    @Around("@annotation(com.bsmoot.aspect.RequiresCaptcha)")
    public Object validateCaptcha(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getHttpServletRequest();
        String remoteIP = getRemoteIP(request);
        String reCaptchaResponse = getReCaptchaResponse(request);

        boolean isVerifiedIp = isVerifiedIp(remoteIP);
        ReCaptchaResponse response = !isVerifiedIp ?
                reCaptchaService.validate(reCaptchaResponse, remoteIP):
                null;

        if(isVerifiedIp || response.isSuccess()){
            successResponseHandler.handle(joinPoint, response, request);
        }else{
            errorResponseHandler.handle(joinPoint, response, request);
        }

        return joinPoint.proceed();
    }

    private HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    private String getRemoteIP(HttpServletRequest request){
        return IPUtil.getClientIP(request,
                reCaptchaAspectProps.getProxyRealIPHeaderName(),
                reCaptchaAspectProps.getProxyRealIPHeaderDelimiter());
    }

    private String getReCaptchaResponse(HttpServletRequest request) throws MissingReCaptchaException {
        String reCaptchaResponse = request.getHeader(reCaptchaAspectProps.getHeaderName());
        if(StringUtils.isEmpty(reCaptchaResponse)){
            throw new MissingReCaptchaException(Constants.ERROR_MISSING_RESPONSE);
        }
        return  reCaptchaResponse;
    }

    private boolean isVerifiedIp(String remoteIp){
        return  !StringUtils.isEmpty(remoteIp) && reCaptchaAspectProps.getVerifiedIps().contains(remoteIp);
    }
}
