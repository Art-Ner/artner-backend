package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.Filmography;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmographyRepository extends JpaRepository<Filmography, Long> {
}
