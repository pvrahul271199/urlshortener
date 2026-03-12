package com.sayone.urlshortener.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.ZonedDateTime;

@Data
public class ShortenRequest {
    @NotBlank @URL
    private String originalUrl;
    String customAlias;
    ZonedDateTime expiresAt;
}
