package com.sayone.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(name="urls", indexes = @Index(columnList = "short_code"))
public class Url {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code", unique=true, length = 10)
    private String shortCode;

    @Column(name = "original_url", nullable = false, columnDefinition = "")
    private String originalUrl;

    @Column(name = "click_count")
    private Long clickCount = 0L;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @Column(name = "last_accessed_at")
    private ZonedDateTime lastAccessedAt;
}
