package kr.artner.domain.performance.dto;

import kr.artner.domain.performance.entity.Performance;

public class PerformanceConverter {
    public static PerformanceResponse.GetPerformanceResponse toGetPerformanceResponse(Performance performance) {
        return PerformanceResponse.GetPerformanceResponse.builder()
                .performanceId(performance.getId())
                .title(performance.getTitle())
                .posterUrl(performance.getPosterUrl())
                .build();
    }
}
