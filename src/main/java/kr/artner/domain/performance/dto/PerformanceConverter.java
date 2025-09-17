package kr.artner.domain.performance.dto;

import kr.artner.domain.performance.entity.Performance;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.venue.entity.Venue;

import java.time.LocalDateTime;

public class PerformanceConverter {

    public static Performance toEntity(PerformanceRequest.CreatePerformanceRequest request, Project project, Venue venue) {
        return Performance.builder()
                .project(project)
                .venue(venue)
                .title(request.getTitle())
                .description(request.getDescription())
                .genreCode(request.getGenreCode())
                .runningTime(request.getRunningTime())
                .posterUrl(request.getPosterUrl())
                .startDt(request.getStartDt())
                .endDt(request.getEndDt())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static PerformanceResponse.CreatePerformanceResponse toCreatePerformanceResponse(Performance performance) {
        return PerformanceResponse.CreatePerformanceResponse.builder()
                .id(performance.getId())
                .projectId(performance.getProject() != null ? performance.getProject().getId() : null)
                .venueId(performance.getVenue() != null ? performance.getVenue().getId() : null)
                .title(performance.getTitle())
                .description(performance.getDescription())
                .genreCode(performance.getGenreCode().name())
                .runningTime(performance.getRunningTime())
                .posterUrl(performance.getPosterUrl())
                .startDt(performance.getStartDt())
                .endDt(performance.getEndDt())
                .status(performance.getStatus().name())
                .publishedAt(performance.getPublishedAt())
                .createdAt(performance.getCreatedAt())
                .updatedAt(performance.getUpdatedAt())
                .build();
    }

    public static PerformanceResponse.PublishPerformanceResponse toPublishPerformanceResponse(Performance performance) {
        return PerformanceResponse.PublishPerformanceResponse.builder()
                .id(performance.getId())
                .projectId(performance.getProject() != null ? performance.getProject().getId() : null)
                .venueId(performance.getVenue() != null ? performance.getVenue().getId() : null)
                .title(performance.getTitle())
                .description(performance.getDescription())
                .genreCode(performance.getGenreCode().name())
                .runningTime(performance.getRunningTime())
                .posterUrl(performance.getPosterUrl())
                .startDt(performance.getStartDt())
                .endDt(performance.getEndDt())
                .status(performance.getStatus().name())
                .publishedAt(performance.getPublishedAt())
                .createdAt(performance.getCreatedAt())
                .updatedAt(performance.getUpdatedAt())
                .build();
    }

    public static PerformanceResponse.GetPerformanceResponse toGetPerformanceResponse(Performance performance) {
        return PerformanceResponse.GetPerformanceResponse.builder()
                .performanceId(performance.getId())
                .title(performance.getTitle())
                .posterUrl(performance.getPosterUrl())
                .build();
    }

    public static PerformanceResponse.PerformanceItem toPerformanceItem(Performance performance) {
        return PerformanceResponse.PerformanceItem.builder()
                .id(performance.getId())
                .projectId(performance.getProject() != null ? performance.getProject().getId() : null)
                .venueId(performance.getVenue() != null ? performance.getVenue().getId() : null)
                .title(performance.getTitle())
                .description(performance.getDescription())
                .genreCode(performance.getGenreCode().name())
                .runningTime(performance.getRunningTime())
                .posterUrl(performance.getPosterUrl())
                .startDt(performance.getStartDt())
                .endDt(performance.getEndDt())
                .status(performance.getStatus().name())
                .publishedAt(performance.getPublishedAt())
                .createdAt(performance.getCreatedAt())
                .updatedAt(performance.getUpdatedAt())
                .build();
    }

    public static PerformanceResponse.PerformanceDetailResponse toPerformanceDetailResponse(Performance performance) {
        return PerformanceResponse.PerformanceDetailResponse.builder()
                .id(performance.getId())
                .projectId(performance.getProject() != null ? performance.getProject().getId() : null)
                .venueId(performance.getVenue() != null ? performance.getVenue().getId() : null)
                .title(performance.getTitle())
                .description(performance.getDescription())
                .genreCode(performance.getGenreCode().name())
                .runningTime(performance.getRunningTime())
                .posterUrl(performance.getPosterUrl())
                .startDt(performance.getStartDt())
                .endDt(performance.getEndDt())
                .status(performance.getStatus().name())
                .publishedAt(performance.getPublishedAt())
                .createdAt(performance.getCreatedAt())
                .updatedAt(performance.getUpdatedAt())
                .build();
    }

    public static PerformanceResponse.UpdatePerformanceResponse toUpdatePerformanceResponse(Performance performance) {
        return PerformanceResponse.UpdatePerformanceResponse.builder()
                .id(performance.getId())
                .projectId(performance.getProject() != null ? performance.getProject().getId() : null)
                .venueId(performance.getVenue() != null ? performance.getVenue().getId() : null)
                .title(performance.getTitle())
                .description(performance.getDescription())
                .genreCode(performance.getGenreCode().name())
                .runningTime(performance.getRunningTime())
                .posterUrl(performance.getPosterUrl())
                .startDt(performance.getStartDt())
                .endDt(performance.getEndDt())
                .status(performance.getStatus().name())
                .publishedAt(performance.getPublishedAt())
                .createdAt(performance.getCreatedAt())
                .updatedAt(performance.getUpdatedAt())
                .build();
    }
}
