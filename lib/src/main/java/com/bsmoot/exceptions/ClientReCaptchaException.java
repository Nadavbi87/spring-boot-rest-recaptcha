package com.bsmoot.exceptions;

import com.bsmoot.service.ReCaptchaResponse;

/**
 * Client ReCaptcha exception
 * {@link ReCaptchaResponse#hasClientError()}
 * @author Nadav Bismuth
 */
public class ClientReCaptchaException extends ReCaptchaException {
    public ClientReCaptchaException(String message) {
        super(message);
    }

    public ClientReCaptchaException(Throwable cause) {
        super(cause);
    }

    public ClientReCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}
