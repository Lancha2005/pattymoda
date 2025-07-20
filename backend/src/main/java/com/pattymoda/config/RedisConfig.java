package com.pattymoda.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true", matchIfMissing = false)
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Cache específicos para tienda de ropa con TTL optimizado
        cacheConfigurations.put("productos", 
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("categorias", 
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2)));
        cacheConfigurations.put("marcas", 
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2)));
        cacheConfigurations.put("tallas", 
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(4)));
        cacheConfigurations.put("colores", 
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(4)));
        cacheConfigurations.put("inventario", 
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put("precios", 
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}