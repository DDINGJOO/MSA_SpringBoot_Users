package dding.profile.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSimpleResponse {
    private String userId;
    private String nickname;
    private String profileImageUrl;
}
