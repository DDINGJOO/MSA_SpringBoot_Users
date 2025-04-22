package dding.customplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dding.customplace.dto.request.MarkerCreatedRequest;
import dding.customplace.dto.response.MarkerResponseDto;
import dding.customplace.dto.response.MarkerSimpleResponseDto;
import dding.customplace.service.MarkerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MarkersController.class)
class MarkersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MarkerService markerService;

    private MarkerCreatedRequest makeRequest() {
        return MarkerCreatedRequest.builder()
                .postId("post1")
                .title("Test Title")
                .address("123 Test St")
                .userId("user1")
                .description("Test Description")
                .date(LocalDateTime.of(2025, 4, 23, 12, 0))
                .latitude(37.0)
                .longitude(127.0)
                .color("red")
                .score(10)
                .build();
    }

    private MarkerResponseDto makeResponseDto() {
        return MarkerResponseDto.builder()
                .postId("post1")
                .title("Test Title")
                .address("123 Test St")
                .userId("user1")
                .description("Test Description")
                .date(LocalDateTime.of(2025, 4, 23, 12, 0))
                .latitude(37.0)
                .longitude(127.0)
                .color("red")
                .score(10)
                .build();
    }

    @Test
    @DisplayName("POST /api/markers - 성공")
    void createPost_success() throws Exception {
        MarkerCreatedRequest req = makeRequest();
        MarkerResponseDto dto = makeResponseDto();

        given(markerService.createPost(any(MarkerCreatedRequest.class)))
                .willReturn(dto);

        mockMvc.perform(post("/api/markers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value("post1"))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.address").value("123 Test St"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.latitude").value(37.0))
                .andExpect(jsonPath("$.longitude").value(127.0))
                .andExpect(jsonPath("$.color").value("red"))
                .andExpect(jsonPath("$.score").value(10));

        then(markerService).should().createPost(any(MarkerCreatedRequest.class));
    }

    @Test
    @DisplayName("GET /api/markers/all - 전체 글 조회")
    void getAllPosts() throws Exception {
        MarkerResponseDto dto1 = makeResponseDto();
        MarkerResponseDto dto2 = MarkerResponseDto.builder()
                .postId("post2")
                .title("Another Title")
                .address("456 Another St")
                .userId("user2")
                .description("Another Desc")
                .date(LocalDateTime.of(2025, 4, 24, 15, 30))
                .latitude(38.0)
                .longitude(128.0)
                .color("blue")
                .score(5)
                .build();

        given(markerService.getAllPosts())
                .willReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/markers/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].postId").value("post1"))
                .andExpect(jsonPath("$[1].postId").value("post2"));

        then(markerService).should().getAllPosts();
    }

    @Test
    @DisplayName("GET /api/markers/simple/{Id} - 단일 요약 조회")
    void getSimplePostById() throws Exception {
        MarkerSimpleResponseDto simpleDto = MarkerSimpleResponseDto.builder()
                .postId("post1")
                .title("Test Title")
                .description("Test Description")
                .date(LocalDateTime.of(2025, 4, 23, 12, 0))
                .address("123 Test St")
                .score(10)
                .build();

        given(markerService.getSimplePostById("post1"))
                .willReturn(simpleDto);

        mockMvc.perform(get("/api/markers/simple/post1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value("post1"))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.address").value("123 Test St"))
                .andExpect(jsonPath("$.score").value(10));

        then(markerService).should().getSimplePostById("post1");
    }

    @Test
    @DisplayName("GET /api/markers/simple/my/{userId} - 내 글 요약 조회")
    void getMyPosts() throws Exception {
        MarkerSimpleResponseDto simple1 = MarkerSimpleResponseDto.builder()
                .postId("post1")
                .title("Test Title")
                .description("Test Description")
                .date(LocalDateTime.of(2025, 4, 23, 12, 0))
                .address("123 Test St")
                .score(10)
                .build();

        MarkerSimpleResponseDto simple2 = MarkerSimpleResponseDto.builder()
                .postId("post3")
                .title("Third Title")
                .description("Third Description")
                .date(LocalDateTime.of(2025, 4, 25, 9, 0))
                .address("789 Elsewhere")
                .score(7)
                .build();

        given(markerService.getMyPosts("user1"))
                .willReturn(List.of(simple1, simple2));

        mockMvc.perform(get("/api/markers/simple/my/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].postId").value("post1"))
                .andExpect(jsonPath("$[1].postId").value("post3"));

        then(markerService).should().getMyPosts("user1");
    }
}
