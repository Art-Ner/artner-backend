package kr.artner.domain.ticket.dto;

import kr.artner.domain.performance.entity.Ticket;
import kr.artner.domain.ticket.enums.TicketStatus;

public class TicketConverter {
    
    public static TicketResponse.TicketItem toTicketItem(Ticket ticket) {
        return TicketResponse.TicketItem.builder()
                .id(ticket.getId())
                .buyer(TicketResponse.BuyerInfo.builder()
                        .id(ticket.getBuyer().getId())
                        .username(ticket.getBuyer().getUsername())
                        .build())
                .price(ticket.getPrice())
                .status(ticket.getStatus().name())
                .purchasedAt(ticket.getPurchasedAt())
                .build();
    }

    public static TicketResponse.CreateReservationResponse toCreateReservationResponse(Ticket ticket) {
        return TicketResponse.CreateReservationResponse.builder()
                .id(ticket.getId())
                .performanceId(ticket.getPerformance().getId())
                .buyerId(ticket.getBuyer().getId())
                .price(ticket.getPrice())
                .status(ticket.getStatus().name())
                .build();
    }

    public static TicketResponse.PaymentResponse toPaymentResponse(Ticket ticket) {
        return TicketResponse.PaymentResponse.builder()
                .ticket(TicketResponse.PaymentTicket.builder()
                        .id(ticket.getId())
                        .performanceId(ticket.getPerformance().getId())
                        .buyerId(ticket.getBuyer().getId())
                        .price(ticket.getPrice())
                        .status(ticket.getStatus().name())
                        .purchasedAt(ticket.getPurchasedAt())
                        .build())
                .build();
    }

    public static TicketResponse.PaymentDetailResponse toPaymentDetailResponse(Ticket ticket) {
        // 티켓 상태를 결제 상태로 매핑
        String paymentStatus = mapTicketStatusToPaymentStatus(ticket.getStatus());
        
        return TicketResponse.PaymentDetailResponse.builder()
                .ticketId(ticket.getId())
                .amount(ticket.getPrice())
                .paymentStatus(paymentStatus)
                .externalPaymentId(ticket.getExternalPaymentId())
                .idempotencyKey(ticket.getIdempotencyKey() != null ? ticket.getIdempotencyKey().toString() : null)
                .paidAt(ticket.getPurchasedAt())
                .build();
    }

    public static TicketResponse.TicketDetailResponse toTicketDetailResponse(Ticket ticket) {
        return TicketResponse.TicketDetailResponse.builder()
                .id(ticket.getId())
                .performanceId(ticket.getPerformance().getId())
                .buyerId(ticket.getBuyer().getId())
                .price(ticket.getPrice())
                .status(ticket.getStatus().name())
                .holdExpiresAt(ticket.getHoldExpiresAt())
                .purchasedAt(ticket.getPurchasedAt())
                .cancelledAt(ticket.getCancelledAt())
                .externalPaymentId(ticket.getExternalPaymentId())
                .idempotencyKey(ticket.getIdempotencyKey() != null ? ticket.getIdempotencyKey().toString() : null)
                .build();
    }

    public static TicketResponse.CancelResponse toCancelResponse(Ticket ticket) {
        return TicketResponse.CancelResponse.builder()
                .ticket(TicketResponse.CancelTicket.builder()
                        .id(ticket.getId())
                        .performanceId(ticket.getPerformance().getId())
                        .buyerId(ticket.getBuyer().getId())
                        .price(ticket.getPrice())
                        .status(ticket.getStatus().name())
                        .holdExpiresAt(ticket.getHoldExpiresAt())
                        .purchasedAt(ticket.getPurchasedAt())
                        .cancelledAt(ticket.getCancelledAt())
                        .externalPaymentId(ticket.getExternalPaymentId())
                        .idempotencyKey(ticket.getIdempotencyKey() != null ? ticket.getIdempotencyKey().toString() : null)
                        .build())
                .build();
    }

    private static String mapTicketStatusToPaymentStatus(TicketStatus ticketStatus) {
        return switch (ticketStatus) {
            case RESERVED -> "PENDING";
            case PAID -> "SUCCEEDED";
            case CANCELLED -> "CANCELED";
            case REFUNDED -> "REFUNDED";
        };
    }
}
