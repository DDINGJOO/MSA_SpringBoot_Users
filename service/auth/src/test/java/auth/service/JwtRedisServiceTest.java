package auth.service;



import dding.auth.service.JwtRedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class JwtRedisServiceTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtRedisService jwtRedisService;

    @BeforeEach
    void setup() {
        redisTemplate.delete("test:refreshToken");
        redisTemplate.delete("test:blacklist");
    }

    @Test
    @DisplayName("리프레시 토큰 저장 및 조회")
    void testSaveAndGetRefreshToken() {
        jwtRedisService.saveRefreshToken("testuser", "refresh-token-1234", 60000);

        String refreshToken = jwtRedisService.getRefreshToken("testuser");

        assertThat(refreshToken).isEqualTo("refresh-token-1234");
    }

    @Test
    @DisplayName("블랙리스트 토큰 추가 및 확인")
    void testBlacklistToken() {
        jwtRedisService.addToBlacklist("blacklisted-token", 60000);

        boolean isBlacklisted = jwtRedisService.isBlacklisted("blacklisted-token");

        assertThat(isBlacklisted).isTrue();
    }

    @Test
    @DisplayName("블랙리스트 토큰이 만료되면 제거됨")
    void testBlacklistTokenExpiration() throws InterruptedException {
        jwtRedisService.addToBlacklist("temp-token", 2000); // 2초 후 만료

        TimeUnit.MILLISECONDS.sleep(2500); // 2.5초 대기

        boolean isBlacklisted = jwtRedisService.isBlacklisted("temp-token");

        assertThat(isBlacklisted).isFalse(); // 자동 삭제되어야 함
    }
}