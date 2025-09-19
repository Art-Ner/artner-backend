package kr.artner.domain.ticket.service;

import kr.artner.domain.performance.entity.Ticket;
import kr.artner.domain.ticket.dto.TicketConverter;
import kr.artner.domain.ticket.dto.TicketResponse;
import kr.artner.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {
    
    private final TicketRepository ticketRepository;

    @Transactional
    public TicketResponse.PaymentResponse processPayment(Long ticketId) {
        // 티켓 조회
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));

        // 결제 처리
        ticket.processPayment();

        return TicketConverter.toPaymentResponse(ticket);
    }

    public TicketResponse.PaymentDetailResponse getPaymentDetail(Long ticketId) {
        // 티켓 조회
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));

        return TicketConverter.toPaymentDetailResponse(ticket);
    }

    public TicketResponse.TicketDetailResponse getTicketDetail(Long ticketId) {
        // 티켓 조회
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));

        return TicketConverter.toTicketDetailResponse(ticket);
    }

    @Transactional
    public TicketResponse.CancelResponse cancelTicket(Long ticketId) {
        // 티켓 조회
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));

        // 티켓 취소
        ticket.cancel();

        return TicketConverter.toCancelResponse(ticket);
    }
}
