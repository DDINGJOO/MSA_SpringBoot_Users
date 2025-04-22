package dding.profile.dto.request;

import lombok.*;

@Getter
@Builder
@Setter
public class UserSearchRequest {
    private String nickname;
    private String preferred1;
    private String preferred2;
    private int page;       // 페이지 번호
    private int size;       // 페이지 크기
}
