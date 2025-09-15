package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ArtistGenre;
import kr.artner.domain.artist.entity.ArtistGenreId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistGenreRepository extends JpaRepository<ArtistGenre, ArtistGenreId> {
}