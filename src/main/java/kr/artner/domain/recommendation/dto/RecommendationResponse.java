package kr.artner.domain.recommendation.dto;

import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.project.enums.ProjectStatus;
import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class RecommendationResponse {

    @Getter
    @Builder
    public static class BatchGenerateResponse {
        private String message;
        private int processedUserCount;
        private int totalRecommendationsGenerated;
        private LocalDateTime generatedAt;
    }

    @Getter
    @Builder
    public static class RecommendedProject {
        private Long id;
        private String title;
        private String concept;
        private GenreCode targetGenre;
        private String targetRegion;
        private ProjectStatus status;
        private OwnerInfo owner;
        private double score;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class OwnerInfo {
        private Long id;
        private String username;
        private String artistName;
    }

    @Getter
    @Builder
    public static class GetRecommendationsResponse {
        private List<RecommendedProject> projects;
        private int totalCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime generatedAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime expiresAt;
    }
}
