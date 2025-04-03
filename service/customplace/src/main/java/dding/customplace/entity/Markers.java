package dding.customplace.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "markers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Markers {

    @Id
    private String id;

    private String userId;

    private String title;

    private String address;

    @Lob
    private String description;

    private LocalDateTime date;

    private Double latitude;

    private Double longitude;

    private String color;

    private int score;

}
