package com.bsmoot.exceptions;

import com.bsmoot.service.ReCaptchaResponse;

/**
 * Server ReCaptcha exception
 * {@link ReCaptchaResponse#hasServerError()}
 * @author Nadav Bismuth
 */
public class ServerReCaptchaException extends ReCaptchaException {
    public ServerReCaptchaException(String message) {
        super(message);
    }

    public ServerReCaptchaException(Throwable cause) {
        super(cause);
    }

    public ServerReCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}
