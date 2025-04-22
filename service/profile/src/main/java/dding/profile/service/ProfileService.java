package dding.profile.service;


import dding.profile.dto.request.ProfileRequest;

import dding.profile.dto.request.ProfileUpDateRequest;
import dding.profile.dto.request.UserSearchRequest;
import dding.profile.dto.response.ProfileReadResponse;
import dding.profile.dto.response.ProfileResponse;
import dding.profile.dto.response.ProfileSimpleResponse;
import dding.profile.entity.Profile;
import dding.profile.execption.ProfileNotFoundException;
import dding.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    public boolean DuplimentedNicknam(String nickname)
    {
        return profileRepository.existsByNickname(nickname);
    }

    @Transactional
    public void createSimpleProfile(ProfileRequest request) {
        if (profileRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException("이미 프로필이 존재합니다.");
        }

        Profile profile = ProfileRequest.toEntity(request);

        profileRepository.save(profile);
    }

    public ProfileResponse getProfile(String userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(()  -> new ProfileNotFoundException(userId));

        return ProfileResponse.fromEntity(profile);
    }


    public ProfileReadResponse readProfile(String userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(()  -> new ProfileNotFoundException(userId));

        return ProfileReadResponse.fromEntity(profile);
    }

    public ProfileSimpleResponse getSimpleProfile(String userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        return ProfileSimpleResponse.fromEntity(profile);
    }

    @Transactional
    public void updateProfile(String userId, ProfileUpDateRequest request)  {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow();
        if(!(request.getNickname().equals(profile.getNickname())) && profileRepository.existsByNickname(request.getNickname()))
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        if(request.getNickname() == null || request.getNickname().isEmpty())
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        if(request.getCity() == null || request.getCity().isEmpty())
            throw new IllegalArgumentException("도시를 입력해주세요.");
        if(request.getPreferred1() == null || request.getPreferred1().isEmpty())
            throw new IllegalArgumentException("선호1을 입력해주세요.");
        if(request.getPreferred2() == null || request.getPreferred2().isEmpty())
            throw new IllegalArgumentException("선호2을 입력해주세요.");
        if(request.getIntroduction() == null || request.getIntroduction().isEmpty())
            throw new IllegalArgumentException("자기소개를 입력해주세요.");
        if(request.getIntroduction().length() > 100)
            throw new IllegalArgumentException("자기소개는 100자 이내로 작성해주세요.");
        if(request.getPreferred1().length() > 10)
            throw new IllegalArgumentException("선호1은 10자 이내로 작성해주세요.");
        if(request.getPreferred2().length() > 10)
            throw new IllegalArgumentException("선호2은 10자 이내로 작성해주세요.");
        if(request.getNickname().length() > 10)
            throw new IllegalArgumentException("닉네임은 10자 이내로 작성해주세요.");
        if(request.getCity().length() > 10)
            throw new IllegalArgumentException("도시는 10자 이내로 작성해주세요.");
        if(request.getIntroduction().length() > 100)
            throw new IllegalArgumentException("자기소개는 100자 이내로 작성해주세요.");


        profileRepository.save(ProfileUpDateRequest.ToEntity(request,profile));


    }

    @Transactional
    public void deleteProfile(String userId) {
        profileRepository.deleteById(userId);
    }


    public Page<ProfileSimpleResponse> searchUsers(UserSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Profile> page = profileRepository.findByConditions(
                request.getNickname(),
                request.getPreferred1(),
                request.getPreferred2(),
                pageable
        );

        return page.map(p -> ProfileSimpleResponse.builder()
                .userId(p.getUserId())
                .nickname(p.getNickname())
                .preferred1(p.getPreferred1())
                .preferred2(p.getPreferred2())
                .build());
    }
}
