package com.bsmoot.autoconfigure;

import com.bsmoot.aspect.RestReCaptchaAspect;
import com.bsmoot.handlers.ErrorResponseHandler;
import com.bsmoot.handlers.SuccessResponseHandler;
import com.bsmoot.service.ReCaptchaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class RestReCaptchaAutoConfigurationTest {

    private final WebApplicationContextRunner webContextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(RestReCaptchaAutoConfiguration.class));

    private final ApplicationContextRunner nonWebContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(RestReCaptchaAutoConfiguration.class));


    @Test
    public void When_WebContextWithRequiredReCaptchaProps_Expect_ReCaptchaAspectExists() {
        this.webContextRunner
                .withPropertyValues("recaptcha.enabled=true",
                        "recaptcha.service.secret-key=secret-key",
                        "recaptcha.service.verify-url=https://www.google.com/recaptcha/api/siteverify",
                        "recaptcha.aspect.header-name=g-recaptcha-response")
                .run((context) ->{
                    RestReCaptchaAutoConfiguration authConfig = context.getBean(RestReCaptchaAutoConfiguration.class);
                    assertThat(context).hasSingleBean(RestReCaptchaAspect.class);
                    assertThat(context.getBean(RestReCaptchaAspect.class))
                            .isSameAs(authConfig.restReCaptchaAspect(
                                   authConfig.reCaptchaService(authConfig.reCaptchaProps()),
                                            authConfig.successResponseHandler(),
                                            authConfig.errorResponseHandler(),
                                            authConfig.reCaptchaAspectProps()));

                });

    }

    @Test
    public void When_NonWebContext_Expect_ReCaptchaAspectNoExists() {
        this.nonWebContextRunner
                .withPropertyValues("recaptcha.enabled=true",
                        "recaptcha.service.secret-key=secret-key",
                        "recaptcha.service.verify-url=https://www.google.com/recaptcha/api/siteverify")
                .run((context) -> assertThat(context).doesNotHaveBean(RestReCaptchaAspect.class));
    }

    @Test
    public void When_WebContextWithReCaptchaPropertyDisabled_Expect_ReCaptchaAspectNoExists() {
        this.webContextRunner
                .withPropertyValues("recaptcha.enabled=false",
                        "recaptcha.service.secret-key=secret-key",
                        "recaptcha.service.verify-url=https://www.google.com/recaptcha/api/siteverify")
                .run((context) -> assertThat(context).doesNotHaveBean(RestReCaptchaAspect.class));
    }

    @Test
    public void When_OverrideSuccessResponseHandlerBean_ExpectSingleBeanSameType() {
        this.webContextRunner
                .withPropertyValues("recaptcha.enabled=true",
                        "recaptcha.service.secret-key=secret-key",
                        "recaptcha.service.verify-url=https://www.google.com/recaptcha/api/siteverify")
                .withUserConfiguration(OverrideSuccessResponseHandlerConfiguration.class)
                .run((context) -> {
                    OverrideSuccessResponseHandlerConfiguration overrideConf =
                            context.getBean(OverrideSuccessResponseHandlerConfiguration.class);

                    assertThat(context).hasSingleBean(SuccessResponseHandler.class);
                    assertThat(context).getBean(SuccessResponseHandler.class)
                            .isSameAs(overrideConf.overrideSuccessResponseHandler())
                            .isExactlyInstanceOf(OverrideSuccessResponseHandlerConfiguration
                                    .OverrideSuccessResponseHandler.class);

                });
    }

    @Test
    public void When_OverrideErrorResponseHandlerBean_ExpectSingleBeanSameType() {
        this.webContextRunner
                .withPropertyValues("recaptcha.enabled=true",
                        "recaptcha.service.secret-key=secret-key",
                        "recaptcha.service.verify-url=https://www.google.com/recaptcha/api/siteverify")
                .withUserConfiguration(OverrideErrorResponseHandlerConfiguration.class)
                .run((context) -> {
                    OverrideErrorResponseHandlerConfiguration overrideConf =
                            context.getBean(OverrideErrorResponseHandlerConfiguration.class);

                    assertThat(context).hasSingleBean(ErrorResponseHandler.class);
                    assertThat(context).getBean(ErrorResponseHandler.class)
                            .isSameAs(overrideConf.overrideErrorResponseHandler())
                            .isExactlyInstanceOf(OverrideErrorResponseHandlerConfiguration.
                                    OverrideErrorResponseHandler.class);
                });
    }

    @Test
    public void When_OverrideReCaptchaServiceBean_ExpectSingleBeanSameType() {
        this.webContextRunner
                .withPropertyValues("recaptcha.enabled=true",
                        "recaptcha.service.secret-key=secret-key",
                        "recaptcha.service.verify-url=https://www.google.com/recaptcha/api/siteverify")
                .withUserConfiguration(OverrideReCaptchaServiceConfiguration.class)
                .run((context) -> {
                    OverrideReCaptchaServiceConfiguration overrideConf =
                            context.getBean(OverrideReCaptchaServiceConfiguration.class);
                    RestReCaptchaAutoConfiguration authConfig = context.getBean(RestReCaptchaAutoConfiguration.class);

                    assertThat(context).hasSingleBean(ReCaptchaService.class);
                    assertThat(context).getBean(ReCaptchaService.class)
                            .isSameAs(overrideConf.overrideReCaptchaService(authConfig.reCaptchaProps()))
                            .isExactlyInstanceOf(OverrideReCaptchaServiceConfiguration.OverrideReCaptchaService.class);
                });
    }
}