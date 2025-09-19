package kr.artner.domain.ticket.controller;

import kr.artner.domain.performance.entity.Ticket;
import kr.artner.domain.ticket.dto.TicketConverter;
import kr.artner.domain.ticket.dto.TicketResponse;
import kr.artner.domain.ticket.service.NaverPaymentService;
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
    private final NaverPaymentService naverPaymentService;

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

    // === 네이버페이 결제 API ===
    
    @PostMapping("/{ticketId}/payment/naver")
    public ApiResponse<Map<String, Object>> processNaverPayment(
            @PathVariable Long ticketId,
            @RequestBody Map<String, Object> requestBody) {
        
        String merchantPayKey = (String) requestBody.get("merchantPayKey");
        String naverPaymentId = (String) requestBody.get("naverPaymentId");
        Integer amount = (Integer) requestBody.get("amount");
        
        // 1. 네이버페이 결제 정보 검증
        if (!naverPaymentService.verifyNaverPayment(merchantPayKey, naverPaymentId, amount)) {
            throw new IllegalArgumentException("네이버페이 결제 정보가 유효하지 않습니다.");
        }
        
        // 2. 결제 처리
        Ticket ticket = naverPaymentService.processNaverPayment(ticketId, merchantPayKey, naverPaymentId);
        
        return ApiResponse.success(
                "네이버페이 결제가 완료되었습니다.",
                Map.of(
                    "ticketId", ticket.getId(),
                    "status", ticket.getStatus().toString(),
                    "purchasedAt", ticket.getPurchasedAt(),
                    "externalPaymentId", ticket.getExternalPaymentId()
                )
        );
    }

    @PostMapping("/{ticketId}/payment/naver/cancel")
    public ApiResponse<Map<String, Object>> cancelNaverPayment(
            @PathVariable Long ticketId,
            @RequestBody Map<String, String> requestBody) {
        
        String cancelReason = requestBody.getOrDefault("cancelReason", "사용자 요청");
        Ticket ticket = naverPaymentService.cancelNaverPayment(ticketId, cancelReason);
        
        return ApiResponse.success(
                "네이버페이 결제가 취소되었습니다.",
                Map.of(
                    "ticketId", ticket.getId(),
                    "status", ticket.getStatus().toString(),
                    "cancelledAt", ticket.getCancelledAt()
                )
        );
    }
}
