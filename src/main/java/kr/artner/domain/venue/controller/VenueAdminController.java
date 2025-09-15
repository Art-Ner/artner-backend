package kr.artner.domain.venue.controller;

import kr.artner.domain.venue.dto.VenueResponse;
import kr.artner.domain.venue.service.VenueAdminService;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/venue-admin")
@RequiredArgsConstructor
public class VenueAdminController {

    private final VenueAdminService venueAdminService;

    @GetMapping("/{venueAdminId}/profile")
    public ApiResponse<VenueResponse.VenueAdminProfileResponse> getVenueAdminProfile(@PathVariable Long venueAdminId) {
        VenueResponse.VenueAdminProfileResponse response = venueAdminService.getVenueAdminProfile(venueAdminId);
        return ApiResponse.success(response);
    }

    @GetMapping("/{venueAdminId}/venue")
    public ApiResponse<VenueResponse.VenueListResponse> getVenueAdminVenues(
            @PathVariable Long venueAdminId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        VenueResponse.VenueListResponse response = venueAdminService.getVenuesByAdmin(venueAdminId, pageable);
        return ApiResponse.success(response);
    }
}
