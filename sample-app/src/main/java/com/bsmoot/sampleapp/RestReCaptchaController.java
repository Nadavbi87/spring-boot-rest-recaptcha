package com.bsmoot.sampleapp;


import com.bsmoot.aspect.RequiresCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class RestReCaptchaController {

    @RequiresCaptcha
    @GetMapping("/protected")
    public boolean captchaProtectedPath(){
        return true;
    }
}

