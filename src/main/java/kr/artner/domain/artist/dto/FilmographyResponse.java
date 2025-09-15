package kr.artner.domain.artist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FilmographyResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilmographyItem {
        private Long id;
        private String title;
        private String description;
        private LocalDate releasedAt;
        private String mediaUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilmographyListResponse {
        private List<FilmographyItem> filmographies;
        private kr.artner.response.PageInfo pageInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateFilmographyResponse {
        private Long id;
        private String title;
        private String description;
        private LocalDate releasedAt;
        private String mediaUrl;
        private String message;
    }
}