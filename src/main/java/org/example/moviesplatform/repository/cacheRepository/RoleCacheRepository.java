package org.example.moviesplatform.repository.cacheRepository;

import org.example.moviesplatform.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@ConditionalOnBean(RedisTemplate.class)
public class RoleCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Long ttl;

    public RoleCacheRepository(RedisTemplate<String, Object> redisTemplate,
                               @Value("${cache.redis.role.ttl:3600}") Long ttl) {
        this.redisTemplate = redisTemplate;
        this.ttl = ttl;
    }

    public Role read(String name) {
        Object v = redisTemplate.opsForValue().get(name);
        return v instanceof Role ? (Role) v : null;
    }

    public void save(Role role) {
        redisTemplate.opsForValue().set(role.getName(), role, ttl, TimeUnit.SECONDS);
    }
}
