package kr.artner.domain.userreview.dto;

import kr.artner.domain.user.dto.UserResponse;
import kr.artner.response.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class UserReviewResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetUserReviewResponse {
        private Long id;
        private UserResponse.GetUserInfoResponse user;
        private UserResponse.GetUserInfoResponse targetUser;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUserReviewResponse {
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserReviewResponse {
        private Long id;
        private String content;
        private LocalDateTime updatedAt;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetUserReviewsResponse {
        private List<UserReviewItem> reviews;
        private PageInfo pageInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserReviewItem {
        private Long id;
        private UserResponse.GetUserInfoResponse reviewer;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
