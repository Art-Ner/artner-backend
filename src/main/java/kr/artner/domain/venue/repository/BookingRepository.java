package kr.artner.domain.venue.repository;

import kr.artner.domain.venue.entity.Booking;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.enums.BookingStatus;
import kr.artner.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByVenueAndStartDtLessThanAndEndDtGreaterThan(
            Venue venue, LocalDateTime endDt, LocalDateTime startDt);
    
    List<Booking> findByRequestedByOrderByCreatedAtDesc(User requestedBy);
    
    List<Booking> findByVenueOrderByCreatedAtDesc(Venue venue);

    // 대관 요청 목록 조회용 메서드들
    Page<Booking> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    Page<Booking> findByVenueOrderByCreatedAtDesc(Venue venue, Pageable pageable);
    
    Page<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status, Pageable pageable);
    
    Page<Booking> findByVenueAndStatusOrderByCreatedAtDesc(Venue venue, BookingStatus status, Pageable pageable);
}
