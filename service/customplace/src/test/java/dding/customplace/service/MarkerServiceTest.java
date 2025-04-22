package dding.customplace.service;

import static org.junit.jupiter.api.Assertions.*;



import dding.customplace.dto.request.MarkerCreatedRequest;
import dding.customplace.dto.response.MarkerResponseDto;
import dding.customplace.dto.response.MarkerSimpleResponseDto;
import dding.customplace.entity.Markers;
import dding.customplace.repostiory.MarkersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarkerServiceTest {

    @Mock
    private MarkersRepository markersRepository;

    @InjectMocks
    private MarkerService markerService;

    private MarkerCreatedRequest sampleRequest;
    private Markers sampleEntity;

    @BeforeEach
    void setUp() {
        sampleRequest = MarkerCreatedRequest.builder()
                .postId("post1")
                .title("My Title")
                .address("123 Main St")
                .userId("user1")
                .description("A nice place")
                .date(LocalDateTime.of(2025, 4, 23, 12, 0))
                .latitude(37.1234)
                .longitude(127.5678)
                .color("red")
                .score(5)
                .build();

        // create the entity as toEntity would
        sampleEntity = MarkerCreatedRequest.toEntity(sampleRequest);
    }

    @Test
    @DisplayName("createPost - saves and returns DTO")
    void createPost_savesAndReturnsDto() {
        // Arrange: stub repository.save to return the same entity passed in
        when(markersRepository.save(any(Markers.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        MarkerResponseDto dto = markerService.createPost(sampleRequest);

        // Assert: fields in response dto match request
        assertThat(dto.getPostId()).isEqualTo(sampleRequest.getPostId());
        assertThat(dto.getTitle()).isEqualTo(sampleRequest.getTitle());
        assertThat(dto.getAddress()).isEqualTo(sampleRequest.getAddress());
        assertThat(dto.getDescription()).isEqualTo(sampleRequest.getDescription());
        assertThat(dto.getDate()).isEqualTo(sampleRequest.getDate());
        assertThat(dto.getLatitude()).isEqualTo(sampleRequest.getLatitude());
        assertThat(dto.getLongitude()).isEqualTo(sampleRequest.getLongitude());
        assertThat(dto.getColor()).isEqualTo(sampleRequest.getColor());
        assertThat(dto.getScore()).isEqualTo(sampleRequest.getScore());

        // verify save was called once with an entity converted from request
        ArgumentCaptor<Markers> captor = ArgumentCaptor.forClass(Markers.class);
        then(markersRepository).should().save(captor.capture());
        Markers saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(sampleRequest.getPostId());
        assertThat(saved.getUserId()).isEqualTo(sampleRequest.getUserId());
    }

    @Test
    @DisplayName("getAllPosts - returns all posts as DTOs")
    void getAllPosts_returnsMappedDtoList() {
        // Arrange
        Markers m1 = sampleEntity;
        Markers m2 = MarkerCreatedRequest.toEntity(
                MarkerCreatedRequest.builder()
                        .postId("post2")
                        .title("Another")
                        .address("456 Side St")
                        .userId("user2")
                        .description("Another place")
                        .date(LocalDateTime.of(2025, 4, 24, 15, 30))
                        .latitude(38.0000)
                        .longitude(128.0000)
                        .color("blue")
                        .score(3)
                        .build()
        );

        when(markersRepository.findAll()).thenReturn(List.of(m1, m2));

        // Act
        List<MarkerResponseDto> dtos = markerService.getAllPosts();

        // Assert
        assertThat(dtos).hasSize(2);
        assertThat(dtos)
                .extracting(MarkerResponseDto::getPostId, MarkerResponseDto::getTitle)
                .containsExactly(
                        tuple("post1", "My Title"),
                        tuple("post2", "Another")
                );
    }

    @Test
    @DisplayName("getMyPosts - filters by userId")
    void getMyPosts_filtersByUserId() {
        // Arrange
        Markers forUser = sampleEntity;
        Markers other = MarkerCreatedRequest.toEntity(
                MarkerCreatedRequest.builder()
                        .postId("post3")
                        .title("Different")
                        .address("789 Elsewhere")
                        .userId("user2")
                        .description("Elsewhere")
                        .date(LocalDateTime.now())
                        .latitude(35.0)
                        .longitude(128.0)
                        .color("green")
                        .score(4)
                        .build()
        );
        when(markersRepository.findAllByUserId("user1"))
                .thenReturn(List.of(forUser));

        // Act
        List<MarkerSimpleResponseDto> simples = markerService.getMyPosts("user1");

        // Assert
        assertThat(simples).hasSize(1);
        MarkerSimpleResponseDto dto = simples.get(0);
        assertThat(dto.getPostId()).isEqualTo("post1");
        assertThat(dto.getTitle()).isEqualTo("My Title");
        assertThat(dto.getDescription()).isEqualTo("A nice place");
    }

    @Test
    @DisplayName("getPostById - returns post by ID")
    void getSimplePostById_found() {
        // Arrange
        when(markersRepository.findById("post1"))
                .thenReturn(Optional.of(sampleEntity));

        // Act
        MarkerSimpleResponseDto dto = markerService.getSimplePostById("post1");

        // Assert
        assertThat(dto.getPostId()).isEqualTo(sampleEntity.getId());
        assertThat(dto.getTitle()).isEqualTo(sampleEntity.getTitle());
        assertThat(dto.getAddress()).isEqualTo(sampleEntity.getAddress());
        assertThat(dto.getScore()).isEqualTo(sampleEntity.getScore());
    }

    @Test
    @DisplayName("getPostById - throws if not found")
    void getSimplePostById_notFound_throws() {
        // Arrange
        when(markersRepository.findById("unknown"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> markerService.getSimplePostById("unknown"))
                .isInstanceOf(NoSuchElementException.class);
    }
}

