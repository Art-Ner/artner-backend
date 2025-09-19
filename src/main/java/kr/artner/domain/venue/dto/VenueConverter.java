package kr.artner.domain.venue.dto;

import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueAdminProfile;

import java.time.LocalDateTime;

public class VenueConverter {

    public static Venue toEntity(VenueRequest.CreateVenueRequest request, VenueAdminProfile adminProfile) {
        return Venue.builder()
                .adminProfile(adminProfile)
                .name(request.getName())
                .region(request.getRegion())
                .address(request.getAddress())
                .imageUrl(request.getImageUrl())
                .seatCapacity(request.getSeatCapacity())
                .baseRentalFee(request.getBaseRentalFee())
                .description(request.getDescription())
                .source("INTERNAL")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static VenueResponse.CreateVenueResponse toCreateVenueResponse(Venue venue) {
        return VenueResponse.CreateVenueResponse.builder()
                .id(venue.getId())
                .adminUserId(venue.getAdminProfile().getId())
                .name(venue.getName())
                .region(venue.getRegion())
                .address(venue.getAddress())
                .imageUrl(venue.getImageUrl())
                .seatCapacity(venue.getSeatCapacity())
                .baseRentalFee(venue.getBaseRentalFee())
                .description(venue.getDescription())
                .kopisVenueId(venue.getKopisVenueId())
                .createdAt(venue.getCreatedAt())
                .updatedAt(venue.getUpdatedAt())
                .build();
    }

    public static VenueResponse.UpdateVenueResponse toUpdateVenueResponse(Venue venue) {
        return VenueResponse.UpdateVenueResponse.builder()
                .id(venue.getId())
                .adminProfileId(venue.getAdminProfile().getId())
                .name(venue.getName())
                .region(venue.getRegion())
                .address(venue.getAddress())
                .imageUrl(venue.getImageUrl())
                .seatCapacity(venue.getSeatCapacity())
                .baseRentalFee(venue.getBaseRentalFee())
                .description(venue.getDescription())
                .kopisVenueId(venue.getKopisVenueId())
                .createdAt(venue.getCreatedAt())
                .updatedAt(venue.getUpdatedAt())
                .build();
    }

    public static VenueResponse.VenueItem toVenueItem(Venue venue) {
        return VenueResponse.VenueItem.builder()
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

    public static VenueResponse.VenueDetailResponse toVenueDetailResponse(Venue venue) {
        return VenueResponse.VenueDetailResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .region(venue.getRegion())
                .address(venue.getAddress())
                .seatCapacity(venue.getSeatCapacity())
                .baseRentalFee(venue.getBaseRentalFee())
                .description(venue.getDescription())
                .imageUrl(venue.getImageUrl())
                .kopisVenueId(venue.getKopisVenueId())
                .adminUserId(venue.getAdminProfile().getUser().getId())
                .createdAt(venue.getCreatedAt())
                .updatedAt(venue.getUpdatedAt())
                .build();
    }
}
