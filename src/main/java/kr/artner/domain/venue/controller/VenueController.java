package kr.artner.domain.venue.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/venues")
public class VenueController {
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
}
