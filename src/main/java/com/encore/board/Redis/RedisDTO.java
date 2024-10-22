package com.encore.board.Redis;

import lombok.Data;

@Data
public class RedisDTO {
    private String key;
    private String value;

    public RedisDTO(String newkey, String valueOf) {
        this.key = newkey;
        this.value = valueOf;
    }

    @Override
    public String toString() {
        return "RedisDTO{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
