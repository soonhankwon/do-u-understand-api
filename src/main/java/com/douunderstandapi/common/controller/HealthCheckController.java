package com.douunderstandapi.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/heathCheck")
    public String healthCheck() {
        return "OK";
    }
}
