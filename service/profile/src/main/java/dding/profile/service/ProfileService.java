package dding.profile.service;


import dding.profile.dto.request.ProfileRequest;
import dding.profile.dto.request.ProfileUpdateRequest;
import dding.profile.dto.response.ProfileResponse;
import dding.profile.dto.response.ProfileSimpleResponse;
import dding.profile.entity.Profile;
import dding.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public void createProfile(ProfileRequest request) {
        if (profileRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException("이미 프로필이 존재합니다.");
        }

        Profile profile = Profile.builder()
                .userId(request.getUserId())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .addressId(request.getAddressId())
                .profileImageUrl(request.getProfileImageUrl())
                .preferred1(request.getPreferred1())
                .preferred2(request.getPreferred2())
                .introduction(request.getIntroduction())
                .totalPoint(0)
                .userLevel(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        profileRepository.save(profile);
    }

    public ProfileResponse getProfile(String userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다."));

        return ProfileResponse.builder()
                .userId(profile.getUserId())
                .nickname(profile.getNickname())
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .addressId(profile.getAddressId())
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
                .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다."));

        return ProfileSimpleResponse.builder()
                .userId(profile.getUserId())
                .nickname(profile.getNickname())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }

    public void updateProfile(String userId, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다."));

        profile.update(
                request.getNickname(),
                request.getEmail(),
                request.getPhone(),
                request.getAddressId(),
                request.getProfileImageUrl(),
                request.getPreferred1(),
                request.getPreferred2(),
                request.getIntroduction()
        );
    }

    public void deleteProfile(String userId) {
        profileRepository.deleteById(userId);
    }
}
