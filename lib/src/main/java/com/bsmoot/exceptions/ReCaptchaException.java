package com.bsmoot.exceptions;

/**
 * Generic ReCaptcha exception
 * @author Nadav Bismuth
 */
public class ReCaptchaException extends Exception {

    public ReCaptchaException(String message) {
        super(message);
    }

    public ReCaptchaException(Throwable cause) {
        super(cause);
    }

    public ReCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}
