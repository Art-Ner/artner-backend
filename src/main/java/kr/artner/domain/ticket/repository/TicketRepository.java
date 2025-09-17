package kr.artner.domain.ticket.repository;

import kr.artner.domain.performance.entity.Performance;
import kr.artner.domain.performance.entity.Ticket;
import kr.artner.domain.ticket.enums.TicketStatus;
import kr.artner.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    // 공연별 티켓 조회 (상태 필터 포함)
    Page<Ticket> findByPerformanceAndStatusOrderByPurchasedAtDesc(Performance performance, TicketStatus status, Pageable pageable);
    
    // 공연별 티켓 조회 (모든 상태)
    Page<Ticket> findByPerformanceOrderByPurchasedAtDesc(Performance performance, Pageable pageable);
    
    // 구매자별 티켓 조회
    Page<Ticket> findByBuyerOrderByPurchasedAtDesc(User buyer, Pageable pageable);
}
