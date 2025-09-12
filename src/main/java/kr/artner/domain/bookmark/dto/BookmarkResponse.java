package kr.artner.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BookmarkResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetBookmarkResponse {
        private Long id;
        private String targetType;
        private Long targetId;
        private LocalDateTime createdAt;
    }
}
