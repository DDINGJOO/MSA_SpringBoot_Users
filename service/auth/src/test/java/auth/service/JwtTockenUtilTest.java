package auth.service;

import dding.auth.auth.jwt.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenUtilTest {

    private final String secretKey = "my-secret-key-123123";

    @Test
    @DisplayName("JWT 토큰 생성 및 검증")
    void testCreateAndValidateToken() {
        String token = JwtTokenUtil.createToken("testuser", secretKey, 1000L * 60 * 60);
        assertThat(token).isNotEmpty();

        String loginId = JwtTokenUtil.getLoginId(token, secretKey);
        assertThat(loginId).isEqualTo("testuser");
    }

    @Test
    @DisplayName("JWT 토큰 만료 체크")
    void testTokenExpiration() {
        String expiredToken = JwtTokenUtil.createToken("testuser", secretKey, -1000L);
        boolean isExpired = JwtTokenUtil.isExpired(expiredToken, secretKey);

        assertThat(isExpired).isTrue();
    }
}