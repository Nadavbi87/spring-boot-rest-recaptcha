package com.bsmoot.autoconfigure;

import com.bsmoot.exceptions.ReCaptchaException;
import com.bsmoot.properties.ReCaptchaProps;
import com.bsmoot.service.ReCaptchaResponse;
import com.bsmoot.service.ReCaptchaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OverrideReCaptchaServiceConfiguration {

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    ReCaptchaService overrideReCaptchaService(ReCaptchaProps reCaptchaProps){
        RestTemplate restTemplate = new RestTemplate();
        return new OverrideReCaptchaService(restTemplate, reCaptchaProps);
    }

    public static class OverrideReCaptchaService implements ReCaptchaService{
        private ReCaptchaProps reCaptchaProps;
        private RestTemplate restTemplate;

        public OverrideReCaptchaService (RestTemplate restTemplate, ReCaptchaProps reCaptchaProps){
            this.restTemplate = restTemplate;
            this.reCaptchaProps = reCaptchaProps;
        }

        @Override
        public ReCaptchaResponse validate(String reCaptchaResponse, String remoteIp) throws ReCaptchaException {
            return null;
        }
    }
}
