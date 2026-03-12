package com.sayone.urlshortener.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class StatsResponse {
    String shortCode;
    String originalUrl;
    long totalClickCount;
    ZonedDateTime createdAt;
    ZonedDateTime expiresAt;
    ZonedDateTime lastAccessedAt;
}
