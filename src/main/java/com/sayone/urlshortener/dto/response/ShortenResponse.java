package com.sayone.urlshortener.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class ShortenResponse {
    String shortUrl;
    String shortCode;
    ZonedDateTime expiresAt;
}
