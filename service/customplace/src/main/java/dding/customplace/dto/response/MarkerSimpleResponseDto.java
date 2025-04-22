package dding.customplace.dto.response;

import dding.customplace.entity.Markers;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class MarkerSimpleResponseDto {

    private String postId;
    private String title;
    private String description;
    private LocalDateTime date;
    private String address;
    private int score;



    public static MarkerSimpleResponseDto fromEntity(Markers post) {
        return MarkerSimpleResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .date(post.getDate())
                .address(post.getAddress())
                .score(post.getScore())

                .build();
    }
}
