package com.example.demologging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DemoLoggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoLoggingApplication.class, args);
        log.info("============ App Has Started ===========");
    }

}
