package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ArtistGenre;
import kr.artner.domain.artist.entity.ArtistGenreId;
import kr.artner.domain.artist.entity.ArtistProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistGenreRepository extends JpaRepository<ArtistGenre, ArtistGenreId> {
    List<ArtistGenre> findByArtistProfile(ArtistProfile artistProfile);
}