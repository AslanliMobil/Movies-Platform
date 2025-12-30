package org.example.moviesplatform.repository.cacheRepository;
import org.example.moviesplatform.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private static final RedisTemplate<String, User> redisTemplate = null;

    @Value("${cache.redis.user.ttl}")
    static Long ttl;

    public static User read(String name) {
        return redisTemplate.opsForValue().get(name);
    }

    public static void save(User user) {
        redisTemplate.opsForValue().set(user.getUsername(), user, ttl, TimeUnit.SECONDS);
    }

}
