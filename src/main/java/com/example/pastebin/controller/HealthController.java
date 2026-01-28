package com.example.pastebin.controller;

import com.example.pastebin.repository.PasteRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final PasteRepository repo;

    public HealthController(PasteRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/api/healthz")
    public Map<String, Boolean> health() {
        try {
            repo.count(); // DB ping
            return Map.of("ok", true);
        } catch (Exception e) {
            return Map.of("ok", false);
        }
    }
}
