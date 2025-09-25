package kr.artner.domain.project.repository;

import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p JOIN FETCH p.owner o JOIN FETCH o.user WHERE p.id = :id")
    Optional<Project> findByIdWithOwnerAndUser(@Param("id") Long id);

    @Query("SELECT p FROM Project p JOIN FETCH p.owner " +
           "WHERE (:keyword IS NULL OR p.title = :keyword OR p.concept = :keyword) " +
           "AND (:status IS NULL OR p.status = :status) " +
           "AND (:genre IS NULL OR p.targetGenre = :genre) " +
           "AND (:region IS NULL OR p.targetRegion = :region) " +
           "AND (:ownerId IS NULL OR p.owner.id = :ownerId)")
    Page<Project> findProjectsWithFilters(
            @Param("keyword") String keyword,
            @Param("status") ProjectStatus status,
            @Param("genre") GenreCode genre,
            @Param("region") String region,
            @Param("ownerId") Long ownerId,
            Pageable pageable
    );


    @Query("SELECT p FROM Project p JOIN FETCH p.owner")
    List<Project> findAllProjects();

    @Query("SELECT p FROM Project p WHERE p.conceptEmbedding IS NULL")
    List<Project> findAllByConceptEmbeddingIsNull();

    @Query(value = "SELECT p.*, (p.concept_embedding <=> CAST(:queryVector AS vector)) as similarity_distance " +
           "FROM projects p " +
           "WHERE p.concept_embedding IS NOT NULL " +
           "ORDER BY p.concept_embedding <=> CAST(:queryVector AS vector) " +
           "LIMIT :limit",
           nativeQuery = true)
    List<Object[]> findSimilarProjectsByEmbeddingWithDistance(@Param("queryVector") String queryVector, @Param("limit") int limit);

    @Query(value = "SELECT * FROM projects p " +
           "WHERE p.concept_embedding IS NOT NULL " +
           "ORDER BY p.concept_embedding <=> CAST(:queryVector AS vector) " +
           "LIMIT :limit",
           nativeQuery = true)
    List<Project> findSimilarProjectsByEmbedding(@Param("queryVector") String queryVector, @Param("limit") int limit);

    Page<Project> findAllByOwner(ArtistProfile owner, Pageable pageable);
    Page<Project> findAllByOwnerAndStatus(ArtistProfile owner, ProjectStatus status, Pageable pageable);
    List<Project> findByStatusOrderByCreatedAtDesc(ProjectStatus status);
}
