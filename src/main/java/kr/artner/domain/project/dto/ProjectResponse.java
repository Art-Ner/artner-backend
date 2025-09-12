package kr.artner.domain.project.dto;

import kr.artner.domain.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ProjectResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetProjectResponse {
        private Long id;
        private UserResponse.GetUserInfoResponse owner;
        private String title;
        private String concept;
        private String targetRegion;
        private String targetGenre;
        private String expectedScale;
        private String status;
        private LocalDateTime createdAt;
    }
}
