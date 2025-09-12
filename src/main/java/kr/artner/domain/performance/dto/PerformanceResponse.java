package kr.artner.domain.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PerformanceResponse {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPerformanceResponse {
        private Long performanceId;
        private String title;
        private String posterUrl;
    }
}
