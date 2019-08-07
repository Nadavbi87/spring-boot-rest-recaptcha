package com.bsmoot.properties;

/**
 * This class contains the properties for the ReCaptcha Service.
 * @author Nadav Bismuth
 */
public class ReCaptchaProps {
    /**
     * The secret key generated by Google's ReCaptcha service and used for validating user captcha response.
     */
    private String secretKey;
    /**
     * Google's ReCaptcha service verify url
     */
    private String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";
    /**
     * The rest client connection request timeout
     */
    private int connectionRequestTimeout = 2000;
    /**
     * The rest client connection timeout
     */
    private int connectionTimeout = 2000;
    /**
     * The rest client socket timeout
     */
    private int socketTimeout = 2000;

    public ReCaptchaProps() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}