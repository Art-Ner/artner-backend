package kr.artner.domain.ticket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TicketRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReservationRequest {
        @NotNull(message = "구매자 ID는 필수입니다.")
        private Long buyerId;
        
        @NotNull(message = "가격은 필수입니다.")
        private Integer price;
    }
}
