package com.m_takahisa.taskpulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Map<String, String> sayHello() {
        // Java 21 の型推論(var)や、モダンな Map 作成を利用
        var response = Map.of(
                "message", "Hello from TaskPulse!",
                "status", "UP",
                "java_version", System.getProperty("java.version")
        );
        return response;
    }
}
