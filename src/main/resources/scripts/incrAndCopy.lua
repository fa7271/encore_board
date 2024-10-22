-- keys 첫번째꺼 ARGV[1] 만큼올래
redis.call('INCRBY', KEYS[1], ARGV[1])
-- 올린가 value에 저장
local value = redis.call('GET', KEYS[1])
-- key[2] 에 복사
redis.call('SET', KEYS[2], value)
-- 출력위해 리턴
return tonumber(value)