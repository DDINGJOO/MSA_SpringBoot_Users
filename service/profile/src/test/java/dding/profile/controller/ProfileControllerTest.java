package dding.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dding.profile.dto.request.ProfileRequest;
import dding.profile.dto.request.ProfileUpDateRequest;
import dding.profile.dto.response.ProfileResponse;
import dding.profile.dto.response.ProfileSimpleResponse;
import dding.profile.dto.response.ProfileReadResponse;
import dding.profile.service.ProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    @Test
    @DisplayName("POST /api/profiles/{userId} - 성공")
    void createProfile_success() throws Exception {
        // given
        ProfileRequest req = new ProfileRequest();
        req.setUserId("user3");
        req.setNickname("nick3");
        // … 나머지 필드 세팅

        given(profileService.DuplimentedNicknam("nick3")).willReturn(false);
        willDoNothing().given(profileService).createSimpleProfile(any());

        // when
        mockMvc.perform(post("/api/profiles/user3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        // then
        ArgumentCaptor<ProfileRequest> captor = ArgumentCaptor.forClass(ProfileRequest.class);
        then(profileService).should().createSimpleProfile(captor.capture());
        ProfileRequest captured = captor.getValue();
        assertEquals("user3", captured.getUserId());
        assertEquals("nick3", captured.getNickname());
    }

    @Test
    @DisplayName("POST /api/profiles/{userId} - 본인만 생성 가능 (403)")
    void createProfile_forbidden() throws Exception {
        ProfileRequest req = new ProfileRequest();
        req.setUserId("other");
        req.setNickname("nickX");

        mockMvc.perform(post("/api/profiles/user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("본인만 프로필을 생성할 수 있습니다."));
    }

    @Test
    @DisplayName("GET /api/profiles/simple/{userId}")
    void getSimpleProfile() throws Exception {
        ProfileSimpleResponse dto = ProfileSimpleResponse.builder()
                .userId("user1")
                .nickname("nick1")
                .preferred1("guitar")
                .preferred2("bass")
                .build();
        given(profileService.getSimpleProfile("user1")).willReturn(dto);

        mockMvc.perform(get("/api/profiles/simple/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.nickname").value("nick1"))
                .andExpect(jsonPath("$.preferred1").value("guitar"))
                .andExpect(jsonPath("$.preferred2").value("bass"));
    }

    @Test
    @DisplayName("GET /api/profiles/{userId}")
    void getProfile_full() throws Exception {
        ProfileResponse dto = ProfileResponse.builder()
                .userId("user2")
                .nickname("nick2")
                .city("CityB")
                .build();
        given(profileService.getProfile("user2")).willReturn(dto);

        mockMvc.perform(get("/api/profiles/user2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user2"))
                .andExpect(jsonPath("$.nickname").value("nick2"))
                .andExpect(jsonPath("$.city").value("CityB"));
    }

    @Test
    @DisplayName("GET /api/profiles/read/{userId}")
    void readProfile() throws Exception {
        ProfileReadResponse dto = ProfileReadResponse.builder()
                .nickname("nick1")
                .introduction("intro1")
                .build();
        given(profileService.readProfile("user1")).willReturn(dto);

        mockMvc.perform(get("/api/profiles/read/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("nick1"))
                .andExpect(jsonPath("$.introduction").value("intro1"));
    }

    @Test
    @DisplayName("PUT /api/profiles/{userId}")
    void updateProfile() throws Exception {
        // given
        ProfileUpDateRequest upd = new ProfileUpDateRequest();
        upd.setNickname("nick1-upd");
        // ... 나머지 필드 세팅

        willDoNothing().given(profileService)
                .updateProfile(eq("user1"), any(ProfileUpDateRequest.class));

        // when
        mockMvc.perform(put("/api/profiles/user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(content().string("프로필이 수정되었습니다."));

        // then
        ArgumentCaptor<ProfileUpDateRequest> captor =
                ArgumentCaptor.forClass(ProfileUpDateRequest.class);
        then(profileService).should()
                .updateProfile(eq("user1"), captor.capture());

        ProfileUpDateRequest captured = captor.getValue();
        assertEquals("nick1-upd", captured.getNickname());
        // ... 필요한 필드들 추가 검증
    }

    @Test
    @DisplayName("DELETE /api/profiles/{userId}")
    void deleteProfile() throws Exception {
        willDoNothing().given(profileService).deleteProfile("user2");

        mockMvc.perform(delete("/api/profiles/user2"))
                .andExpect(status().isOk())
                .andExpect(content().string("프로필이 삭제되었습니다."));

        then(profileService).should().deleteProfile("user2");
    }

    @Test
    @DisplayName("GET /api/profiles/users - 필터 없이")
    void searchUsers_noFilter() throws Exception {
        List<ProfileSimpleResponse> list = List.of(
                ProfileSimpleResponse.builder().userId("u1").nickname("nick1").preferred1("a").preferred2("b").build(),
                ProfileSimpleResponse.builder().userId("u2").nickname("nick2").preferred1("a").preferred2("b").build()
        );
        Page<ProfileSimpleResponse> page = new PageImpl<>(list, PageRequest.of(0, 10), 2);
        given(profileService.searchUsers(any())).willReturn(page);

        mockMvc.perform(get("/api/profiles/users")
                        .param("page","0")
                        .param("size","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @DisplayName("GET /api/profiles/users - 닉네임 필터")
    void searchUsers_byNickname() throws Exception {
        List<ProfileSimpleResponse> list = List.of(
                ProfileSimpleResponse.builder().userId("u2").nickname("nick2").preferred1("a").preferred2("b").build()
        );
        Page<ProfileSimpleResponse> page = new PageImpl<>(list, PageRequest.of(0, 5), 1);
        given(profileService.searchUsers(argThat(req -> "nick2".equals(req.getNickname()))))
                .willReturn(page);

        mockMvc.perform(get("/api/profiles/users")
                        .param("nickname","nick2")
                        .param("page","0")
                        .param("size","5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].nickname").value("nick2"));
    }
}
