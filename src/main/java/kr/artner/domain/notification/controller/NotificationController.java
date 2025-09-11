package kr.artner.domain.notification.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @PostMapping
    public ResponseEntity<?> sendNotification() { return ResponseEntity.ok().build(); }

    @GetMapping
    public ResponseEntity<?> getNotifications() { return ResponseEntity.ok().build(); }

    @PatchMapping("/{id}")
    public ResponseEntity<?> readNotification(@PathVariable Long id) { return ResponseEntity.ok().build(); }
}
