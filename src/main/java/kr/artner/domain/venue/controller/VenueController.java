package kr.artner.domain.venue.controller;

import kr.artner.domain.venue.dto.VenueReviewResponse;
import kr.artner.domain.venue.service.VenueReviewService;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueReviewService venueReviewService;
    @PostMapping
    public ResponseEntity<?> createVenue() { return ResponseEntity.ok().build(); }

    @PatchMapping("/{venueId}")
    public ResponseEntity<?> updateVenue(@PathVariable Long venueId) { return ResponseEntity.ok().build(); }

    @DeleteMapping("/{venueId}")
    public ResponseEntity<?> deleteVenue(@PathVariable Long venueId) { return ResponseEntity.ok().build(); }

    @GetMapping
    public ResponseEntity<?> getVenues(@RequestParam(value = "keyword", required = false) String keyword) { return ResponseEntity.ok().build(); }

    @GetMapping("/{venueId}")
    public ResponseEntity<?> getVenueDetail(@PathVariable Long venueId) { return ResponseEntity.ok().build(); }

    @GetMapping("/{venueId}/calendar")
    public ResponseEntity<?> getVenueCalendar(@PathVariable Long venueId) { return ResponseEntity.ok().build(); }

    @PostMapping("/{venueId}/availability")
    public ResponseEntity<?> addVenueAvailability(@PathVariable Long venueId) { return ResponseEntity.ok().build(); }

    @DeleteMapping("/{venueId}/availability")
    public ResponseEntity<?> deleteVenueAvailability(@PathVariable Long venueId) { return ResponseEntity.ok().build(); }

    @GetMapping("/{venueId}/reviews")
    public ApiResponse<VenueReviewResponse.GetVenueReviewsResponse> getVenueReviews(
            @PathVariable Long venueId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        VenueReviewResponse.GetVenueReviewsResponse response = venueReviewService.getVenueReviews(venueId, page, size);
        return ApiResponse.success(response);
    }
}
