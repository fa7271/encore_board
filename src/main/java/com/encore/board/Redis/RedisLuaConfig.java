package com.encore.board.Redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisLuaConfig {
    @Bean
    public RedisScript<Long> IncrAndCopyScript() {
        Resource script = new ClassPathResource("scripts/incrAndCopy.lua");
        log.info("IncrAndCopyScript 등록 : {} ", script);
        return RedisScript.of(script, Long.class);
    }

    @Bean
    public RedisScript<Void> IncrScript() {
        Resource script = new ClassPathResource("scripts/incr.lua");
        log.info("IncrScript 등록 : {} ", script);
        return RedisScript.of(script);
    }
}
