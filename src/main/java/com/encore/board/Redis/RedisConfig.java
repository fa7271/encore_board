package com.encore.board.Redis;


import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

import static org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;

@Configuration
@EnableRedisRepositories
@Slf4j
public class RedisConfig {
    //    yml 안에 있는 정보를 프로그램 안으로 가져온다.
    @Value("${spring.redis.host}")
    public String host;
    @Value("${spring.redis.port}")
    public int port;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        return redisTemplate;
    }
    @Bean
    public CacheManager cacheManager() {
//        RedisCacheManager 생성
        RedisCacheManagerBuilder builder = RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
//        default로 설정을 한다.
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
//                키와 밸류 값 직렬화 방식을 정한다.
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//                cache: 이름으로 시작하고, 시간 30분
                .prefixCacheNameWith("cache:")
                .entryTtl(Duration.ofMinutes(30));
        builder.cacheDefaults(configuration);

        return builder.build();
    }

    @Bean
    public RedissonClient redissonClient(){
        log.info("RedissonClient 등록");
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        log.info("userconfig : {}", config);
        return Redisson.create(config);
    }
}
