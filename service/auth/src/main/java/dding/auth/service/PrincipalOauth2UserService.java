package dding.auth.service;


import dding.auth.auth.Oauth2.GoogleUserInfo;
import dding.auth.auth.Oauth2.KakaoUserInfo;
import dding.auth.auth.Oauth2.OAuth2UserInfo;
import dding.auth.auth.UserRole;
import dding.auth.auth.principal.PrincipalDetails;
import dding.auth.entity.Auth;
import dding.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pk.ULID;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final AuthRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo( oAuth2User.getAttributes() );
        } else if(provider.equals("kakao")) {
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo( (Map)oAuth2User.getAttributes() );
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String loginId = provider + "_" + providerId;
        String nickname = oAuth2UserInfo.getName();


        Optional<Auth> optionalUser = userRepository.findByLoginId(loginId);
        Auth user = null;

        if(optionalUser.isEmpty()) {
            user = Auth.builder()
                    .id(new ULID().generatedKey())
                    .loginId(loginId)
                    .nickname(nickname)
                    .provider(provider)
                    .providerId(providerId)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}