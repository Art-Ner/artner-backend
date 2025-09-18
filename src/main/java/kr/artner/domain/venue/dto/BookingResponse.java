package kr.artner.domain.venue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBookingResponse {
        private Long id;
        private Long requestedBy;
        private Long venueId;
        private Long projectId;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private String status;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingItem {
        private Long id;
        private Long venueId;
        private Long projectId;
        private Long requestedBy;
        private String status;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private LocalDateTime decidedAt;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingDetailResponse {
        private Long id;
        private Long venueId;
        private Long projectId;
        private Long requestedBy;
        private String status;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private LocalDateTime decidedAt;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingListResponse {
        private List<BookingItem> bookings;
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
    public static class BookingApprovalResponse {
        private Long id;
        private Long venueId;
        private Long projectId;
        private Long requestedBy;
        private String status;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private LocalDateTime decidedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingRejectionResponse {
        private Long id;
        private Long venueId;
        private Long projectId;
        private Long requestedBy;
        private String status;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private LocalDateTime decidedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingCancellationResponse {
        private Long id;
        private Long venueId;
        private Long projectId;
        private Long requestedBy;
        private String status;
        private LocalDateTime startDt;
        private LocalDateTime endDt;
        private LocalDateTime decidedAt;
    }
}