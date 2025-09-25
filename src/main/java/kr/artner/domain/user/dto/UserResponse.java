package kr.artner.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResponse {
        private Long id;
        private String email;
        private String username;
        private LocalDateTime joinDate;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailInfoDTO {
        private Long id;
        private String email;
        private String username;
        private String phone;
        private String profileImageUrl;
        private String nickname;
        private Long artistId;
        private Long venueAdminId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResponse {
        private String username;
        private String phone;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetUserInfoResponse {
        private Long id;
        private String username;
        private String nickname;
        private Long artistId;
        private Long venueAdminId;
    }
}
