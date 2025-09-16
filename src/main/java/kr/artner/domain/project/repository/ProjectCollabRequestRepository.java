package kr.artner.domain.project.repository;

import kr.artner.domain.project.entity.ProjectCollabRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectCollabRequestRepository extends JpaRepository<ProjectCollabRequest, Long> {
    
    @Query("SELECT pcr FROM ProjectCollabRequest pcr WHERE pcr.project.id = :projectId AND pcr.requester.id = :requesterId")
    Optional<ProjectCollabRequest> findByProjectIdAndRequesterId(@Param("projectId") Long projectId, @Param("requesterId") Long requesterId);
}
