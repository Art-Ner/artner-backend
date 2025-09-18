package kr.artner.domain.venue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class VenueAvailabilityResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAvailabilityResponse {
        private Long id;
        private Long venueId;
        private Boolean isBlocked;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private String note;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAvailabilityResponse {
        private Long id;
        private Long venueId;
        private Boolean isBlocked;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private String note;
    }
}