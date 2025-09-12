package kr.artner.domain.bookmark.dto;

import kr.artner.domain.bookmark.entity.Bookmark;

public class BookmarkConverter {

    public static BookmarkResponse.GetBookmarkResponse toGetBookmarkResponse(Bookmark bookmark) {
        return BookmarkResponse.GetBookmarkResponse.builder()
                .id(bookmark.getId())
                .targetType(bookmark.getTargetType().name())
                .targetId(bookmark.getTargetId())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}
