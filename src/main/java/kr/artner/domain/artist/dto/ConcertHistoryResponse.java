package kr.artner.domain.artist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ConcertHistoryResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConcertHistoryItem {
        private Long id;
        private String workTitle;
        private List<String> roleCodes;
        private LocalDate startedOn;
        private LocalDate endedOn;
        private String proofUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConcertHistoryListResponse {
        private List<ConcertHistoryItem> concertHistories;
        private kr.artner.response.PageInfo pageInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateConcertHistoryResponse {
        private Long id;
        private String workTitle;
        private List<String> roleCodes;
        private LocalDate startedOn;
        private LocalDate endedOn;
        private String proofUrl;
        private String message;
    }
}