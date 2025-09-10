package kr.artner.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.artner.domain.common.enums.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO {

        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "올바른 이메일 형식이 아닙니다")
        private String email;

        @NotBlank(message = "사용자명은 필수입니다")
        private String username;

        private String phone;
        
        private String profileImageUrl;

        @NotNull(message = "OAuth 제공자는 필수입니다")
        private OAuthProvider oauthProvider;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfile {
        @NotBlank(message = "사용자명은 필수입니다")
        private String username;

        private String phone;
    }
}
