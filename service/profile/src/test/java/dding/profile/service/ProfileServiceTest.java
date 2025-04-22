package dding.profile.service;

import dding.profile.dto.request.ProfileRequest;
import dding.profile.dto.request.ProfileUpDateRequest;
import dding.profile.dto.response.ProfileReadResponse;
import dding.profile.dto.response.ProfileResponse;
import dding.profile.dto.response.ProfileSimpleResponse;
import dding.profile.entity.Profile;
import dding.profile.execption.ProfileNotFoundException;
import dding.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ProfileServiceTest {
    @Autowired
    private final ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;


    @Test
    @DisplayName("프로필 생성")
    void createSimpleProfile() {
        // given
        ProfileRequest request = new ProfileRequest();
        request.setUserId("testUserId111");
        request.setNickname("testNickname");
        request.setPreferred1("testPreferred1");
        request.setPreferred2("testPreferred2");
        request.setIntroduction("testIntroduction");
        request.setSNS_agree(true);
        request.setPhone("testPhone");
        request.setCity("testCity");
        request.setEmail("testEmail");


        // when
        profileService.createSimpleProfile(request);

        // then
        ProfileResponse profile = profileService.getProfile(request.getUserId());
        assertEquals(request.getNickname(), profile.getNickname());
        profileRepository.delete(profileRepository.findById(request.getUserId()).orElseThrow());
    }

    @Test
    @DisplayName("프로필 생성 - 조회  : ProfileResponse")
    void getProfile() {

        String userId = "testUserId112";
        ProfileRequest request = new ProfileRequest();
        request.setUserId(userId);
        request.setNickname("testNickname");
        request.setPreferred1("testPreferred1");
        request.setPreferred2("testPreferred2");
        request.setIntroduction("testIntroduction");
        request.setSNS_agree(true);
        request.setPhone("testPhone");
        request.setCity("testCity");
        request.setEmail("testEmail");

        profileService.createSimpleProfile(request);

        // when
        ProfileResponse profile = profileService.getProfile(userId);

        // then
        assertEquals(request.getNickname(), profile.getNickname());
        profileRepository.delete(profileRepository.findById(userId).orElseThrow());
    }

    @Test
    @DisplayName("프로필 생성 - 조회  : ProfileReadResponse")
    void readProfile() {
        String userId = "testUserId13";
        ProfileRequest request = new ProfileRequest();
        request.setUserId(userId);
        request.setNickname("testNickname");
        request.setPreferred1("testPreferred1");
        request.setPreferred2("testPreferred2");
        request.setIntroduction("testIntroduction");
        request.setSNS_agree(true);
        request.setPhone("testPhone");
        request.setCity("testCity");
        request.setEmail("testEmail");

        profileService.createSimpleProfile(request);

        // when
        ProfileReadResponse profile = profileService.readProfile(userId);

        // then
        assertEquals(request.getNickname(), profile.getNickname());
        assertEquals(request.getPreferred1(), profile.getPreferred1());
        assertEquals(request.getPreferred2(), profile.getPreferred2());
        assertEquals(request.getIntroduction(), profile.getIntroduction());
        assertEquals(request.getCity(), profile.getCity());
        assertEquals(request.getIntroduction(), profile.getIntroduction());
        profileRepository.delete(profileRepository.findById(userId).orElseThrow());

    }

    @Test
    @DisplayName("프로필 생성 - 조회  : ProfileSimpleResponse")
    void getSimpleProfile() {
        String userId = "testUserId14";
        ProfileRequest request = new ProfileRequest();
        request.setUserId(userId);
        request.setNickname("testNickname");
        request.setPreferred1("testPreferred1");
        request.setPreferred2("testPreferred2");
        request.setIntroduction("testIntroduction");
        request.setSNS_agree(true);
        request.setPhone("testPhone");
        request.setCity("testCity");
        request.setEmail("testEmail");

        profileService.createSimpleProfile(request);

        // when
        ProfileSimpleResponse profile = profileService.getSimpleProfile(userId);

        // then
        assertEquals(request.getNickname(), profile.getNickname());
        assertEquals(request.getPreferred1(), profile.getPreferred1());
        assertEquals(request.getPreferred2(), profile.getPreferred2());
        assertEquals(request.getCity(), profile.getCity());

        assertEquals(request.getCity(), profile.getCity());
        profileRepository.delete(profileRepository.findById(userId).orElseThrow());


    }

    @Test
    @DisplayName("프로필 수정")
    void updateProfile() {
        String userId = "tes115";
        ProfileRequest request = new ProfileRequest();
        request.setUserId(userId);
        request.setNickname("123123");
        request.setPreferred1("123");
        request.setPreferred2("123");
        request.setIntroduction("123");
        request.setSNS_agree(true);
        request.setPhone("testPhone");
        request.setCity("testCity");

        profileService.createSimpleProfile(request);

        ProfileUpDateRequest updateRequest = new ProfileUpDateRequest();
        updateRequest.setNickname("1133");
        updateRequest.setPreferred1("GUITAR");
        updateRequest.setPreferred2("PIANO");
        updateRequest.setIntroduction("updatedIntroduction");
        updateRequest.setPhone("updatedPhone");
        updateRequest.setCity("123");
        updateRequest.setSNS_agree(true);

        profileService.updateProfile(userId, updateRequest);

        // when
        ProfileResponse profile = profileService.getProfile(userId);

        // then
        assertEquals(updateRequest.getNickname(), profile.getNickname());
        assertEquals(updateRequest.getPreferred1(), profile.getPreferred1());
        assertEquals(updateRequest.getPreferred2(), profile.getPreferred2());
        assertEquals(updateRequest.getIntroduction(), profile.getIntroduction());
        assertEquals(updateRequest.getPhone(), profile.getPhone());
        assertEquals(updateRequest.getCity(), profile.getCity());
        profileRepository.delete(profileRepository.findById(userId).orElseThrow());

    }

    @Test
    @DisplayName("프로필 삭제")
    void deleteProfile() {
        String userId = "testUserId16";
        ProfileRequest request = new ProfileRequest();
        request.setUserId(userId);
        request.setNickname("testNickname");
        request.setPreferred1("testPreferred1");
        request.setPreferred2("testPreferred2");
        request.setIntroduction("testIntroduction");
        request.setSNS_agree(true);
        request.setPhone("testPhone");
        request.setCity("testCity");
        request.setEmail("testEmail");

        profileService.createSimpleProfile(request);

        // when
        profileService.deleteProfile(userId);

        // then
        assertThrows(ProfileNotFoundException.class, () -> profileService.getProfile(userId));
    }


    @Test
    @DisplayName("중복된 닉네임 확인(중복 X)")
    void duplimentedNickname() {
        // given
        String nickname = "testNickname11";
        boolean expected = false;

        // when
        boolean result = profileService.DuplimentedNicknam(nickname);

        // then
        assertEquals(expected, result);
    }


}
