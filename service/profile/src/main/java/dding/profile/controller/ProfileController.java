package dding.profile.controller;

import dding.profile.dto.request.ProfileRequest;
import dding.profile.dto.request.ProfileUpDateRequest;
import dding.profile.dto.request.UserSearchRequest;
import dding.profile.dto.response.ProfileReadResponse;
import dding.profile.dto.response.ProfileResponse;
import dding.profile.dto.response.ProfileSimpleResponse;
import dding.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    @GetMapping("/simple/{userId}")
    public ResponseEntity<ProfileSimpleResponse> getSimpleProfile(@PathVariable String userId) {
        ProfileSimpleResponse response = profileService.getSimpleProfile(userId);
        return ResponseEntity.ok(response);
    }


    //  프로필 생성
    @PostMapping("/{userId}")
    public ResponseEntity<?> createProfile(@PathVariable String userId,
                                           @RequestBody ProfileRequest request) {

        if (!userId.equals(request.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "success", false,
                    "message", "본인만 프로필을 생성할 수 있습니다."
            ));
        }

        if (profileService.DuplimentedNicknam(request.getNickname())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이미 사용 중인 닉네임 입니다."
            ));
        }

        profileService.createSimpleProfile(request);
        return ResponseEntity.ok("프로필이 생성되었습니다.");
    }


    // 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable String userId) {
        ProfileResponse response = profileService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/read/{userId}")
    public ResponseEntity<?> readProfile(@PathVariable String userId) {
        ProfileReadResponse response = profileService.readProfile(userId);
        return ResponseEntity.ok(response);
    }


    // 프로필 수정
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable String userId,
                                                @RequestBody ProfileUpDateRequest request){
        profileService.updateProfile(userId, request);
        return ResponseEntity.ok("프로필이 수정되었습니다.");
    }

    // 프로필 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteProfile(@PathVariable String userId) {
        profileService.deleteProfile(userId);
        return ResponseEntity.ok("프로필이 삭제되었습니다.");
    }



    @GetMapping("/users")
    //GET /api/users?nickname=딩&preferred1=Guitar&page=0&size=5
    public ResponseEntity<Page<ProfileSimpleResponse>> searchUsers(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String preferred1,
            @RequestParam(required = false) String preferred2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserSearchRequest request = UserSearchRequest.builder()
                .nickname(nickname)
                .page(page)
                .preferred1(preferred1)
                .preferred2(preferred2)
                .size(size)
                .build();
        return ResponseEntity.ok(profileService.searchUsers(request));
    }

}

