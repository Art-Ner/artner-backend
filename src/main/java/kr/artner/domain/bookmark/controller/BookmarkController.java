package kr.artner.domain.bookmark.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @PostMapping("/{targetId}")
    public ResponseEntity<?> addBookmark(@PathVariable Long targetId) {
        // TODO: 북마크 추가
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<?> removeBookmark(@PathVariable Long targetId, @RequestParam(value = "target_type", required = false) String targetType) {
        // TODO: 북마크 해제
        return ResponseEntity.ok().build();
    }
}
