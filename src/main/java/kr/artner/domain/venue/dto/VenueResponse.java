package kr.artner.domain.venue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class VenueResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VenueItem {
        private Long id;
        private String name;
        private String region;
        private String address;
        private String imageUrl;
        private Integer seatCapacity;
        private Integer baseRentalFee;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VenueListResponse {
        private List<VenueItem> venues;
        private kr.artner.response.PageInfo pageInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VenueAdminProfileResponse {
        private Long id;
        private String profileImageUrl;
        private String businessRegNumber;
        private String businessName;
        private String phone;
        private String description;
        private String userEmail;
        private String userName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateVenueAdminProfileResponse {
        private Long id;
        private String businessRegNumber;
        private String businessName;
        private String phone;
        private String description;
        private String profileImageUrl;
    }
}
