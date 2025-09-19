package kr.artner.domain.venue.controller;

import kr.artner.domain.venue.service.KopisVenueSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/venues/kopis")
@RequiredArgsConstructor
public class KopisVenueController {

    private final KopisVenueSyncService kopisVenueSyncService;

    @PostMapping("/sync")
    public ResponseEntity<Map<String, String>> syncVenues() {
        try {
            log.info("Manual KOPIS venue synchronization requested");
            kopisVenueSyncService.syncVenueData();

            return ResponseEntity.ok(Map.of(
                "message", "KOPIS 공연장 데이터 동기화가 완료되었습니다.",
                "status", "success"
            ));
        } catch (Exception e) {
            log.error("Failed to sync KOPIS venue data", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "message", "KOPIS 공연장 데이터 동기화 중 오류가 발생했습니다: " + e.getMessage(),
                "status", "error"
            ));
        }
    }
}