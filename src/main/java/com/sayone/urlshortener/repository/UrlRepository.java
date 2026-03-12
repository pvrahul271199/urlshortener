package com.sayone.urlshortener.repository;

import com.sayone.urlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);
    void deleteByExpiresAtBefore(ZonedDateTime now);
}
