package kr.artner.domain.venue.repository;

import kr.artner.domain.venue.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
