package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/api/status")
    public Map<String, String> status() {
        return Map.of("status", "ok", "message", "Mainframe UI running");
    }

    @PostMapping("/api/echo")
    public Map<String, Object> echo(@RequestBody Map<String, Object> payload) {
        return Map.of("received", payload);
    }
}
