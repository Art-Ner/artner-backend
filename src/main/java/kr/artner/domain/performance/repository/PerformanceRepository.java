package kr.artner.domain.performance.repository;

import kr.artner.domain.performance.entity.Performance;
import kr.artner.domain.performance.enums.PerformanceStatus;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.venue.entity.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    
    // 프로젝트별 공연 조회
    List<Performance> findByProjectOrderByStartDtDesc(Project project);
    
    // 공연장별 공연 조회
    List<Performance> findByVenueOrderByStartDtDesc(Venue venue);
    
    // 상태별 공연 조회
    Page<Performance> findByStatusOrderByStartDtDesc(PerformanceStatus status, Pageable pageable);
    
    // 특정 기간 내 공연 조회
    List<Performance> findByStartDtBetweenOrderByStartDtDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    // 공연장과 시간 겹침 확인
    List<Performance> findByVenueAndStartDtLessThanAndEndDtGreaterThan(
            Venue venue, LocalDateTime endDt, LocalDateTime startDt);

    // 복잡한 검색 조건으로 공연 조회
    @Query("""
        SELECT p FROM Performance p 
        LEFT JOIN p.venue v 
        WHERE 
            (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.description LIKE CONCAT('%', :keyword, '%')) 
            AND (:genreCode IS NULL OR p.genreCode = :genreCode)
            AND (:region IS NULL OR v.region = :region)
            AND (:projectId IS NULL OR p.project.id = :projectId)
            AND (:venueId IS NULL OR p.venue.id = :venueId)
            AND (:startDtFrom IS NULL OR p.startDt >= :startDtFrom)
            AND (:startDtTo IS NULL OR p.startDt <= :startDtTo)
        ORDER BY p.startDt DESC
        """)
    Page<Performance> findPerformancesWithFilters(
            @Param("keyword") String keyword,
            @Param("genreCode") kr.artner.domain.common.enums.GenreCode genreCode,
            @Param("region") String region,
            @Param("projectId") Long projectId,
            @Param("venueId") Long venueId,
            @Param("startDtFrom") LocalDateTime startDtFrom,
            @Param("startDtTo") LocalDateTime startDtTo,
            Pageable pageable
    );
}
