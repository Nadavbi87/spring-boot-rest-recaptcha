package com.bsmoot.exceptions;


/**
 * Invalid ReCaptcha service exception - Bad Request
 * @author Nadav Bismuth
 */
public class MissingReCaptchaException extends ClientReCaptchaException {
    public MissingReCaptchaException(String message) {
        super(message);
    }

    public MissingReCaptchaException(Throwable cause) {
        super(cause);
    }

    public MissingReCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}
