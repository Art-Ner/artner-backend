package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ArtistRole;
import kr.artner.domain.artist.entity.ArtistRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRoleRepository extends JpaRepository<ArtistRole, ArtistRoleId> {
}