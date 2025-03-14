package dding.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtRedisService {

    private final String BLACKLIST_PREFIX = "blacklist:";
    private final String REFRESH_PREFIX = "refresh:";

    private final RedisTemplate<String, String> redisTemplate;

    // 로그아웃 토큰 블랙리스트 등록
    public void addToBlacklist(String accessToken, long expirationMillis) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + accessToken, "logout", expirationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken);
    }

    // 리프레시 토큰 저장
    public void saveRefreshToken(String loginId, String refreshToken, long expireMillis) {
        redisTemplate.opsForValue().set(REFRESH_PREFIX + loginId, refreshToken, expireMillis, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String loginId) {
        return redisTemplate.opsForValue().get(REFRESH_PREFIX + loginId);
    }

    public void deleteRefreshToken(String loginId) {
        redisTemplate.delete(REFRESH_PREFIX + loginId);
    }
}

