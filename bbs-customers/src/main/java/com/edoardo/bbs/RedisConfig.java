package com.edoardo.bbs;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration () {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues();
    }\

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer () {
        return (builder) ->
            builder
                .withCacheConfiguration("customers", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
                .withCacheConfiguration("addresses", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(60)));
    }

}
