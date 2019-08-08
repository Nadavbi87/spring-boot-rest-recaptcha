
# Spring Boot Rest reCAPTCHA Starter  
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.bsmoot/rest-recaptcha-spring-boot-starter/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.bsmoot/rest-recaptcha-spring-boot-starter)  
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)  
  
Easy integration for Rest/Web API spring boot projects with Google's reCaptcha service.  
  
## Prerequisites  
  
* reCAPTCHA secret key. To get it go to the [reCAPTCHA Home Page](https://www.google.com/recaptcha/intro/index.html) and set up your reCAPTCHA.  
* The **spring-boot-starter-aop** dependency present on the classpath.  
* The project runs in a web environment e.g.: via a simple spring web starter.  
  
## Installing  
  
```xml  
<dependencies>
    ... 
    <dependency> 
        <groupId>com.bsmooth</groupId> 
	<artifactId>rest-recptcha-spring-boot-starter</artifactId> 
	<version>1.0.2</version> 
    </dependency> 	
    ...
</dependencies>  
  
```   
## How To Use  
  
### Simple usage  
  
If you do not want to customize the reCAPTCHA validation behaviour just use the `@RequiresCaptcha` annotation on top of the protected endpoint,  
And add your configuretion from application properties.  
  
At the moment the following properties can be configured:  
  #### Global Properties
| Name | Description | Default Value |  
| :---         |     :---      | :--- |  
| `recaptcha.enabled`| Disable/Enable the auto configuration | true |  
  
#### Service Properties  
  
Prefix - `recaptcha.service`  
  
| Service Properties| Description | Default Value |  
| :---         |     :---      | :--- |  
| `secret`| The secret key obtained from Google's reCAPTCHA | null |  
| `verify-url`| Google's reCAPTCHA verify endpoint| "https://www.google.com/recaptcha/api/siteverify" |  
| `connection-request-timeout` | The http client request time out | 2000 |  
| `connection-timeout` | The http client connection time out  | 2000 |  
| `socket-timeout`| The http client socket time out  | 2000 |  
  
#### Aspect Properties  
  
Prefix - `recaptcha.aspect`  
  
| Name | Description | Default Value |  
| :---         |     :---      | :--- |  
| `header-name`| The header name in which the client needs to send his ReCaptcha response | "g-recaptcha-response" |  
| `proxy-real-ip-header-name`| The header of the real client IP (e.g.: X-Forwarded-For) | null, By default the ```request.getRemoteAddr()``` will be used. |  
| `proxy-real-ip-header-delimiter` | Delimiter for multi value real client IP header (e.g.: \, ) | null |  
| `verified-ips` | List of verified IPs (',' delimited) that will pass automatically without captcha validation ( useful if you want to exclude dev/test machines )   | null |  
  
**Example**  	
  
Place the following lines into your `application.properties`  
```  
recaptcha.aspect.proxy-real-ip-header-name=X-Forwarded-For  
recaptcha.aspect.proxy-real-ip-header-delimiter=\,  
recaptcha.service.secret-key=${your-secret-key}  
recaptcha.aspect.verified-ips=127.0.0.1,10.0.0.2  
```   
Protected Endpoint  
```java  
@RequestMapping("/api")  
@RestController  
public class RestReCaptchaController {  
  
 @RequiresCaptcha 
 @GetMapping("/protected") 
 public boolean captchaProtectedPath() { return true; }
}  
```  
 Catch Exceptions and decide how to react.
 Exceptions:
 - `MissingReCaptchaException` - Bad Request.
 - `ClientReCaptchaException`&nbsp;&nbsp;&nbsp;- reCAPTCHA Client Error.
 - `ServerReCaptchaException`  &nbsp; - reCAPTCHA Server Error

 ```java
@ControllerAdvice  
@Order(Ordered.HIGHEST_PRECEDENCE)  
public class RestExceptionHandler {  
  
    @ExceptionHandler(ReCaptchaException.class)  
    protected ResponseEntity<Object> ReCaptchaException(ReCaptchaException ex, WebRequest request) {  
        HttpStatus status = HttpStatus.FORBIDDEN;  
        String message = ex.getMessage();  
        if(ex instanceof ServerReCaptchaException){  
            status = HttpStatus.INTERNAL_SERVER_ERROR;  
        }else if (ex instanceof MissingReCaptchaException){  
            status = HttpStatus.BAD_REQUEST;  
        }  
        return new ResponseEntity<>(message, status);  
    }  
}
 ```
  
That's all, you're good to go now, try to reach your protected endpoints.    
  
### Customize Error Handlers     
If you want custom error handlers for different scenarios, just define your own handler beans. At the moment the following callback handlers are supported:  
* `SuccessResponseHandler`: this handler will be invoked after a valid reCAPTCHA value has been sent.   
* `ErrorResponseHandler`: this handler will be invoked after an invalid reCAPTCHA value has been sent.  
 
  
**Example**  


```java  
@Configuration  
public class CustomHandlersConfiguration {  
    @Bean  
    ErrorResponseHandler customErrorResponseHandler() {  
        return new ErrorResponseHandler(){  
            AuditLogger auditLogger = ....
            Override  
            public void handle(ProceedingJoinPoint joinPoint, ReCaptchaResponse response, HttpServletRequest request) throws ReCaptchaException {
                ReCaptchaResponse.ErrorCode[] errorCodes = response.getErrorCodes();  
                String errorMessage = getFirstErrorMessage(errorCodes);
                get invkoed method, ip etc..
                ...
                auditLogger.write(event, method, ip, response, errorMessage...);
		
               if(response.hasServerError()){  
                    throw new ServerReCaptchaException(errorMessage);  
                }else if(response.hasClientError()){  
                    throw new ClientReCaptchaException(errorMessage);
                }			
            }  
        };  
    }  
  
    @Bean  
    SuccessResponseHandler customSuccessResponseHandler() {  
        return new SuccessResponseHandler() {
            AuditLogger auditLogger = ....  
            @Override  
            public void handle(ProceedingJoinPoint joinPoint, ReCaptchaResponse response, HttpServletRequest request) throws ReCaptchaException {  
            get invoked method, ip etc.. 
            ...
            auditLogger.write(event, method, ip, response...); 
            }  
        };  
    }
}      
```  
  
### Fully reCAPTCHA service custom behaviour  
If you want totally different behavior, 
 Set `recaptcha.enabled` to false, Implement `ReCaptchaService` interface  and  then define a bean with type `ReCaptchaService`.   
  
**Example**  
  
application.properties
```
recaptcha.enabled = false;
```
   
```java  
@Service
public class CustomReCaptchaServiceImpl implements ReCaptchaService {  
  
    private YourHttpClient httpClient;  
    private YourServiceProps serviceProps;  
  
    @Autowired  
    public ReCaptchaServiceImpl(YourHttpClient httpClient, YourServiceProps serviceProps) {  
        this.httpClient= httpClient;  
        this.serviceProps= serviceProps;  
    }  
  
    public ReCaptchaResponse validate(String reCaptchaResponse, String remoteIp) throws ReCaptchaException {  
        Your awesome implementation...  
    }
```  

## Author  
  
* **Nadav Bismuth** - [Nadavbi87](https://github.com/Nadavbi87)  
