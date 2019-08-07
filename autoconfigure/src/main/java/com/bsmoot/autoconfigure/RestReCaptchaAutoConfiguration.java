package com.bsmoot.autoconfigure;

import com.bsmoot.aspect.RestReCaptchaAspect;
import com.bsmoot.handlers.DefaultErrorResponseHandler;
import com.bsmoot.handlers.DefaultSuccessResponseHandler;
import com.bsmoot.handlers.ErrorResponseHandler;
import com.bsmoot.handlers.SuccessResponseHandler;
import com.bsmoot.properties.ReCaptchaAspectProps;
import com.bsmoot.properties.ReCaptchaProps;
import com.bsmoot.service.ReCaptchaService;
import com.bsmoot.service.ReCaptchaServiceImpl;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.Advice;
import org.aspectj.weaver.AnnotatedElement;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * The auto configuration class for rest ReCaptcha starter for Spring Boot.
 * The class will be trigger only if <b>recaptcha.enabled</b> property value is other then true.
 * By default he value is set to true.
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(value = WebMvcAutoConfiguration.class)
@ConditionalOnClass({EnableAspectJAutoProxy.class, Aspect.class, Advice.class, AnnotatedElement.class})
@ConditionalOnProperty(prefix = "recaptcha", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties
public class RestReCaptchaAutoConfiguration {

    /**
     * Create the ReCaptcha aspect property file object
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix="recaptcha.aspect")
    public ReCaptchaAspectProps reCaptchaAspectProps(){
        return new ReCaptchaAspectProps();
    }
    /**
     * Create the ReCaptcha service property file object
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix="recaptcha.service")
    public ReCaptchaProps reCaptchaProps(){
        return new ReCaptchaProps();
    }
    /**
     * Error handler for handle invalid recaptcha response
     * Can be overridden by creating custom bean from {@link ErrorResponseHandler} type.
     * @return - The error response handler
     */
    @Bean
    @ConditionalOnMissingBean
    public ErrorResponseHandler errorResponseHandler(){
        return new DefaultErrorResponseHandler();
    }
    /**
     * Success handler for handle valid recaptcha response
     * Can be overridden by creating custom bean from {@link SuccessResponseHandler} type.
     * @return - The success response handler
     */
    @Bean
    @ConditionalOnMissingBean
    public SuccessResponseHandler successResponseHandler(){
        return new DefaultSuccessResponseHandler();
    }
    /**
     * The ReCaptcha service - used to validate the Google ReCaptcha response.
     * Can be overridden by creating custom bean from {@link ReCaptchaService} type.
     * @param reCaptchaProps - The ReCaptcha service properties
     * @return - The ReCaptcha service.
     */
    @Bean
    @ConditionalOnMissingBean
    public ReCaptchaService reCaptchaService(ReCaptchaProps reCaptchaProps){
        RestTemplate restTemplate = getRestTemplate(reCaptchaProps);
        return new ReCaptchaServiceImpl(restTemplate, reCaptchaProps);
    }
    /**
     * The Rest ReCaptcha Aspect which will be used to validate the ReCaptcha protected endpoint.
     * @param reCaptchaService
     * @param successResponseHandler - {@link SuccessResponseHandler}
     * @param errorResponseHandler - {@link ErrorResponseHandler}
     * @param reCaptchaAspectProps - {@link ReCaptchaAspectProps}
     * @return - {@link RestReCaptchaAspect}
     */
    @Bean
    public RestReCaptchaAspect restReCaptchaAspect(ReCaptchaService reCaptchaService,
                                            SuccessResponseHandler successResponseHandler,
                                            ErrorResponseHandler errorResponseHandler,
                                            ReCaptchaAspectProps reCaptchaAspectProps){
        return new RestReCaptchaAspect(reCaptchaService,
                successResponseHandler,
                errorResponseHandler,
                reCaptchaAspectProps
        );
    }
    /**
     * Create request configuration for the ReCaptcha service rest client
     * @param reCaptchaProps - {@link ReCaptchaProps}
     * @return - {@link RequestConfig}
     */
    public RequestConfig getRequestConfig(ReCaptchaProps reCaptchaProps) {
        RequestConfig result = RequestConfig.custom()
                .setConnectionRequestTimeout(reCaptchaProps.getConnectionRequestTimeout())
                .setConnectTimeout(reCaptchaProps.getConnectionTimeout())
                .setSocketTimeout(reCaptchaProps.getSocketTimeout())
                .build();
        return result;
    }
    /**
     * Create http client for the ReCaptcha service rest client
     * With the request config object as the default request configuration
     * @param requestConfig - {@link RequestConfig}
     * @return -  {@link CloseableHttpClient}
     */
    public CloseableHttpClient getHttpClient(RequestConfig requestConfig) {
        CloseableHttpClient result = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(requestConfig)
                .build();
        return result;
    }
    /**
     * Create the rest template client for the ReCaptcha service
     * @param reCaptchaProps - {@link ReCaptchaProps}
     * @return - {@link RestTemplate}
     */
    public RestTemplate getRestTemplate(ReCaptchaProps reCaptchaProps) {
        RequestConfig requestConfig = getRequestConfig(reCaptchaProps);
        HttpClient httpClient = getHttpClient(requestConfig);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }
}
