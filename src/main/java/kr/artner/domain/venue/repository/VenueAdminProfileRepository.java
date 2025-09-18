package kr.artner.domain.venue.repository;

import kr.artner.domain.venue.entity.VenueAdminProfile;
import kr.artner.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VenueAdminProfileRepository extends JpaRepository<VenueAdminProfile, Long> {
    Optional<VenueAdminProfile> findByUser(User user);
}