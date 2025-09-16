package kr.artner.domain.venue.controller;

import jakarta.validation.Valid;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.venue.dto.VenueAvailabilityRequest;
import kr.artner.domain.venue.dto.VenueAvailabilityResponse;
import kr.artner.domain.venue.dto.VenueRequest;
import kr.artner.domain.venue.dto.VenueResponse;
import kr.artner.domain.venue.service.VenueAvailabilityService;
import kr.artner.domain.venue.service.VenueService;
import kr.artner.global.auth.LoginMember;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {
    
    private final VenueService venueService;
    private final VenueAvailabilityService venueAvailabilityService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, Object>> createVenue(
            @LoginMember User user,
            @RequestBody @Valid VenueRequest.CreateVenueRequest request
    ) {
        VenueResponse.CreateVenueResponse response = venueService.createVenue(request, user.getId());
        
        return ApiResponse.success(
                "공연장이 등록되었습니다.",
                Map.of("venue", response)
        );
    }

    @PatchMapping("/{venueId}")
    public ApiResponse<Map<String, Object>> updateVenue(
            @PathVariable Long venueId,
            @LoginMember User user,
            @RequestBody @Valid VenueRequest.UpdateVenueRequest request
    ) {
        VenueResponse.UpdateVenueResponse response = venueService.updateVenue(venueId, request, user.getId());
        
        return ApiResponse.success(
                "공연장 정보가 수정되었습니다.",
                Map.of("venue", response)
        );
    }

    @DeleteMapping("/{venueId}")
    public ApiResponse<Void> deleteVenue(
            @PathVariable Long venueId,
            @LoginMember User user
    ) {
        venueService.deleteVenue(venueId, user.getId());
        
        return ApiResponse.success("공연장이 삭제되었습니다.", null);
    }

    @GetMapping
    public ApiResponse<VenueResponse.VenueListResponse> getVenues(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        VenueResponse.VenueListResponse response = venueService.getVenues(keyword, region, limit, offset);
        
        return ApiResponse.success("공연장 목록을 불러왔습니다.", response);
    }

    @GetMapping("/{venueId}")
    public ApiResponse<Map<String, Object>> getVenueDetail(@PathVariable Long venueId) {
        VenueResponse.VenueDetailResponse response = venueService.getVenueDetail(venueId);
        
        return ApiResponse.success(
                "공연장 정보를 불러왔습니다.",
                Map.of("venue", response)
        );
    }

    @GetMapping("/{venueId}/calendar")
    public ApiResponse<VenueResponse.VenueCalendarResponse> getVenueCalendar(
            @PathVariable Long venueId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "month", required = false) String month
    ) {
        VenueResponse.VenueCalendarResponse response = venueService.getVenueCalendar(venueId, startDate, endDate, month);
        
        return ApiResponse.success("공연장 일정을 불러왔습니다.", response);
    }

    @PostMapping("/{venueId}/availability")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, Object>> addVenueAvailability(
            @PathVariable Long venueId,
            @LoginMember User user,
            @RequestBody @Valid VenueAvailabilityRequest.CreateAvailabilityRequest request
    ) {
        VenueAvailabilityResponse.CreateAvailabilityResponse response = 
                venueAvailabilityService.addUnavailableSlot(venueId, request, user.getId());
        
        return ApiResponse.success(
                "대관 불가 일정이 추가되었습니다.",
                Map.of("availability", response)
        );
    }

    @PatchMapping("/{venueId}/availability/{availabilityId}")
    public ApiResponse<Map<String, Object>> updateVenueAvailability(
            @PathVariable Long venueId,
            @PathVariable Long availabilityId,
            @LoginMember User user,
            @RequestBody @Valid VenueAvailabilityRequest.UpdateAvailabilityRequest request
    ) {
        VenueAvailabilityResponse.UpdateAvailabilityResponse response = 
                venueAvailabilityService.updateAvailabilitySlot(venueId, availabilityId, request, user.getId());
        
        return ApiResponse.success(
                "대관 불가 일정이 수정되었습니다.",
                Map.of("availability", response)
        );
    }

    @DeleteMapping("/{venueId}/availability/{availabilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteVenueAvailability(
            @PathVariable Long venueId,
            @PathVariable Long availabilityId,
            @LoginMember User user
    ) {
        venueAvailabilityService.deleteAvailabilitySlot(venueId, availabilityId, user.getId());
        
        return ApiResponse.success("대관 불가 일정이 삭제되었습니다.", null);
    }
}
