package kr.artner.domain.venue.repository;

import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueAdminProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Page<Venue> findAllByAdminProfileOrderByCreatedAtDesc(VenueAdminProfile adminProfile, Pageable pageable);
}
