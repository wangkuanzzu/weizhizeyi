package com.example.filedemo.property;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(value = "caching")
@EnableCaching
@Data
public class CacheConfiguration {
    @Data
    public static class CacheSpec {
        private Integer timeout;
        private Integer maxSize = 1024;
        private Integer expireMode = ExpireMode.EXPIRE_AFTER_ACCESS.code;
    }
    private Map<String, CacheSpec> specs;
    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        SimpleCacheManager manager = new SimpleCacheManager();
        if (specs != null) {
            List<CaffeineCache> caches =
                    specs.entrySet().stream()
                            .map(entry -> buildCache(entry.getKey(), entry.getValue(), ticker))
                            .collect(Collectors.toList());
            manager.setCaches(caches);
        }
        return manager;
    }
    private CaffeineCache buildCache(String name, CacheSpec cacheSpec, Ticker ticker) {
        final Caffeine<Object, Object> caffeineBuilder
                = cacheSpec.expireMode == ExpireMode.EXPIRE_AFTER_ACCESS.code ?
        Caffeine.newBuilder()
                .expireAfterAccess(cacheSpec.getTimeout(), TimeUnit.SECONDS)
                .maximumSize(cacheSpec.getMaxSize())
                .ticker(ticker) :
                Caffeine.newBuilder()
                .expireAfterWrite(cacheSpec.getTimeout(), TimeUnit.SECONDS)
                .maximumSize(cacheSpec.getMaxSize())
                .ticker(ticker);
        return new CaffeineCache(name, caffeineBuilder.build());
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

    enum ExpireMode {
        EXPIRE_AFTER_WRITE(0),
        EXPIRE_AFTER_ACCESS(1);
        int code;
        ExpireMode(int code) {
            this.code = code;
        }
    }
}
