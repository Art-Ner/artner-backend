package kr.artner.domain.venue.repository;

import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VenueAvailabilityRepository extends JpaRepository<VenueAvailability, Long> {
    
    List<VenueAvailability> findByVenueAndStartDtLessThanEqualAndEndDtGreaterThanEqualOrderByStartDtAsc(
            Venue venue, LocalDateTime endDateTime, LocalDateTime startDateTime);
    
    List<VenueAvailability> findByVenueAndStartDtBetweenOrderByStartDtAsc(
            Venue venue, LocalDateTime rangeStart, LocalDateTime rangeEnd);
            
    List<VenueAvailability> findByVenueOrderByStartDtAsc(Venue venue);
}