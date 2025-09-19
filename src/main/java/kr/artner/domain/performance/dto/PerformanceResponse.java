package kr.artner.domain.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PerformanceResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePerformanceResponse {
        private Long id;
        private Long projectId;
        private Long venueId;
        private String title;
        private String description;
        private String genreCode;
        private Integer runningTime;
        private String posterUrl;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private String status;
        private LocalDateTime publishedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublishPerformanceResponse {
        private Long id;
        private Long projectId;
        private Long venueId;
        private String title;
        private String description;
        private String genreCode;
        private Integer runningTime;
        private String posterUrl;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private String status;
        private LocalDateTime publishedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPerformanceResponse {
        private Long performanceId;
        private String title;
        private String posterUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceListResponse {
        private java.util.List<PerformanceItem> performances;
        private PageInfo pageInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceItem {
        private Long id;
        private Long projectId;
        private Long venueId;
        private String title;
        private String description;
        private String genreCode;
        private Integer runningTime;
        private String posterUrl;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private String status;
        private LocalDateTime publishedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private Integer page;
        private Integer size;
        private Long totalElements;
        private Integer totalPages;
        private Boolean first;
        private Boolean last;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceDetailResponse {
        private Long id;
        private Long projectId;
        private Long venueId;
        private String title;
        private String description;
        private String genreCode;
        private Integer runningTime;
        private String posterUrl;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private String status;
        private LocalDateTime publishedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePerformanceResponse {
        private Long id;
        private Long projectId;
        private Long venueId;
        private String title;
        private String description;
        private String genreCode;
        private Integer runningTime;
        private String posterUrl;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private String status;
        private LocalDateTime publishedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
