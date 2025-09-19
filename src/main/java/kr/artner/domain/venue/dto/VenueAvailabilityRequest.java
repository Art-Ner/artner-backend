package kr.artner.domain.venue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class VenueAvailabilityRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAvailabilityRequest {
        
        @NotNull(message = "시작 시각은 필수입니다.")
        private LocalDateTime startDt;
        
        @NotNull(message = "종료 시각은 필수입니다.")
        private LocalDateTime endDt;
        
        private String note;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAvailabilityRequest {
        
        private LocalDateTime startDt;
        
        private LocalDateTime endDt;
        
        private String note;
    }
}