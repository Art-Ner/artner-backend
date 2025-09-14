package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, Long> {
    Optional<ArtistProfile> findByUser(User user);
}
