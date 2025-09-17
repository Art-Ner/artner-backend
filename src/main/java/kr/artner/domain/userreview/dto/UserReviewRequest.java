package kr.artner.domain.userreview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserReviewRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUserReview {
        @NotBlank(message = "리뷰 내용은 필수입니다.")
        @Size(min = 10, max = 500, message = "리뷰 내용은 10자 이상 500자 이하여야 합니다.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserReview {
        @NotBlank(message = "리뷰 내용은 필수입니다.")
        @Size(min = 10, max = 500, message = "리뷰 내용은 10자 이상 500자 이하여야 합니다.")
        private String content;
    }
}
