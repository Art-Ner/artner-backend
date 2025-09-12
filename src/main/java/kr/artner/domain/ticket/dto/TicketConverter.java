package kr.artner.domain.ticket.dto;

import kr.artner.domain.performance.dto.PerformanceConverter;
import kr.artner.domain.performance.entity.Ticket;
import kr.artner.domain.user.dto.UserConverter;

public class TicketConverter {
    public static TicketResponse.GetTicketResponse toGetTicketResponse(Ticket ticket) {
        return TicketResponse.GetTicketResponse.builder()
                .ticketId(ticket.getId())
                .performance(PerformanceConverter.toGetPerformanceResponse(ticket.getPerformance()))
                .buyer(UserConverter.toGetUserInfoResponse(ticket.getBuyer()))
                .price(ticket.getPrice())
                .status(ticket.getStatus().name())
                .purchasedAt(ticket.getPurchasedAt())
                .build();
    }
}
