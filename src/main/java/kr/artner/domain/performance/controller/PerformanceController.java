package kr.artner.domain.performance.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/performances")
public class PerformanceController {

    @GetMapping("/{performanceId}")
    public ResponseEntity<?> getPerformanceDetail(@PathVariable Long performanceId) {
        // TODO: 공연 상세 조회
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getPerformances(@RequestParam(value = "keyword", required = false) String keyword) {
        // TODO: 공연 목록 조회
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{performanceId}")
    public ResponseEntity<?> deletePerformance(@PathVariable Long performanceId) {
        // TODO: 공연 삭제
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{performanceId}")
    public ResponseEntity<?> publishPerformance(@PathVariable Long performanceId) {
        // TODO: 공연 최종 게시
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{performanceId}")
    public ResponseEntity<?> updatePerformance(@PathVariable Long performanceId) {
        // TODO: 등록된 공연 수정
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new")
    public ResponseEntity<?> createPerformanceDraft() {
        // TODO: 공연 최초 임시 저장
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{performanceId}/tickets")
    public ResponseEntity<?> getPerformanceTickets(@PathVariable Long performanceId) {
        // TODO: 예매 현황 조회
        return ResponseEntity.ok().build();
    }
}
