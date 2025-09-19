package kr.artner.domain.artist.repository;

import kr.artner.domain.artist.entity.ArtistRole;
import kr.artner.domain.artist.entity.ArtistRoleId;
import kr.artner.domain.artist.entity.ArtistProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRoleRepository extends JpaRepository<ArtistRole, ArtistRoleId> {
    List<ArtistRole> findByArtistProfile(ArtistProfile artistProfile);
}