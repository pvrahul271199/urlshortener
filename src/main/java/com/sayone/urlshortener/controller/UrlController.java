package com.sayone.urlshortener.controller;

import com.sayone.urlshortener.dto.request.ShortenRequest;
import com.sayone.urlshortener.dto.response.ShortenResponse;
import com.sayone.urlshortener.dto.response.StatsResponse;
import com.sayone.urlshortener.repository.UrlRepository;
import com.sayone.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        return ResponseEntity.ok(urlService.shortenUrl(request));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String originalUrl = urlService.resolveUrl(shortCode);

    }

    @GetMapping("/api/stats/{shortCode}")
    public ResponseEntity<StatsResponse> stats(@PathVariable String shortCode){
        return ResponseEntity.ok(urlService.getStats(shortCode));
    }

    @DeleteMapping("/api/links/{shortCode}")
    public ResponseEntity<Void> deleteLink(@PathVariable String shortCode){
        urlService.deleteUrl(shortCode);
        return ResponseEntity.noContent().build();
    }

}
