package kr.artner.domain.ticket.repository;

import kr.artner.domain.performance.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
