package kr.artner.domain.venue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BookingRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBookingRequest {
        
        @NotNull(message = "공연장 ID는 필수입니다.")
        private Long venueId;
        
        @NotNull(message = "프로젝트 ID는 필수입니다.")
        private Long projectId;
        
        @NotNull(message = "시작 시각은 필수입니다.")
        private LocalDateTime startDt;
        
        @NotNull(message = "종료 시각은 필수입니다.")
        private LocalDateTime endDt;
    }
}