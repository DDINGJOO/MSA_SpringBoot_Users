package dding.profile.service;


import dding.profile.dto.request.ProfileRequest;

import dding.profile.dto.request.SimpleUpdateRequest;
import dding.profile.dto.request.UserSearchRequest;
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

        Profile profile = Profile.builder()
                .userId(request.getUserId())
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .profileImageUrl(request.getProfileImageUrl())
                .preferred1(request.getPreferred1())
                .preferred2(request.getPreferred2())
                .totalPoint(0)
                .userLevel(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        profileRepository.save(profile);
    }

    public ProfileResponse getProfile(String userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(()  -> new ProfileNotFoundException(userId));

        return ProfileResponse.builder()
                .userId(profile.getUserId())
                .nickname(profile.getNickname())
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .city(profile.getCity())
                .profileImageUrl(profile.getProfileImageUrl())
                .preferred1(profile.getPreferred1())
                .preferred2(profile.getPreferred2())
                .introduction(profile.getIntroduction())
                .totalPoint(profile.getTotalPoint())
                .userLevel(profile.getUserLevel())
                .build();
    }

    public ProfileSimpleResponse getSimpleProfile(String userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        return ProfileSimpleResponse.builder()
                .userId(profile.getUserId())
                .nickname(profile.getNickname())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }

    @Transactional
    public void updateProfile(String userId, SimpleUpdateRequest request)  {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow();

        System.out.println(" [업데이트 진입] request.nickname = " + request.getNickname());
        System.out.println(" 기존 nickname = " + profile.getNickname());

        profile.setNickname(request.getNickname());
        profile.setPhone(request.getPhone());
        profile.setPreferred1(request.getPreferred1());
        profile.setPreferred2(request.getPreferred2());
        profile.setProfileImageUrl(request.getProfileImageUrl());
        profile.setIntroduction(request.getIntroduction());
        profile.setSnsAgree(request.isSNS_agree());

        System.out.println(" 업데이트 요청 완료");


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
                .profileImageUrl(p.getProfileImageUrl())
                .preferred1(p.getPreferred1())
                .preferred2(p.getPreferred2())
                .build());
    }
}
