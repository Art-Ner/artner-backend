package kr.artner.domain.venue.repository;

import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueAdminProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Page<Venue> findAllByAdminProfileOrderByCreatedAtDesc(VenueAdminProfile adminProfile, Pageable pageable);
    
    // 키워드와 지역 모두 있을 때
    Page<Venue> findByNameContainingIgnoreCaseOrRegionContainingIgnoreCaseAndRegionContainingIgnoreCaseOrderByCreatedAtDesc(
            String nameKeyword, String regionKeyword, String region, Pageable pageable);
    
    // 키워드만 있을 때 (이름 또는 지역 검색)
    Page<Venue> findByNameContainingIgnoreCaseOrRegionContainingIgnoreCaseOrderByCreatedAtDesc(
            String nameKeyword, String regionKeyword, Pageable pageable);
    
    // 지역만 있을 때
    Page<Venue> findByRegionContainingIgnoreCaseOrderByCreatedAtDesc(String region, Pageable pageable);
    
    // 모든 공연장 (필터 없음)
    Page<Venue> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
