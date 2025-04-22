package dding.profile.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")

class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;

    class Profile {
        private String userId;
        private String nickname;
        private String profileImageUrl;
        private String introduction;
        private String location;
        private String website;
        private String email;
        private String phoneNumber;
    }



    @Test
    @DisplayName("existsByNickname - 존재하지 않는 닉네임")
    void existsByNickname() {
        // given
        String nickname = "testNickname";
        // when
        boolean exists = profileRepository.existsByNickname(nickname);
        // then
        assertFalse(exists);
    }

}
