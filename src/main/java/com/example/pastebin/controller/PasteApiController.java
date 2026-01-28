package com.example.pastebin.controller;

import com.example.pastebin.entity.Paste;
import com.example.pastebin.service.PasteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/pastes", produces = "application/json")
public class PasteApiController {

    private final PasteService service;

    public PasteApiController(PasteService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, Object> body) {

        String content = (String) body.get("content");
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid content");
        }

        Integer ttl = body.get("ttl_seconds") == null ? null : (Integer) body.get("ttl_seconds");
        Integer maxViews = body.get("max_views") == null ? null : (Integer) body.get("max_views");

        Paste p = service.createPaste(content, ttl, maxViews);

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        return Map.of(
                "id", p.getId(),
                "url", baseUrl + "/p/" + p.getId()
        );
    }

    // FETCH
    @GetMapping("/{id}")
    public Map<String, Object> fetch(@PathVariable String id, HttpServletRequest req) {

        Instant now = getNow(req);
        Paste p = service.fetch(id, now);

        return Map.of(
                "content", p.getContent(),
                "remaining_views",
                p.getMaxViews() == null ? null : (p.getMaxViews() - p.getViews()),
                "expires_at", p.getExpiresAt()
        );
    }

    private Instant getNow(HttpServletRequest req) {

        // Normal mode
        if (!"1".equals(System.getenv("TEST_MODE"))) {
            return Instant.now();
        }

        // TEST_MODE = 1
        String h = req.getHeader("x-test-now-ms");

        if (h == null || h.isBlank()) {
            return Instant.now(); // fallback safely
        }

        try {
            return Instant.ofEpochMilli(Long.parseLong(h));
        } catch (NumberFormatException e) {
            return Instant.now(); // NEVER throw 500
        }
    }

}
