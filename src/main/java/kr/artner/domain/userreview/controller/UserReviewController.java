package kr.artner.domain.userreview.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/user-reviews")
public class UserReviewController {

    @PostMapping("/{userId}")
    public ResponseEntity<?> createUserReview(@PathVariable Long userId) {
        // TODO: 유저간 리뷰 등록
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUserReview(@PathVariable Long userId) {
        // TODO: 유저간 리뷰 수정
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserReview(@PathVariable Long userId) {
        // TODO: 유저간 리뷰 삭제
        return ResponseEntity.ok().build();
    }
}
