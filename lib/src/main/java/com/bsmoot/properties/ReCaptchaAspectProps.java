package com.bsmoot.properties;


import java.util.HashSet;
import java.util.Set;

/**
 * This class contains the properties for the ReCaptcha Aspect.
 * @author Nadav Bismuth
 */
public class ReCaptchaAspectProps {
    /**
     * The header name in which the client needs to send his ReCaptcha response
     */
    private String headerName = "g-recaptcha-response";

    /**
     * The proxy header name of real client ip
     * For example : X-Forwarded-For
     */
    private String proxyRealIPHeaderName;

    /**
     * The proxy header real client ip delimiter,
     * Use if the header contains multi line ips separated by the specific delimiter,
     * The client ip should be the first element
     * For example: "," -> client, proxy1, proxy2
     */
    private String proxyRealIPHeaderDelimiter;
    /**
     * List of specific Ips to be auto verified without the need of the ReCaptcha validation.
     * Can be used for testing.
     */
    private Set<String> verifiedIps = new HashSet<>();

    public ReCaptchaAspectProps() {}

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getProxyRealIPHeaderName() {
        return proxyRealIPHeaderName;
    }

    public void setProxyRealIPHeaderName(String proxyRealIPHeaderName) {
        this.proxyRealIPHeaderName = proxyRealIPHeaderName;
    }

    public String getProxyRealIPHeaderDelimiter() {
        return proxyRealIPHeaderDelimiter;
    }

    public void setProxyRealIPHeaderDelimiter(String proxyRealIPHeaderDelimiter) {
        this.proxyRealIPHeaderDelimiter = proxyRealIPHeaderDelimiter;
    }

    public Set<String> getVerifiedIps() {
        return verifiedIps;
    }

    public void setVerifiedIps(Set<String> verifiedIps) {
        this.verifiedIps = verifiedIps;
    }
}
