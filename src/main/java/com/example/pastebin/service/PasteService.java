package com.example.pastebin.service;

import com.example.pastebin.Exception.PasteUnavailableException;
import com.example.pastebin.entity.Paste;
import com.example.pastebin.repository.PasteRepository;

import org.springframework.http.HttpStatus;


import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PasteService {

    private final PasteRepository repo;

    public PasteService(PasteRepository repo) {
        this.repo = repo;
    }

    // CREATE
    public Paste createPaste(String content, Integer ttl, Integer maxViews) {
        Paste p = new Paste();
        p.setId(UUID.randomUUID().toString());
        p.setContent(content);
        p.setMaxViews(maxViews);

        if (ttl != null) {
            p.setExpiresAt(Instant.now().plusSeconds(ttl));
        }

        return repo.save(p);
    }

    // ✅ API FETCH (COUNTS VIEW)
    @Transactional
    public Paste fetch(String id, Instant now) {

        Paste p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paste not found"));

        if (p.getExpiresAt() != null && now.isAfter(p.getExpiresAt())) {
            throw new RuntimeException("Paste expired");
        }

        if (p.getMaxViews() != null && p.getViews() >= p.getMaxViews()) {
            throw new RuntimeException("View limit exceeded");
        }

        p.setViews(p.getViews() + 1);
        return repo.save(p);
    }

    // ✅ HTML VIEW (DOES NOT COUNT & DOES NOT BLOCK BY VIEW LIMIT)
    public Paste viewPaste(String id, Instant now) {

        Paste p = repo.findById(id)
                .orElseThrow(PasteUnavailableException::new);

        if (p.getExpiresAt() != null && now.isAfter(p.getExpiresAt())) {
            throw new PasteUnavailableException();
        }

        if (p.getMaxViews() != null && p.getViews() >= p.getMaxViews()) {
            throw new PasteUnavailableException();
        }

        return p;
    }


}
