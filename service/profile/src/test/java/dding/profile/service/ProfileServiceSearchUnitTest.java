package dding.profile.service;

import dding.profile.dto.request.UserSearchRequest;
import dding.profile.dto.response.ProfileSimpleResponse;
import dding.profile.entity.Profile;
import dding.profile.repository.ProfileRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceSearchUnitTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @Test
    @DisplayName("searchUsers - 검색 조건에 맞는 사용자 프로필을 조회한다.")
    void searchUsers_MapsEntitiesToDto() {
        // given
        Profile p1 = Profile.builder().userId("u1").nickname("nickA").preferred1("p1").preferred2("p2").build();
        Profile p2 = Profile.builder().userId("u2").nickname("nickB").preferred1("p1").preferred2("p2").build();
        List<Profile> entities = Arrays.asList(p1, p2);
        Page<Profile> mockPage = new PageImpl<>(entities, PageRequest.of(0, 10), entities.size());

        when(profileRepository.findByConditions(
                "nick", "p1", "p2", PageRequest.of(0, 10)
        )).thenReturn(mockPage);

        UserSearchRequest req = UserSearchRequest.builder()
                .nickname("nick")
                .preferred1("p1")
                .preferred2("p2")
                .page(0)
                .size(10)
                .build();

        // when
        Page<ProfileSimpleResponse> result = profileService.searchUsers(req);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(ProfileSimpleResponse::getUserId, ProfileSimpleResponse::getNickname)
                .containsExactly(
                        Tuple.tuple("u1", "nickA"),
                        Tuple.tuple("u2", "nickB")
                );
    }
}
