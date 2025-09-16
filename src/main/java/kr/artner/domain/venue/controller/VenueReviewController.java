package kr.artner.domain.venue.controller;

import jakarta.validation.Valid;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.venue.dto.VenueReviewRequest;
import kr.artner.domain.venue.dto.VenueReviewResponse;
import kr.artner.domain.venue.service.VenueReviewService;
import kr.artner.global.auth.LoginMember;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/venue-reviews")
@RequiredArgsConstructor
public class VenueReviewController {

    private final VenueReviewService venueReviewService;

    @PostMapping("/{venueId}")
    public ApiResponse<VenueReviewResponse.CreateVenueReviewResponse> createVenueReview(
            @LoginMember User user,
            @PathVariable Long venueId,
            @Valid @RequestBody VenueReviewRequest.CreateVenueReview request
    ) {
        VenueReviewResponse.CreateVenueReviewResponse response = venueReviewService.createVenueReview(user, venueId, request);
        return ApiResponse.success(response);
    }

    @PatchMapping("/{venueId}")
    public ApiResponse<VenueReviewResponse.UpdateVenueReviewResponse> updateVenueReview(
            @LoginMember User user,
            @PathVariable Long venueId,
            @Valid @RequestBody VenueReviewRequest.UpdateVenueReview request
    ) {
        VenueReviewResponse.UpdateVenueReviewResponse response = venueReviewService.updateVenueReview(user, venueId, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{venueId}")
    public ApiResponse<Void> deleteVenueReview(
            @LoginMember User user,
            @PathVariable Long venueId
    ) {
        venueReviewService.deleteVenueReview(user, venueId);
        return ApiResponse.success(null);
    }
}
