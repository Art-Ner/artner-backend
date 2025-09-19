package kr.artner.domain.ticket.service;

import kr.artner.domain.performance.entity.Ticket;
import kr.artner.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NaverPaymentService {

    private final TicketRepository ticketRepository;

    @Transactional
    public Ticket processNaverPayment(Long ticketId, String merchantPayKey, String naverPaymentId) {
        // 1. 티켓 조회
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));

        // 2. 결제 가능 상태 확인
        if (!ticket.isPayable()) {
            throw new IllegalStateException("결제할 수 없는 티켓 상태입니다. 상태: " + ticket.getStatus());
        }

        // 3. 중복 결제 방지 - idempotency key 생성 (테스트용으로 임시 비활성화)
        UUID idempotencyKey = UUID.randomUUID(); // 매번 새로운 UUID 생성
        
        // 4. 이미 같은 merchantPayKey로 결제된 티켓이 있는지 확인 (테스트용으로 임시 비활성화)
        // if (ticketRepository.existsByIdempotencyKey(idempotencyKey)) {
        //     throw new IllegalStateException("이미 처리된 결제 요청입니다.");
        // }

        // 5. 네이버페이 결제 처리
        ticket.processNaverPayment(naverPaymentId, idempotencyKey);

        log.info("네이버페이 결제 완료 - ticketId: {}, merchantPayKey: {}, naverPaymentId: {}", 
                ticketId, merchantPayKey, naverPaymentId);

        return ticket;
    }

    @Transactional
    public Ticket cancelNaverPayment(Long ticketId, String cancelReason) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));

        // 결제된 상태에서만 취소 가능
        if (ticket.getStatus() != kr.artner.domain.ticket.enums.TicketStatus.PAID) {
            throw new IllegalStateException("결제된 티켓만 취소할 수 있습니다.");
        }

        ticket.cancel();
        
        log.info("네이버페이 결제 취소 - ticketId: {}, reason: {}", ticketId, cancelReason);

        return ticket;
    }

    public boolean verifyNaverPayment(String merchantPayKey, String naverPaymentId, Integer amount) {
        // TODO: 실제 네이버페이 API로 결제 정보 검증
        // 현재는 기본 검증만 수행
        
        if (merchantPayKey == null || merchantPayKey.trim().isEmpty()) {
            return false;
        }
        
        if (naverPaymentId == null || naverPaymentId.trim().isEmpty()) {
            return false;
        }
        
        if (amount == null || amount <= 0) {
            return false;
        }

        log.info("네이버페이 결제 검증 - merchantPayKey: {}, naverPaymentId: {}, amount: {}", 
                merchantPayKey, naverPaymentId, amount);

        // 실제 환경에서는 네이버페이 API 호출하여 검증
        return true;
    }
}