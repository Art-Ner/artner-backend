package kr.artner.domain.notification.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @PostMapping
    public ResponseEntity<?> sendNotification() { return ResponseEntity.ok().build(); }

    @GetMapping
    public ResponseEntity<?> getNotifications(
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam(value = "kind", required = false) String kind,
            @RequestParam(value = "is_read", required = false) Boolean isRead,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "date_from", required = false) String dateFrom, // Consider using LocalDate or LocalDateTime
            @RequestParam(value = "date_to", required = false) String dateTo    // Consider using LocalDate or LocalDateTime
    ) { return ResponseEntity.ok().build(); }

    @PatchMapping("/{id}")
    public ResponseEntity<?> readNotification(@PathVariable Long id) { return ResponseEntity.ok().build(); }
}
