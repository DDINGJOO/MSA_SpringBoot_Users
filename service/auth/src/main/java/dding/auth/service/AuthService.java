package dding.auth.service;


import dding.auth.dto.request.JoinRequest;
import dding.auth.dto.request.LoginRequest;
import dding.auth.entity.Auth;
import dding.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder encoder;


    /**
     * loginId 중복 체크
     * 회원가입 기능 구현 시 사용
     * 중복되면 true return
     */
    public boolean checkLoginIdDuplicate(String loginId) {
        return authRepository.existsByLoginId(loginId);
    }

    /**
     * nickname 중복 체크
     * 회원가입 기능 구현 시 사용
     * 중복되면 true return
     */
    public boolean checkNicknameDuplicate(String nickname) {
        return authRepository.existsByNickname(nickname);
    }


    /**
     * 회원가입 기능 2
     * 화면에서 JoinRequest(loginId, password, nickname)을 입력받아 User로 변환 후 저장
     * 비밀번호를 암호화해서 저장
     * loginId, nickname 중복 체크는 Controller에서 진행 => 에러 메세지 출력을 위해
     */
    public void join2(JoinRequest req) {
        authRepository.save(req.toEntity(encoder.encode(req.getPassword())));
    }

    /**
     *  로그인 기능
     *  화면에서 LoginRequest(loginId, password)을 입력받아 loginId와 password가 일치하면 User return
     *  loginId가 존재하지 않거나 password가 일치하지 않으면 null return
     */
    public Auth login(LoginRequest req) {
        Optional<Auth> optionalUser = authRepository.findByLoginId(req.getLoginId());

        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }

        Auth user = optionalUser.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if(encoder.matches(req.getPassword(),user.getPassword())) {

            return user;
        }
        return null;

    }



    /**
     * userId를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * userId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * userId로 찾아온 User가 존재하면 User return
     */
    public Auth getLoginUserById(String userId) {
        if(userId == null) return null;

        Optional<Auth> optionalUser = authRepository.findById(userId);
        return optionalUser.orElse(null);

    }

    /**
     * loginId(String)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * loginId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * loginId로 찾아온 User가 존재하면 User return
     */
    public Auth getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<Auth> optionalUser = authRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }


}
