package com.bsmoot.service;

import com.bsmoot.constants.Constants;
import com.bsmoot.exceptions.ReCaptchaException;
import com.bsmoot.properties.ReCaptchaProps;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * This is the ReCaptcha Service implementation.
 * Validate the client ReCaptcha response at Google
 * @author Nadav Bismuth
 */
public class ReCaptchaServiceImpl implements ReCaptchaService {

    private RestTemplate restTemplate;
    private ReCaptchaProps reCaptchaProps;

    public ReCaptchaServiceImpl(RestTemplate restTemplate, ReCaptchaProps reCaptchaProps) {
        this.restTemplate = restTemplate;
        this.reCaptchaProps = reCaptchaProps;
    }

    public ReCaptchaResponse validate(String reCaptchaResponse, String remoteIp) throws ReCaptchaException {
        ReCaptchaResponse response;
        try {
            response = restTemplate.postForObject(
                    reCaptchaProps.getVerifyUrl(),
                    buildRequestBody(reCaptchaResponse, remoteIp),
                    ReCaptchaResponse.class);
        }catch (Exception e){
            response = new ReCaptchaResponse(false, getGeneralError());
        }
        return response;
    }

    private ReCaptchaResponse.ErrorCode[] getGeneralError() {
        ReCaptchaResponse.ErrorCode[] errorCode = new ReCaptchaResponse.ErrorCode[1];
        errorCode[0] = ReCaptchaResponse.ErrorCode.forValue(Constants.ERROR_GENERAL);
        return errorCode;
    }

    private MultiValueMap<String, String> buildRequestBody(String reCaptchaResponse, String remoteIp){
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add(Constants.RQUEST_SECRET_PARAM_NAME, reCaptchaProps.getSecretKey());
        requestBody.add(Constants.RQUEST_REMOTE_IP_PARAM_NAME, remoteIp);
        requestBody.add(Constants.RQUEST_RESPONSE_PARAM_NAME, reCaptchaResponse);
        return requestBody;
    }
}
