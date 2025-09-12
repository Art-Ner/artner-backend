package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ConcertHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertHistoryRepository extends JpaRepository<ConcertHistory, Long> {
}
