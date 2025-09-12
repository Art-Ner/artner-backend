package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ArtistProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, Long> {
}
