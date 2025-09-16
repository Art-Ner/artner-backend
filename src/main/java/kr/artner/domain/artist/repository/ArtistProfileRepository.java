package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ArtistProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, Long> {
    
    @Query("SELECT ap FROM ArtistProfile ap WHERE ap.user.id = :userId")
    Optional<ArtistProfile> findByUserId(@Param("userId") Long userId);
}
