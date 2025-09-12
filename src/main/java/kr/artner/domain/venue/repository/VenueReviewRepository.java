package kr.artner.domain.venue.repository;

import kr.artner.domain.venue.entity.VenueReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueReviewRepository extends JpaRepository<VenueReview, Long> {
}
