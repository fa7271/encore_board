package com.encore.board.Redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    private final RedisLuaService redisLuaService;

    public RedisController(RedisLuaService redisLuaService) {
        this.redisLuaService = redisLuaService;
    }

    @GetMapping("/redisincr")
    public String redisincr(String key) {
        redisLuaService.incr("a");
        return "ok";
    }
    @GetMapping("/incrAndCopy")
    public RedisDTO incrAndCopy(String originkey, String newkey, Integer count) {
        return redisLuaService.incrAndCopy("a","b",3);

    }
}
