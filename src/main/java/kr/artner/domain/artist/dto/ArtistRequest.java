package kr.artner.domain.artist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

public class ArtistRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateArtistProfile {

        @NotBlank(message = "아티스트명은 필수입니다.")
        @Size(max = 100, message = "아티스트명은 100자 이하여야 합니다.")
        private String artistName;

        @NotBlank(message = "헤드라인은 필수입니다.")
        @Size(max = 150, message = "헤드라인은 150자 이하여야 합니다.")
        private String headline;

        @Size(max = 1000, message = "소개는 1000자 이하여야 합니다.")
        private String bio;

        @Size(max = 5, message = "URL은 최대 5개까지 등록 가능합니다.")
        private List<String> urls;

        @Size(max = 10, message = "장르는 최대 10개까지 선택 가능합니다.")
        private List<String> genres;

        @Size(max = 10, message = "역할은 최대 10개까지 선택 가능합니다.")
        private List<String> roles;
    }
}
