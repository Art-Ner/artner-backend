package kr.artner.domain.userreview.dto;

import kr.artner.domain.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
