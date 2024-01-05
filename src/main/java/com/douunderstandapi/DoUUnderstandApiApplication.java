package com.douunderstandapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DoUUnderstandApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoUUnderstandApiApplication.class, args);
    }

}
