package auth.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import dding.auth.dto.request.JoinRequest;
import dding.auth.dto.request.LoginRequest;
import dding.auth.entity.Auth;
import dding.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static dding.auth.auth.jwt.JwtTokenUtil.createToken;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AuthRepository authRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        authRepository.deleteAll(); // 테스트마다 DB 초기화
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void testSignupSuccess() throws Exception {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setLoginId("testuser");
        joinRequest.setPassword("1234");
        joinRequest.setPasswordCheck("1234");
        joinRequest.setNickname("테스트유저");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원가입 완료"));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 불일치")
    void testSignupPasswordMismatch() throws Exception {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setLoginId("testuser");
        joinRequest.setPassword("1234");
        joinRequest.setPasswordCheck("wrong");
        joinRequest.setNickname("테스트유저");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("비밀번호가 일치하지 않습니다")));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void testLoginSuccess() throws Exception {
        // 먼저 회원가입 유저 미리 저장
        Auth user = Auth.builder()
                .loginId("testuser")
                .password(passwordEncoder.encode("1234"))
                .nickname("닉네임")
                .role(dding.auth.auth.UserRole.USER)
                .build();
        authRepository.save(user);

        LoginRequest loginRequest = new LoginRequest("testuser", "1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.accessToken").doesNotExist()) // accessToken은 result 안에 있음
                .andExpect(jsonPath("$.result.accessToken").exists());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void testLoginFail() throws Exception {
        LoginRequest loginRequest = new LoginRequest("nonexistent", "wrongpass");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("토큰 기반 사용자 정보 조회")
    void testGetUserInfo() throws Exception {
        // 유저 등록
        Auth user = Auth.builder()
                .loginId("testuser")
                .password(passwordEncoder.encode("1234"))
                .nickname("닉네임")
                .role(dding.auth.auth.UserRole.USER)
                .build();
        authRepository.save(user);

        // JWT 발급
        String secretKey = "my-secret-key-123123";
        String token = createToken(user.getLoginId(), secretKey, 1000 * 60 * 60);

        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.loginId").value("testuser"))
                .andExpect(jsonPath("$.result.nickname").value("닉네임"));
    }
}

