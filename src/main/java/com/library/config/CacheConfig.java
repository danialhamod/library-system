package com.library.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

@Configuration
@EnableCaching
public class CacheConfig {

    // In-memory cache
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("books", "patrons");
    }
}