package com.sayone.urlshortener.cleanup;

import com.sayone.urlshortener.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@Slf4j
public class ExpiryCleanupScheduler {

    private final UrlRepository urlRepository;

    public ExpiryCleanupScheduler(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Scheduled(cron = "0 0 * * *")
    public void cleanExpiredUrls(){
        urlRepository.deleteByExpiresAtBefore(ZonedDateTime.now());
        log.info("Removed expired short URL's");
    }
}
