package kr.artner.domain.venue.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @PostMapping
    public ResponseEntity<?> createBooking() {
        // TODO: 대관 요청 생성
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getBookings() {
        // TODO: 대관 요청 목록 조회
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingDetail(@PathVariable Long bookingId) {
        // TODO: 대관 요청 상세 조회
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{bookingId}/approve")
    public ResponseEntity<?> approveBooking(@PathVariable Long bookingId) {
        // TODO: 대관 수락
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{bookingId}/reject")
    public ResponseEntity<?> rejectBooking(@PathVariable Long bookingId) {
        // TODO: 대관 거절
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        // TODO: 대관 취소
        return ResponseEntity.ok().build();
    }
}
