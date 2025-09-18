package kr.artner.domain.venue.dto;

import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueAvailability;

import java.time.LocalDateTime;

public class VenueAvailabilityConverter {

    public static VenueAvailability toEntity(VenueAvailabilityRequest.CreateAvailabilityRequest request, Venue venue) {
        return VenueAvailability.builder()
                .venue(venue)
                .startDt(request.getStartDt())
                .endDt(request.getEndDt())
                .isBlocked(true) // 불가 일정이므로 항상 true
                .note(request.getNote())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static VenueAvailabilityResponse.CreateAvailabilityResponse toCreateAvailabilityResponse(VenueAvailability availability) {
        return VenueAvailabilityResponse.CreateAvailabilityResponse.builder()
                .id(availability.getId())
                .venueId(availability.getVenue().getId())
                .isBlocked(availability.getIsBlocked())
                .startDt(availability.getStartDt())
                .endDt(availability.getEndDt())
                .note(availability.getNote())
                .build();
    }

    public static VenueAvailabilityResponse.UpdateAvailabilityResponse toUpdateAvailabilityResponse(VenueAvailability availability) {
        return VenueAvailabilityResponse.UpdateAvailabilityResponse.builder()
                .id(availability.getId())
                .venueId(availability.getVenue().getId())
                .isBlocked(availability.getIsBlocked())
                .startDt(availability.getStartDt())
                .endDt(availability.getEndDt())
                .note(availability.getNote())
                .build();
    }
}