package org.example.moviesplatform.repository.cacheRepository;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private static final RedisTemplate<String, UserEntity> redisTemplate = null;

    @Value("${cache.redis.user.ttl}")
    static Long ttl;

    public static UserEntity read(String name) {
        return redisTemplate.opsForValue().get(name);
    }

    public static void save(UserEntity userEntity) {
        redisTemplate.opsForValue().set(userEntity.getUsername(), userEntity, ttl, TimeUnit.SECONDS);
    }

}
