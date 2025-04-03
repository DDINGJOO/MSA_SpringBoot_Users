package dding.auth.controller.kakao;

import dding.auth.auth.jwt.JwtTokenUtil;
import dding.auth.dto.kakao.KakaoRequestDto;
import dding.auth.dto.kakao.KakaoUserInfoResponseDto;
import dding.auth.entity.Auth;
import dding.auth.service.AuthService;
import dding.auth.service.JwtRedisService;
import dding.auth.service.kako.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final AuthService authService;
    private final JwtRedisService jwtRedisService;


    private String secretKey="MY-123123";

    // 유효시간 (단위: ms)
    private final long accessTokenExpireMs = 1000L * 60 * 30;       //30분
    private final long refreshTokenExpireMs = 1000L * 60 * 60 * 24; // 24시간







    @GetMapping("/callback")
    public ResponseEntity<?> callback(
            @RequestParam("code") String code){


        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoAccessToken);
        Auth user;

        //이전에 가입 완료라면,
        if(authService.checkFirst("kakao",userInfo.getId().toString()))
        {
             user = authService.getAuthByProviderProviderId("kakao",userInfo.getId().toString());
        }
        else {
            user = authService.signupSocial("kakao",userInfo.getId().toString());
        }



        String myAccessToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, accessTokenExpireMs);
        String myRefreshToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, refreshTokenExpireMs);

        // Redis에 refreshToken 저장
        jwtRedisService.saveRefreshToken(user.getLoginId(), myRefreshToken, refreshTokenExpireMs);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", myAccessToken);
        result.put("refreshToken", myRefreshToken);
        result.put("expiresIn", accessTokenExpireMs / 1000); // 초 단위
        result.put("loginId", user.getLoginId());
        result.put("userId", user.getId());
        result.put("role", user.getRole().name());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "로그인 성공",
                "result", result
        ));

    }
}
