package kr.artner.domain.ticket.controller;

import kr.artner.domain.ticket.dto.TicketResponse;
import kr.artner.domain.ticket.service.TicketService;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    
    private final TicketService ticketService;

    @GetMapping("/{ticketId}")
    public ApiResponse<TicketResponse.TicketDetailResponse> getTicketDetail(@PathVariable Long ticketId) {
        TicketResponse.TicketDetailResponse response = ticketService.getTicketDetail(ticketId);
        
        return ApiResponse.success(
                "예매 정보를 불러왔습니다.",
                response
        );
    }

    @PostMapping("/{ticketId}/hold")
    public ResponseEntity<?> holdTicket(@PathVariable Long ticketId) { return ResponseEntity.ok().build(); }

    @PostMapping("/{ticketId}/payment")
    public ApiResponse<Map<String, Object>> processPayment(@PathVariable Long ticketId) {
        TicketResponse.PaymentResponse response = ticketService.processPayment(ticketId);
        
        return ApiResponse.success(
                "결제가 완료되었습니다.",
                Map.of("ticket", response.getTicket())
        );
    }

    @GetMapping("/{ticketId}/payment")
    public ApiResponse<TicketResponse.PaymentDetailResponse> getPaymentDetail(@PathVariable Long ticketId) {
        TicketResponse.PaymentDetailResponse response = ticketService.getPaymentDetail(ticketId);
        
        return ApiResponse.success(
                "결제 정보를 불러왔습니다.",
                response
        );
    }

    @DeleteMapping("/{ticketId}")
    public ApiResponse<Map<String, Object>> cancelTicket(@PathVariable Long ticketId) {
        TicketResponse.CancelResponse response = ticketService.cancelTicket(ticketId);
        
        return ApiResponse.success(
                "예매가 취소되었습니다.",
                Map.of("ticket", response.getTicket())
        );
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyTickets() { return ResponseEntity.ok().build(); }
}
