package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.entity.Filmography;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilmographyRepository extends JpaRepository<Filmography, Long> {
    Page<Filmography> findAllByArtistProfileOrderByReleasedAtDesc(ArtistProfile artistProfile, Pageable pageable);

    Optional<Filmography> findByIdAndArtistProfile(Long id, ArtistProfile artistProfile);
}
