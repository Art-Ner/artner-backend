package kr.artner.domain.artist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class FilmographyRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateFilmography {

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 150, message = "제목은 150자 이하여야 합니다.")
        private String title;

        @Size(max = 1000, message = "설명은 1000자 이하여야 합니다.")
        private String description;

        @NotNull(message = "발표일은 필수입니다.")
        private LocalDate releasedAt;

        @Size(max = 255, message = "미디어 URL은 255자 이하여야 합니다.")
        private String mediaUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateFilmography {

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 150, message = "제목은 150자 이하여야 합니다.")
        private String title;

        @Size(max = 1000, message = "설명은 1000자 이하여야 합니다.")
        private String description;

        @NotNull(message = "발표일은 필수입니다.")
        private LocalDate releasedAt;

        @Size(max = 255, message = "미디어 URL은 255자 이하여야 합니다.")
        private String mediaUrl;
    }
}