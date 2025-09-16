package kr.artner.domain.project.repository;

import kr.artner.domain.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    
    @Query("SELECT pm.project.id, COUNT(pm) FROM ProjectMember pm WHERE pm.project.id IN :projectIds GROUP BY pm.project.id")
    List<Object[]> countMembersByProjectIds(@Param("projectIds") List<Long> projectIds);
    
    @Query("SELECT pm FROM ProjectMember pm JOIN FETCH pm.artist WHERE pm.project.id = :projectId ORDER BY pm.joinedAt")
    List<ProjectMember> findByProjectIdWithArtist(@Param("projectId") Long projectId);
}
