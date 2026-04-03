package org.example.moviesplatform.repository.cacheRepository;

import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@ConditionalOnBean(RedisTemplate.class)
public class UserCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Long ttl;

    public UserCacheRepository(RedisTemplate<String, Object> redisTemplate,
                               @Value("${cache.redis.user.ttl:600}") Long ttl) {
        this.redisTemplate = redisTemplate;
        this.ttl = ttl;
    }

    public UserEntity read(String name) {
        Object v = redisTemplate.opsForValue().get(name);
        return v instanceof UserEntity ? (UserEntity) v : null;
    }

    public void save(UserEntity userEntity) {
        redisTemplate.opsForValue().set(userEntity.getUsername(), userEntity, ttl, TimeUnit.SECONDS);
    }
}
