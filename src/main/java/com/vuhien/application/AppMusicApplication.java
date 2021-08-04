package com.vuhien.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class AppMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppMusicApplication.class, args);
    }

}