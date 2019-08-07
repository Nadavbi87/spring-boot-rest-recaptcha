package com.bsmoot.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Static utility for IP addresses
 * @author Nadav Bismuth
 */
public class IPUtil {
    /**
     * Retrieve the client IP from remote address or proxy ip header.
     * @param request - {@link HttpServletRequest}
     * @param proxyRealIPHeaderName - The proxy real ip header name.
     * @param proxyRealIPHeaderDelimiter - The proxy real ip header delimiter if exists.
     * @return clientIP - String or null
     */
    public static String getClientIP(HttpServletRequest request,
                                     String proxyRealIPHeaderName,
                                     String proxyRealIPHeaderDelimiter){
        String clientIP = request.getHeader(proxyRealIPHeaderName);

        if(!StringUtils.isEmpty(clientIP)){
            if(!StringUtils.isEmpty(proxyRealIPHeaderDelimiter)){
                String[] ips = clientIP.split(proxyRealIPHeaderDelimiter);
                if(ips.length > 0){
                    clientIP = ips[0].trim();
                }
            }
        }

        if(clientIP == null){
            clientIP= request.getRemoteAddr();
        }

        return clientIP;
    }
}
