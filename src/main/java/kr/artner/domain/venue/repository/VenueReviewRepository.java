package kr.artner.domain.venue.repository;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenueReviewRepository extends JpaRepository<VenueReview, Long> {
    Optional<VenueReview> findByUserAndVenue(User user, Venue venue);
    Page<VenueReview> findByVenueOrderByCreatedAtDesc(Venue venue, Pageable pageable);
}
