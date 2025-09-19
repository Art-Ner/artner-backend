package kr.artner.global.auth.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TokenDto{
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LoginResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        private UserInfo user;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class UserInfo {
            private Long id;
            private String email;
            private String username;

            @JsonProperty("oauth_provider")
            private String oauthProvider;
        }
    }
}
