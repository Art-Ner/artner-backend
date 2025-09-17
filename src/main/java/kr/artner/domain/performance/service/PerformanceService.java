package kr.artner.domain.performance.service;

import kr.artner.domain.performance.dto.PerformanceConverter;
import kr.artner.domain.performance.dto.PerformanceRequest;
import kr.artner.domain.performance.dto.PerformanceResponse;
import kr.artner.domain.performance.entity.Performance;
import kr.artner.domain.performance.repository.PerformanceRepository;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.enums.GenreCode;
import kr.artner.domain.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final ProjectRepository projectRepository;
    private final VenueRepository venueRepository;

    @Transactional
    public PerformanceResponse.CreatePerformanceResponse createPerformance(
            PerformanceRequest.CreatePerformanceRequest request) {
        
        // 프로젝트 조회 (선택사항)
        Project project = null;
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        }

        // 공연장 조회 (선택사항)
        Venue venue = null;
        if (request.getVenueId() != null) {
            venue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));
        }

        // 시간 검증
        if (request.getStartDt().isAfter(request.getEndDt()) || request.getStartDt().equals(request.getEndDt())) {
            throw new IllegalArgumentException("시작 시각은 종료 시각보다 앞서야 합니다.");
        }

        // 공연장이 지정된 경우 시간 겹침 확인
        if (venue != null) {
            List<Performance> conflictingPerformances = performanceRepository
                    .findByVenueAndStartDtLessThanAndEndDtGreaterThan(venue, request.getEndDt(), request.getStartDt());
            
            if (!conflictingPerformances.isEmpty()) {
                throw new IllegalArgumentException("해당 시간대에 이미 다른 공연이 예정되어 있습니다.");
            }
        }

        // 공연 생성
        Performance performance = PerformanceConverter.toEntity(request, project, venue);
        Performance savedPerformance = performanceRepository.save(performance);

        return PerformanceConverter.toCreatePerformanceResponse(savedPerformance);
    }

    @Transactional
    public PerformanceResponse.PublishPerformanceResponse publishPerformance(Long performanceId) {
        // 공연 조회
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        // 게시 가능성 검증
        validatePerformanceForPublish(performance);

        // 공연 게시
        performance.publish();

        return PerformanceConverter.toPublishPerformanceResponse(performance);
    }

    public PerformanceResponse.PerformanceListResponse getPerformances(
            String keyword,
            String genreCode,
            String region,
            Long projectId,
            Long venueId,
            LocalDateTime startDtFrom,
            LocalDateTime startDtTo,
            Integer page,
            Integer size,
            String sort
    ) {
        // 기본값 설정
        page = page != null ? page : 0;
        size = size != null ? size : 10;
        
        // 정렬 설정
        Sort sortOption = Sort.by(Sort.Direction.DESC, "startDt");
        if (sort != null) {
            String[] sortParts = sort.split(",");
            if (sortParts.length == 2) {
                Sort.Direction direction = "asc".equalsIgnoreCase(sortParts[1]) ? 
                    Sort.Direction.ASC : Sort.Direction.DESC;
                sortOption = Sort.by(direction, sortParts[0]);
            }
        }
        
        Pageable pageable = PageRequest.of(page, size, sortOption);
        
        // GenreCode 변환
        GenreCode genreCodeEnum = null;
        if (genreCode != null) {
            try {
                genreCodeEnum = GenreCode.valueOf(genreCode.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 장르 코드입니다: " + genreCode);
            }
        }
        
        // 페이징된 결과 조회
        Page<Performance> performancePage = performanceRepository.findPerformancesWithFilters(
                keyword,
                genreCodeEnum,
                region,
                projectId,
                venueId,
                startDtFrom,
                startDtTo,
                pageable
        );
        
        // DTO 변환
        List<PerformanceResponse.PerformanceItem> performances = performancePage.getContent()
                .stream()
                .map(PerformanceConverter::toPerformanceItem)
                .collect(Collectors.toList());
        
        // 페이지 정보 구성
        PerformanceResponse.PageInfo pageInfo = PerformanceResponse.PageInfo.builder()
                .page(performancePage.getNumber())
                .size(performancePage.getSize())
                .totalElements(performancePage.getTotalElements())
                .totalPages(performancePage.getTotalPages())
                .first(performancePage.isFirst())
                .last(performancePage.isLast())
                .build();
        
        return PerformanceResponse.PerformanceListResponse.builder()
                .performances(performances)
                .pageInfo(pageInfo)
                .build();
    }

    public PerformanceResponse.PerformanceDetailResponse getPerformanceDetail(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));
        
        return PerformanceConverter.toPerformanceDetailResponse(performance);
    }

    @Transactional
    public PerformanceResponse.UpdatePerformanceResponse updatePerformance(
            Long performanceId, PerformanceRequest.UpdatePerformanceRequest request) {
        
        // 공연 조회
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        // 프로젝트 조회 (선택사항)
        Project project = null;
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        }

        // 공연장 조회 (선택사항)
        Venue venue = null;
        if (request.getVenueId() != null) {
            venue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));
        }

        // 시간 검증
        if (request.getStartDt().isAfter(request.getEndDt()) || request.getStartDt().equals(request.getEndDt())) {
            throw new IllegalArgumentException("시작 시각은 종료 시각보다 앞서야 합니다.");
        }

        // 공연장이 변경된 경우 시간 겹침 확인
        if (venue != null && !venue.equals(performance.getVenue())) {
            List<Performance> conflictingPerformances = performanceRepository
                    .findByVenueAndStartDtLessThanAndEndDtGreaterThan(venue, request.getEndDt(), request.getStartDt());
            
            // 자기 자신은 제외
            conflictingPerformances = conflictingPerformances.stream()
                    .filter(p -> !p.getId().equals(performanceId))
                    .collect(Collectors.toList());
            
            if (!conflictingPerformances.isEmpty()) {
                throw new IllegalArgumentException("해당 시간대에 이미 다른 공연이 예정되어 있습니다.");
            }
        }

        // 공연 정보 업데이트
        performance.updatePerformance(
                project, venue, request.getTitle(), request.getDescription(),
                request.getGenreCode(), request.getRunningTime(), request.getPosterUrl(),
                request.getStartDt(), request.getEndDt()
        );

        return PerformanceConverter.toUpdatePerformanceResponse(performance);
    }

    @Transactional
    public void deletePerformance(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));
        
        performanceRepository.delete(performance);
    }

    private void validatePerformanceForPublish(Performance performance) {
        // 필수 항목 검증
        if (performance.getTitle() == null || performance.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("공연 제목은 필수입니다.");
        }

        if (performance.getDescription() == null || performance.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("공연 내용은 필수입니다.");
        }

        if (performance.getGenreCode() == null) {
            throw new IllegalArgumentException("장르 코드는 필수입니다.");
        }

        if (performance.getStartDt() == null || performance.getEndDt() == null) {
            throw new IllegalArgumentException("공연 시작/종료 시각은 필수입니다.");
        }

        // 시간 검증
        if (performance.getStartDt().isAfter(performance.getEndDt()) || 
            performance.getStartDt().equals(performance.getEndDt())) {
            throw new IllegalArgumentException("시작 시각은 종료 시각보다 앞서야 합니다.");
        }

        // 과거 공연 게시 방지 (선택적)
        if (performance.getEndDt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 종료된 공연은 게시할 수 없습니다.");
        }
    }
}
