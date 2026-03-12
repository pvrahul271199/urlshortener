package com.sayone.urlshortener.service;

import com.sayone.urlshortener.cache.RedisCacheService;
import com.sayone.urlshortener.dto.request.ShortenRequest;
import com.sayone.urlshortener.dto.response.ShortenResponse;
import com.sayone.urlshortener.dto.response.StatsResponse;
import com.sayone.urlshortener.entity.Url;
import com.sayone.urlshortener.exception.UrlShortenerException;
import com.sayone.urlshortener.repository.UrlRepository;
import com.sayone.urlshortener.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final Base62Encoder encoder;
    private final RedisCacheService redisCacheService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.default-ttl-days}")
    private int defaultTtl;

    public ShortenResponse shortenUrl(ShortenRequest request){
        Url url = new Url();
        url.setOriginalUrl(request.getOriginalUrl());
        url.setCreatedAt(ZonedDateTime.now());
        url.setExpiresAt(request.getExpiresAt() != null
            ? request.getExpiresAt() : ZonedDateTime.now().plusDays(defaultTtl)
        );

        // if custom alias present
        if(request.getCustomAlias() != null){
            if(urlRepository.findByShortCode(request.getCustomAlias()).isPresent()){
                throw new UrlShortenerException("Alias " + request.getCustomAlias() + " is already taken");
            }
            url.setShortCode(request.getCustomAlias());
        }

        Url savedUrl = urlRepository.save(url);

        //create base62 if custom alias is not present
        if(request.getCustomAlias() == null){
            savedUrl.setShortCode(encoder.encode(savedUrl.getId()));
            savedUrl = urlRepository.save(savedUrl);
        }

        //put it in redis cache
        redisCacheService.put(savedUrl.getShortCode(), savedUrl.getOriginalUrl(), savedUrl.getExpiresAt());

        return ShortenResponse.builder()
                .shortUrl(baseUrl + "/" + savedUrl.getShortCode())
                .shortCode(savedUrl.getShortCode())
                .expiresAt(savedUrl.getExpiresAt())
                .build();

    }

    public String resolveUrl(String shortCode){

        //check redis first
        String cached = redisCacheService.get(shortCode);
        if(cached != null){
            incrementClick(shortCode);
            return cached;
        }

        // if not present in cache hit db

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlShortenerException("Short code " + shortCode + "not found"));

        if(url.getExpiresAt() != null &&  url.getExpiresAt().isBefore(ZonedDateTime.now())){
            throw new UrlShortenerException("Link for " + shortCode + "expired");
        }

        // add values to cache

        redisCacheService.put(shortCode, url.getOriginalUrl(), url.getExpiresAt());

        return url.getOriginalUrl();
    }

    @Async
    public void incrementClick(String shortCode){
        urlRepository.findByShortCode(shortCode).ifPresent(
                url -> {
                    url.setClickCount(url.getClickCount() + 1);
                    url.setLastAccessedAt(ZonedDateTime.now());
                    urlRepository.save(url);
                }
        );
    }

    public StatsResponse getStats(String shortCode) {
        Url link = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlShortenerException("Stats for " + shortCode + "not found"));


        return StatsResponse.builder()
                .shortCode(shortCode)
                .totalClickCount(link.getClickCount())
                .originalUrl(link.getOriginalUrl())
                .createdAt(link.getCreatedAt())
                .expiresAt(link.getExpiresAt())
                .lastAccessedAt(link.getLastAccessedAt())
                .build();

    }

    public void deleteUrl(String shortCode) {

        Url link = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlShortenerException("Link for " + shortCode + "not found"));

        urlRepository.delete(link);

        redisCacheService.evict(link.getShortCode());

    }
}
