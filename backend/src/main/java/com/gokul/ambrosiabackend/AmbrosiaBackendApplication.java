package com.gokul.ambrosiabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AmbrosiaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmbrosiaBackendApplication.class, args);
    }

}
