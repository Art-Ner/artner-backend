package kr.artner.domain.performance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.artner.domain.common.enums.GenreCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PerformanceRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePerformanceRequest {
        
        private Long projectId;
        
        private Long venueId;
        
        @NotBlank(message = "공연 제목은 필수입니다.")
        private String title;
        
        @NotBlank(message = "공연 내용은 필수입니다.")
        private String description;
        
        @NotNull(message = "장르 코드는 필수입니다.")
        private GenreCode genreCode;
        
        @Positive(message = "러닝타임은 양수여야 합니다.")
        private Integer runningTime;
        
        private String posterUrl;
        
        @NotNull(message = "시작 일시는 필수입니다.")
        private LocalDateTime startDt;
        
        @NotNull(message = "종료 일시는 필수입니다.")
        private LocalDateTime endDt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePerformanceRequest {
        
        private Long projectId;
        
        private Long venueId;
        
        @NotBlank(message = "공연 제목은 필수입니다.")
        private String title;
        
        @NotBlank(message = "공연 내용은 필수입니다.")
        private String description;
        
        @NotNull(message = "장르 코드는 필수입니다.")
        private GenreCode genreCode;
        
        @Positive(message = "러닝타임은 양수여야 합니다.")
        private Integer runningTime;
        
        private String posterUrl;
        
        @NotNull(message = "시작 일시는 필수입니다.")
        private LocalDateTime startDt;
        
        @NotNull(message = "종료 일시는 필수입니다.")
        private LocalDateTime endDt;
    }
}
