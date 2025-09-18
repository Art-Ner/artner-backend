package kr.artner.domain.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.artner.domain.common.enums.GenreCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProjectRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateProjectRequest {
        @NotBlank(message = "프로젝트명은 필수입니다.")
        private String title;

        @NotBlank(message = "프로젝트 요약은 필수입니다.")
        private String concept;

        private String targetRegion;

        @NotNull(message = "공연 장르는 필수입니다.")
        private GenreCode targetGenre;

        private String expectedScale;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProjectRequest {
        private String title;
        private String concept;
        private String targetRegion;
        private GenreCode targetGenre;
        private String expectedScale;
    }
}
