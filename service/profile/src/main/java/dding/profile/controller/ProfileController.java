package dding.profile.controller;

import dding.profile.dto.request.ProfileRequest;
import dding.profile.dto.request.ProfileUpdateRequest;
import dding.profile.dto.response.ProfileResponse;
import dding.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    //  프로필 생성
    @PostMapping
    public ResponseEntity<String> createProfile(@RequestBody ProfileRequest request) {
        profileService.createProfile(request);
        return ResponseEntity.ok("프로필이 생성되었습니다.");
    }

    // 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String userId) {
        ProfileResponse response = profileService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    // 프로필 수정
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateProfile(@PathVariable String userId,
                                                @RequestBody ProfileUpdateRequest request) {
        profileService.updateProfile(userId, request);
        return ResponseEntity.ok("프로필이 수정되었습니다.");
    }

    // 프로필 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable String userId) {
        profileService.deleteProfile(userId);
        return ResponseEntity.ok("프로필이 삭제되었습니다.");
    }
}