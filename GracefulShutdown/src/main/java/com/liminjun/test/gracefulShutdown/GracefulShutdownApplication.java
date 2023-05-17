package com.liminjun.test.gracefulShutdown;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class GracefulShutdownApplication {

    @PreDestroy
    public void preDestroyLog(){
        log.info("spring pre destroy");
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            log.info("jvm pre destroy");
        }));

        SpringApplication.run(GracefulShutdownApplication.class, args);
    }

}