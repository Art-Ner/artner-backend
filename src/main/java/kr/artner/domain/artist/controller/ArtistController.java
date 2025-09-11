package kr.artner.domain.artist.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @GetMapping
    public ResponseEntity<?> getArtists(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "role", required = false) String role
    ) {
        // TODO: 아티스트 목록 조회
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{artistId}/profile")
    public ResponseEntity<?> getArtistProfile(@PathVariable Long artistId) {
        // TODO: 아티스트 프로필 조회
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/profile")
    public ResponseEntity<?> updateMyArtistProfile() {
        // TODO: 아티스트 프로필 수정
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/filmography")
    public ResponseEntity<?> uploadFilmography() {
        // TODO: 필모그래피 업로드
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{artistId}/filmography")
    public ResponseEntity<?> getFilmographyList(
            @PathVariable Long artistId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        // TODO: 필모그래피 목록
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{artistId}/filmography/{filmographyId}")
    public ResponseEntity<?> getFilmographyDetail(@PathVariable Long artistId, @PathVariable Long filmographyId) {
        // TODO: 필모그래피 조회
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/filmography/{filmographyId}")
    public ResponseEntity<?> updateFilmography(@PathVariable Long filmographyId) {
        // TODO: 필모그래피 수정
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/filmography/{filmographyId}")
    public ResponseEntity<?> deleteFilmography(@PathVariable Long filmographyId) {
        // TODO: 필모그래피 삭제
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/concert-history")
    public ResponseEntity<?> uploadConcertHistory() {
        // TODO: 공연 이력 등록
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{artistId}/concert-history")
    public ResponseEntity<?> getConcertHistoryList(
            @PathVariable Long artistId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        // TODO: 공연 이력 목록
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{artistId}/concert-history/{historyId}")
    public ResponseEntity<?> getConcertHistoryDetail(@PathVariable Long artistId, @PathVariable Long historyId) {
        // TODO: 공연 이력 조회
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/concert-history/{historyId}")
    public ResponseEntity<?> updateConcertHistory(@PathVariable Long historyId) {
        // TODO: 공연 이력 수정
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/concert-history/{historyId}")
    public ResponseEntity<?> deleteConcertHistory(@PathVariable Long historyId) {
        // TODO: 공연 이력 삭제
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{artistProfileId}/reviews")
    public ResponseEntity<?> getArtistReviews(
            @PathVariable Long artistProfileId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        // TODO: 아티스트 프로필의 유저간 리뷰 목록 조회
        return ResponseEntity.ok().build();
    }
}
