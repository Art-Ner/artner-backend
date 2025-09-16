package kr.artner.domain.project.repository;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.project.entity.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    // 배치 집계: 프로젝트별 멤버 수 (IN 조건 + GROUP BY) — 파생쿼리로 대체 어려워 @Query 유지
    @Query("SELECT pm.project.id AS projectId, COUNT(pm) AS cnt " +
           "FROM ProjectMember pm " +
           "WHERE pm.project.id IN :projectIds " +
           "GROUP BY pm.project.id")
    List<ProjectMemberCount> countMembersByProjectIds(@Param("projectIds") List<Long> projectIds);

    // 페치 전략: 아티스트 즉시 로딩 + 정렬 — @EntityGraph로 fetch join 대체
    @EntityGraph(attributePaths = "artist")
    List<ProjectMember> findByProjectIdOrderByJoinedAt(Long projectId);

    // 아티스트 프로필 기준 페이지 조회 (develop 쪽 메서드 유지)
    Page<ProjectMember> findAllByArtist(ArtistProfile artist, Pageable pageable);

    // Projection for count result
    interface ProjectMemberCount {
        Long getProjectId();
        Long getCnt();
    }
}
