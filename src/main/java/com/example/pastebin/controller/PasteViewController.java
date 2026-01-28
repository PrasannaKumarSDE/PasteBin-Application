package com.example.pastebin.controller;

import com.example.pastebin.Exception.PasteUnavailableException;
import com.example.pastebin.entity.Paste;

import com.example.pastebin.service.PasteService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Controller
public class PasteViewController {

    private final PasteService service;

    public PasteViewController(PasteService service) {
        this.service = service;
    }

    @GetMapping("/p/{id}")
    public String view(@PathVariable String id, Model model) {
        Paste p = service.viewPaste(id, Instant.now());
        model.addAttribute("content", p.getContent());
        return "paste";
    }

    @ExceptionHandler(PasteUnavailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound() {
        return "error";
    }
}
