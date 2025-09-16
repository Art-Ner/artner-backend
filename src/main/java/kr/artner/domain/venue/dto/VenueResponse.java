package kr.artner.domain.venue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class VenueResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateVenueResponse {
        private Long id;
        private Long adminUserId;
        private String name;
        private String region;
        private String address;
        private String imageUrl;
        private Integer seatCapacity;
        private Integer baseRentalFee;
        private String description;
        private String kopisVenueId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateVenueResponse {
        private Long id;
        private Long adminProfileId;
        private String name;
        private String region;
        private String address;
        private String imageUrl;
        private Integer seatCapacity;
        private Integer baseRentalFee;
        private String description;
        private String kopisVenueId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VenueDetailResponse {
        private Long id;
        private String name;
        private String region;
        private String address;
        private Integer seatCapacity;
        private Integer baseRentalFee;
        private String description;
        private String imageUrl;
        private String kopisVenueId;
        private Long adminUserId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VenueCalendarResponse {
        private Meta meta;
        private List<DayInfo> days;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Meta {
            private Long venueId;
            private String rangeStart;
            private String rangeEnd;
            private String timezone;
            private LocalDateTime generatedAt;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DayInfo {
            private String date;
            private Boolean isBlocked;
            private List<String> notes;
        }
    }

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
        private Integer limit;
        private Integer offset;
        private Long total;
        private PageInfo pageInfo;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PageInfo {
            private Long totalCount;
            private Integer limit;
            private Integer offset;
            private Boolean hasMore;
        }
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
