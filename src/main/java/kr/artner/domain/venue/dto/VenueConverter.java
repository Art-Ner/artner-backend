package kr.artner.domain.venue.dto;

import kr.artner.domain.venue.entity.Venue;

public class VenueConverter {

    public static VenueResponse toVenueResponse(Venue venue) {
        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .region(venue.getRegion())
                .address(venue.getAddress())
                .imageUrl(venue.getImageUrl())
                .seatCapacity(venue.getSeatCapacity())
                .baseRentalFee(venue.getBaseRentalFee())
                .description(venue.getDescription())
                .build();
    }
}
