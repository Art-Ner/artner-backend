package kr.artner.domain.venue.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile; // Import MultipartFile

@RestController
@RequestMapping("/api/venue-admin")
public class VenueAdminController {

    @GetMapping("/{venueAdminId}/profile")
    public ResponseEntity<?> getVenueAdminProfile(@PathVariable Long venueAdminId) {
        // TODO: 공간 사업자 프로필 조회
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{venueAdminId}/venue")
    public ResponseEntity<?> getVenueAdminVenues(@PathVariable Long venueAdminId) {
        // TODO: 공간 사업자 공연장 목록
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/profile-image")
    public ResponseEntity<?> updateVenueAdminProfileImage(@RequestParam("file") MultipartFile file) {
        // TODO: 공간 사업자 프로필 이미지 수정
        return ResponseEntity.ok().build();
    }
}
