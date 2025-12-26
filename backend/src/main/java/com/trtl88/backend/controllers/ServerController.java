package com.trtl88.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ServerController {

    private long serverStart;

    @PostConstruct
    public void init() {
        this.serverStart = System.currentTimeMillis();
    }

    @GetMapping("/api/server/info")
    public Map<String, Object> info() {
        Map<String, Object> m = new HashMap<>();
        m.put("serverStart", serverStart);
        return m;
    }
}
