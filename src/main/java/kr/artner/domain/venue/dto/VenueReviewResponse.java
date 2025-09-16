package kr.artner.domain.venue.dto;

import kr.artner.domain.user.dto.UserResponse;
import kr.artner.domain.venue.dto.VenueResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VenueReviewResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetVenueReviewResponse {
        private Long id;
        private UserResponse.GetUserInfoResponse user;
        private VenueResponse.VenueItem venue;
        private BigDecimal rate;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateVenueReviewResponse {
        private Long id;
        private BigDecimal rate;
        private String content;
        private LocalDateTime createdAt;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateVenueReviewResponse {
        private Long id;
        private BigDecimal rate;
        private String content;
        private LocalDateTime updatedAt;
        private String message;
    }
}