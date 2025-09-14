package kr.artner.domain.project.repository;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.project.entity.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Page<ProjectMember> findAllByArtist(ArtistProfile artist, Pageable pageable);
}
