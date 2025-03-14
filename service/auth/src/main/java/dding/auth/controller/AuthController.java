package dding.auth.controller;

import dding.auth.auth.jwt.JwtTokenUtil;
import dding.auth.dto.request.JoinRequest;
import dding.auth.dto.request.LoginRequest;
import dding.auth.entity.Auth;
import dding.auth.service.AuthService;
import dding.auth.service.JwtRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtRedisService jwtRedisService;

    @Value("${JWT_KEY}")
    private String secretKey;

    // 유효시간 (단위: ms)
    private final long accessTokenExpireMs = 1000L * 60 * 60;       // 1시간
    private final long refreshTokenExpireMs = 1000L * 60 * 60 * 24; // 24시간

    /**
     * ✅회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody JoinRequest joinRequest) {
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "비밀번호가 일치하지 않습니다."
            ));
        }

        if (authService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이미 사용 중인 로그인 ID입니다."
            ));
        }

        if (authService.checkNicknameDuplicate(joinRequest.getNickname())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이미 사용 중인 닉네임입니다."
            ));
        }

        authService.join2(joinRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "회원가입 완료"
        ));
    }

    /**
     *  로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Auth user = authService.login(loginRequest);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다."
            ));
        }

        String accessToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, accessTokenExpireMs);
        String refreshToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, refreshTokenExpireMs);

        // Redis에 refreshToken 저장
        jwtRedisService.saveRefreshToken(user.getLoginId(), refreshToken, refreshTokenExpireMs);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("expiresIn", accessTokenExpireMs / 1000); // 초 단위

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "로그인 성공",
                "result", result
        ));
    }

    /**
     *  내 정보 조회 (JWT 기반)
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "토큰이 존재하지 않습니다."
            ));
        }

        String token = authHeader.substring(7);
        if (JwtTokenUtil.isExpired(token, secretKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Access Token이 만료되었습니다."
            ));
        }

        String loginId = JwtTokenUtil.getLoginId(token, secretKey);
        Auth user = authService.getLoginUserByLoginId(loginId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "사용자 정보를 찾을 수 없습니다."
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "사용자 정보 조회 성공",
                "result", Map.of(
                        "loginId", user.getLoginId(),
                        "nickname", user.getNickname(),
                        "role", user.getRole().name()
                )
        ));
    }

    /**
     *  Refresh Token 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestHeader("Authorization") String accessHeader,
            @RequestBody Map<String, String> body) {

        if (accessHeader == null || !accessHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Access Token이 없습니다."
            ));
        }

        String accessToken = accessHeader.substring(7);
        String loginId;

        try {
            loginId = JwtTokenUtil.getLoginId(accessToken, secretKey);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "토큰에서 사용자 정보 추출 실패"
            ));
        }

        String storedRefreshToken = jwtRedisService.getRefreshToken(loginId);
        String requestRefreshToken = body.get("refreshToken");

        if (storedRefreshToken == null || !storedRefreshToken.equals(requestRefreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "리프레시 토큰이 유효하지 않습니다. 다시 로그인 해주세요."
            ));
        }

        String newAccessToken = JwtTokenUtil.createToken(loginId, secretKey, accessTokenExpireMs);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Access Token 재발급 성공",
                "accessToken", newAccessToken
        ));
    }

    /**
     *  로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessHeader) {
        if (accessHeader == null || !accessHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Access Token이 없습니다."
            ));
        }

        String token = accessHeader.substring(7);
        String loginId = JwtTokenUtil.getLoginId(token, secretKey);
        long expiration = JwtTokenUtil.getRemainingTime(token, secretKey);

        jwtRedisService.addToBlacklist(token, expiration);
        jwtRedisService.deleteRefreshToken(loginId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "로그아웃 완료"
        ));
    }
}
