package kr.artner.domain.venue.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/venue-reviews")
public class VenueReviewController {

    @PostMapping("/{venueId}")
    public ResponseEntity<?> createVenueReview(@PathVariable Long venueId) {
        // TODO: 유저의 공간 리뷰 등록
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{venueId}")
    public ResponseEntity<?> updateVenueReview(@PathVariable Long venueId) {
        // TODO: 유저의 공간 리뷰 수정
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{venueId}")
    public ResponseEntity<?> deleteVenueReview(@PathVariable Long venueId) {
        // TODO: 유저의 공간 리뷰 삭제
        return ResponseEntity.ok().build();
    }

    @GetMapping("/venues/{venueId}/reviews") // This URL is different from the base mapping
    public ResponseEntity<?> getVenueReviews(
            @PathVariable Long venueId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        // TODO: 공간의 리뷰 목록 조회
        return ResponseEntity.ok().build();
    }
}
