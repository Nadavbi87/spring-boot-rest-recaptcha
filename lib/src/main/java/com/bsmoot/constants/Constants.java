package com.bsmoot.constants;

/**
 * Application Constants interface
 * @author Nadav Bismuth
 */
public interface Constants {
    String SUCCESS_PARAM_NAME           = "success";
    String RQUEST_RESPONSE_PARAM_NAME   = "response";
    String RQUEST_SECRET_PARAM_NAME     = "secret";
    String RQUEST_REMOTE_IP_PARAM_NAME  = "remoteip";
    String CHALLENGE_PARAM_NAME         = "challenge_ts";
    String HOSTNAME_PARAM_NAME          = "hostname";
    String ERRORS_PARAM_NAME            = "error-codes";
    String ERROR_MISSING_SECRET         = "missing-input-secret";
    String ERROR_INVALID_SECRET         = "invalid-input-secret";
    String ERROR_MISSING_RESPONSE       = "missing-input-response";
    String ERROR_INVALID_RESPONSE       = "invalid-input-response";
    String ERROR_BED_REQUEST            = "bad-request";
    String ERROR_TIMEOUT_OR_DUPLICATE   = "timeout-or-duplicate";
    String ERROR_GENERAL                = "general-error";
}
