package kr.artner.domain.project.repository;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByOwner(ArtistProfile owner, Pageable pageable);
    Page<Project> findAllByOwnerAndStatus(ArtistProfile owner, ProjectStatus status, Pageable pageable);
}
