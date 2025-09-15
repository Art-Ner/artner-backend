package kr.artner.domain.artist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ConcertHistoryRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateConcertHistory {

        @NotBlank(message = "작품명은 필수입니다.")
        @Size(max = 200, message = "작품명은 200자 이하여야 합니다.")
        private String workTitle;

        @Size(max = 10, message = "역할은 최대 10개까지 선택 가능합니다.")
        private List<String> roleCodes;

        @NotNull(message = "시작일은 필수입니다.")
        private LocalDate startedOn;

        private LocalDate endedOn;

        @Size(max = 255, message = "증빙 URL은 255자 이하여야 합니다.")
        private String proofUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateConcertHistory {

        @NotBlank(message = "작품명은 필수입니다.")
        @Size(max = 200, message = "작품명은 200자 이하여야 합니다.")
        private String workTitle;

        @Size(max = 10, message = "역할은 최대 10개까지 선택 가능합니다.")
        private List<String> roleCodes;

        @NotNull(message = "시작일은 필수입니다.")
        private LocalDate startedOn;

        private LocalDate endedOn;

        @Size(max = 255, message = "증빙 URL은 255자 이하여야 합니다.")
        private String proofUrl;
    }
}