package com.bsmoot.service;

import com.bsmoot.exceptions.ReCaptchaException;

/**
 * This interface define ReCaptcha Service contract
 * @author Nadav Bsimuth
 */
public interface ReCaptchaService {
    ReCaptchaResponse validate(String reCaptchaResponse, String remoteIp) throws ReCaptchaException;
}
