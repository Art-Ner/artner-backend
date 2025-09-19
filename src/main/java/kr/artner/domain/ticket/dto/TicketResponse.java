package kr.artner.domain.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class TicketResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceTicketsResponse {
        private List<TicketItem> tickets;
        private PageInfo pageInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketItem {
        private Long id;
        private BuyerInfo buyer;
        private Integer price;
        private String status;
        private LocalDateTime purchasedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuyerInfo {
        private Long id;
        private String username;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private Long totalCount;
        private Integer page;
        private Integer size;
        private Boolean hasMore;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReservationResponse {
        private Long id;
        private Long performanceId;
        private Long buyerId;
        private Integer price;
        private String status;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentResponse {
        private PaymentTicket ticket;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentTicket {
        private Long id;
        private Long performanceId;
        private Long buyerId;
        private Integer price;
        private String status;
        private LocalDateTime purchasedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetailResponse {
        private Long ticketId;
        private Integer amount;
        private String paymentStatus;
        private String externalPaymentId;
        private String idempotencyKey;
        private LocalDateTime paidAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketDetailResponse {
        private Long id;
        private Long performanceId;
        private Long buyerId;
        private Integer price;
        private String status;
        private LocalDateTime holdExpiresAt;
        private LocalDateTime purchasedAt;
        private LocalDateTime cancelledAt;
        private String externalPaymentId;
        private String idempotencyKey;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelResponse {
        private CancelTicket ticket;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelTicket {
        private Long id;
        private Long performanceId;
        private Long buyerId;
        private Integer price;
        private String status;
        private LocalDateTime holdExpiresAt;
        private LocalDateTime purchasedAt;
        private LocalDateTime cancelledAt;
        private String externalPaymentId;
        private String idempotencyKey;
    }
}
