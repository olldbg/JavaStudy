package com.liminjun.test.gracefulShutdown.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() throws InterruptedException {
        TimeUnit.SECONDS.sleep(20);
        return "test";
    }

}
