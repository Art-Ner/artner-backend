package kr.artner.domain.venue.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class VenueReviewRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateVenueReview {
        @NotNull(message = "평점은 필수입니다.")
        @DecimalMin(value = "0.5", message = "평점은 0.5 이상이어야 합니다.")
        @DecimalMax(value = "5.0", message = "평점은 5.0 이하여야 합니다.")
        @Digits(integer = 1, fraction = 1, message = "평점은 소수점 한 자리까지 입력 가능합니다.")
        private BigDecimal rate;

        @NotBlank(message = "리뷰 내용은 필수입니다.")
        @Size(min = 10, max = 500, message = "리뷰 내용은 10자 이상 500자 이하여야 합니다.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateVenueReview {
        @NotNull(message = "평점은 필수입니다.")
        @DecimalMin(value = "0.5", message = "평점은 0.5 이상이어야 합니다.")
        @DecimalMax(value = "5.0", message = "평점은 5.0 이하여야 합니다.")
        @Digits(integer = 1, fraction = 1, message = "평점은 소수점 한 자리까지 입력 가능합니다.")
        private BigDecimal rate;

        @NotBlank(message = "리뷰 내용은 필수입니다.")
        @Size(min = 10, max = 500, message = "리뷰 내용은 10자 이상 500자 이하여야 합니다.")
        private String content;
    }
}