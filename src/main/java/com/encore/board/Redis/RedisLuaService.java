package com.encore.board.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisLuaService{
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> incrAndCopyScript;
    private final RedisScript<Void> incrScript;

    public void incr(String key) {
        redisTemplate.execute(incrScript, List.of(key));
    }
    public RedisDTO incrAndCopy(String originkey, String newkey, Integer count) {
        Long value = redisTemplate.execute(incrAndCopyScript, List.of(originkey, newkey), String.valueOf(count));
        return new RedisDTO(newkey, String.valueOf(value));
    }
}
