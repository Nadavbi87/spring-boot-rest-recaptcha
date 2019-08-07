package com.bsmoot.service;

import com.bsmoot.constants.Constants;
import com.fasterxml.jackson.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represent the ReCaptcha response object
 * @author Nadav Bismuth
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        Constants.SUCCESS_PARAM_NAME,
        Constants.CHALLENGE_PARAM_NAME,
        Constants.HOSTNAME_PARAM_NAME,
        Constants.ERRORS_PARAM_NAME
})
public class ReCaptchaResponse {
    @JsonProperty(Constants.SUCCESS_PARAM_NAME)
    private boolean success;

    @JsonProperty(Constants.CHALLENGE_PARAM_NAME)
    private String challengeTs;

    @JsonProperty(Constants.HOSTNAME_PARAM_NAME)
    private String hostname;

    @JsonProperty(Constants.ERRORS_PARAM_NAME)
    private ErrorCode[] errorCodes;

    public ReCaptchaResponse() {
    }

    public ReCaptchaResponse(boolean success, ErrorCode[] errorCodes) {
        this.success = success;
        this.errorCodes = errorCodes;
    }

    @JsonIgnore
    public boolean hasClientError() {
        ErrorCode[] errors = getErrorCodes();
        if(errors == null) {
            return false;
        }

        for(ErrorCode error : errors) {
            switch(error) {
                case InvalidResponse:
                case MissingResponse:
                case TimeoutOrDuplicate:
                    return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean hasServerError() {
        ErrorCode[] errors = getErrorCodes();
        if(errors == null ) {
            return false;
        }

        for(ErrorCode error : errors) {
            switch(error) {
                case InvalidSecret:
                case MissingSecret:
                case BedRequest:
                case GeneralError:
                    return true;
            }
        }
        return false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getChallengeTs() {
        return challengeTs;
    }

    public void setChallengeTs(String challengeTs) {
        this.challengeTs = challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public ErrorCode[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(ErrorCode[] errorCodes) {
        this.errorCodes = errorCodes;
    }

    public enum ErrorCode {
        MissingSecret,
        InvalidSecret,
        MissingResponse,
        InvalidResponse,
        BedRequest,
        TimeoutOrDuplicate,
        GeneralError;

        private static Map<String, ErrorCode> errorsMap = new HashMap<String, ErrorCode>(6);

        static {
            errorsMap.put(Constants.ERROR_MISSING_SECRET,   MissingSecret);
            errorsMap.put(Constants.ERROR_INVALID_SECRET,   InvalidSecret);
            errorsMap.put(Constants.ERROR_MISSING_RESPONSE, MissingResponse);
            errorsMap.put(Constants.ERROR_INVALID_RESPONSE, InvalidResponse);
            errorsMap.put(Constants.ERROR_BED_REQUEST, BedRequest);
            errorsMap.put(Constants.ERROR_TIMEOUT_OR_DUPLICATE, TimeoutOrDuplicate);
            errorsMap.put(Constants.ERROR_GENERAL, GeneralError);
        }

        public String getErrorCodeKey(){
            return errorsMap
                    .entrySet()
                    .stream().filter(entry -> entry.getValue() == this)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
        }


        @JsonCreator
        public static ErrorCode forValue(String value) {
            return errorsMap.get(value.toLowerCase());
        }
    }
}
