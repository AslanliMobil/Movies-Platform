package org.example.moviesplatform.repository.cacheRepository;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RoleCacheRepository {

    private static final RedisTemplate<String, Role> redisTemplate = null;

    @Value("${cache.redis.role.ttl}")
    static Long ttl;

    public static Role read(String name) {
        return redisTemplate.opsForValue().get(name);
    }

    public static void save(Role role) {
        redisTemplate.opsForValue().set(role.getName(), role, ttl, TimeUnit.SECONDS);
    }
}
