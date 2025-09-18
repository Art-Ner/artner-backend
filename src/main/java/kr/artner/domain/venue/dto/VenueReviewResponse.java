package kr.artner.domain.venue.dto;

import kr.artner.domain.user.dto.UserResponse;
import kr.artner.response.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VenueReviewResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetVenueReviewResponse {
        private Long id;
        private UserResponse.GetUserInfoResponse user;
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetVenueReviewsResponse {
        private List<VenueReviewItem> reviews;
        private PageInfo pageInfo;
        private BigDecimal averageRating;
        private Long totalReviews;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VenueReviewItem {
        private Long id;
        private UserResponse.GetUserInfoResponse reviewer;
        private BigDecimal rate;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}