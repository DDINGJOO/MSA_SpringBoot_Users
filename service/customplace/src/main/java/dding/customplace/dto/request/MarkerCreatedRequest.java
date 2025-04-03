package dding.customplace.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MarkerCreatedRequest {

    private String postId;
    private String title;
    private String address;
    private String userId;
    private String description;
    private LocalDateTime date;
    private Double latitude;
    private Double longitude;
    private String color;
    private int score;

}
