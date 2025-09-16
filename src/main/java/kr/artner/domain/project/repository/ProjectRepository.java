package kr.artner.domain.project.repository;

import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p JOIN FETCH p.owner " +
           "WHERE (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "       OR LOWER(p.concept) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR p.status = :status) " +
           "AND (:genre IS NULL OR p.targetGenre = :genre) " +
           "AND (:region IS NULL OR LOWER(p.targetRegion) LIKE LOWER(CONCAT('%', :region, '%'))) " +
           "AND (:ownerId IS NULL OR p.owner.id = :ownerId)")
    Page<Project> findProjectsWithFilters(
            @Param("keyword") String keyword,
            @Param("status") ProjectStatus status,
            @Param("genre") GenreCode genre,
            @Param("region") String region,
            @Param("ownerId") Long ownerId,
            Pageable pageable
    );
}
