package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.entity.ConcertHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConcertHistoryRepository extends JpaRepository<ConcertHistory, Long> {
    Page<ConcertHistory> findAllByArtistProfileOrderByStartedOnDesc(ArtistProfile artistProfile, Pageable pageable);

    Optional<ConcertHistory> findByIdAndArtistProfile(Long id, ArtistProfile artistProfile);
}
