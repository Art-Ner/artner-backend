package kr.artner.domain.ticket.dto;

import kr.artner.domain.performance.dto.PerformanceResponse;
import kr.artner.domain.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TicketResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetTicketResponse {
        private Long ticketId;
        private PerformanceResponse.GetPerformanceResponse performance;
        private UserResponse.GetUserInfoResponse buyer;
        private Integer price;
        private String status;
        private LocalDateTime purchasedAt;
    }
}
