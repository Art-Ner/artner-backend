package kr.artner.domain.performance.service;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.performance.dto.PerformanceConverter;
import kr.artner.domain.performance.dto.PerformanceRequest;
import kr.artner.domain.performance.dto.PerformanceResponse;
import kr.artner.domain.performance.entity.Performance;
import kr.artner.domain.performance.repository.PerformanceRepository;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.ticket.dto.TicketConverter;
import kr.artner.domain.ticket.dto.TicketRequest;
import kr.artner.domain.ticket.dto.TicketResponse;
import kr.artner.domain.ticket.enums.TicketStatus;
import kr.artner.domain.ticket.repository.TicketRepository;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.domain.venue.entity.Venue;
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
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;
    private final VenueRepository venueRepository;
    private final ArtistProfileRepository artistProfileRepository;

    /* ========================
     *  Performance Commands
     * ======================== */

    @Transactional
    public PerformanceResponse.CreatePerformanceResponse createPerformance(
            PerformanceRequest.CreatePerformanceRequest request, Long userId) {

        // 사용자 및 아티스트 프로필 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("아티스트 프로필이 필요합니다."));

        // 프로젝트(옵션)
        Project project = null;
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        }

        // 공연장(옵션)
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

        Performance performance = PerformanceConverter.toEntity(request, project, venue, artistProfile);
        Performance saved = performanceRepository.save(performance);
        return PerformanceConverter.toCreatePerformanceResponse(saved);
    }

    @Transactional
    public PerformanceResponse.PublishPerformanceResponse publishPerformance(Long performanceId, Long userId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        validateUserPermission(userId, performance);
        validatePerformanceForPublish(performance);

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
        // 기본 페이지 옵션
        page = page != null ? page : 0;
        size = size != null ? size : 10;

        Sort sortOption = Sort.by(Sort.Direction.DESC, "startDt");
        if (sort != null) {
            String[] parts = sort.split(",");
            if (parts.length == 2) {
                Sort.Direction dir = "asc".equalsIgnoreCase(parts[1]) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sortOption = Sort.by(dir, parts[0]);
            }
        }
        Pageable pageable = PageRequest.of(page, size, sortOption);

        // (임시) 단순 조회 — 필터 로직은 추후 확장
        if (genreCode != null) {
            try { GenreCode.valueOf(genreCode.toUpperCase()); } // 유효성만 확인
            catch (IllegalArgumentException e) { throw new IllegalArgumentException("유효하지 않은 장르 코드입니다: " + genreCode); }
        }

        Page<Performance> pPage = performanceRepository.findAll(pageable);
        List<PerformanceResponse.PerformanceItem> items = pPage.getContent().stream()
                .map(PerformanceConverter::toPerformanceItem)
                .collect(Collectors.toList());

        PerformanceResponse.PageInfo pageInfo = PerformanceResponse.PageInfo.builder()
                .page(pPage.getNumber())
                .size(pPage.getSize())
                .totalElements(pPage.getTotalElements())
                .totalPages(pPage.getTotalPages())
                .first(pPage.isFirst())
                .last(pPage.isLast())
                .build();

        return PerformanceResponse.PerformanceListResponse.builder()
                .performances(items)
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
            Long performanceId, PerformanceRequest.UpdatePerformanceRequest request, Long userId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        validateUserPermission(userId, performance);

        Project project = null;
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        }

        Venue venue = null;
        if (request.getVenueId() != null) {
            venue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));
        }

        if (request.getStartDt().isAfter(request.getEndDt()) || request.getStartDt().equals(request.getEndDt())) {
            throw new IllegalArgumentException("시작 시각은 종료 시각보다 앞서야 합니다.");
        }

        if (venue != null && (performance.getVenue() == null || !venue.equals(performance.getVenue()))) {
            List<Performance> conflicts = performanceRepository
                    .findByVenueAndStartDtLessThanAndEndDtGreaterThan(venue, request.getEndDt(), request.getStartDt())
                    .stream()
                    .filter(p -> !p.getId().equals(performanceId))
                    .collect(Collectors.toList());
            if (!conflicts.isEmpty()) {
                throw new IllegalArgumentException("해당 시간대에 이미 다른 공연이 예정되어 있습니다.");
            }
        }

        performance.updatePerformance(
                project, venue, request.getTitle(), request.getDescription(),
                request.getGenreCode(), request.getRunningTime(), request.getPosterUrl(),
                request.getStartDt(), request.getEndDt()
        );

        return PerformanceConverter.toUpdatePerformanceResponse(performance);
    }

    @Transactional
    public void deletePerformance(Long performanceId, Long userId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));
        validateUserPermission(userId, performance);
        performanceRepository.delete(performance);
    }

    private void validatePerformanceForPublish(Performance performance) {
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
        if (performance.getStartDt().isAfter(performance.getEndDt()) || performance.getStartDt().equals(performance.getEndDt())) {
            throw new IllegalArgumentException("시작 시각은 종료 시각보다 앞서야 합니다.");
        }
        if (performance.getEndDt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 종료된 공연은 게시할 수 없습니다.");
        }
    }

    private void validateUserPermission(Long userId, Performance performance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        ArtistProfile artist = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("아티스트 프로필이 필요합니다."));
        if (!performance.getOwner().getId().equals(artist.getId())) {
            throw new IllegalArgumentException("해당 공연을 관리할 권한이 없습니다.");
        }
    }

    /* ========================
     *  Ticket Queries/Commands
     * ======================== */

    public TicketResponse.PerformanceTicketsResponse getPerformanceTickets(
            Long performanceId,
            String status,
            Integer page,
            Integer size,
            String sort
    ) {
        // 공연 확인
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        page = page != null ? page : 0;
        size = size != null ? size : 20;

        Sort sortOption = Sort.by(Sort.Direction.DESC, "purchasedAt");
        if (sort != null) {
            String[] parts = sort.split(",");
            if (parts.length == 2) {
                Sort.Direction dir = "asc".equalsIgnoreCase(parts[1]) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sortOption = Sort.by(dir, parts[0]);
            }
        }
        Pageable pageable = PageRequest.of(page, size, sortOption);

        Page<kr.artner.domain.performance.entity.Ticket> ticketPage;
        if (status != null) {
            try {
                TicketStatus ticketStatus = TicketStatus.valueOf(status.toUpperCase());
                ticketPage = ticketRepository.findByPerformanceAndStatusOrderByPurchasedAtDesc(
                        performance, ticketStatus, pageable);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 티켓 상태입니다: " + status);
            }
        } else {
            ticketPage = ticketRepository.findByPerformanceOrderByPurchasedAtDesc(performance, pageable);
        }

        List<TicketResponse.TicketItem> tickets = ticketPage.getContent().stream()
                .map(TicketConverter::toTicketItem)
                .collect(Collectors.toList());

        TicketResponse.PageInfo pageInfo = TicketResponse.PageInfo.builder()
                .totalCount(ticketPage.getTotalElements())
                .page(ticketPage.getNumber())
                .size(ticketPage.getSize())
                .hasMore(!ticketPage.isLast())
                .build();

        return TicketResponse.PerformanceTicketsResponse.builder()
                .tickets(tickets)
                .pageInfo(pageInfo)
                .build();
    }

    @Transactional
    public TicketResponse.CreateReservationResponse createTicketReservation(
            Long performanceId, TicketRequest.CreateReservationRequest request) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (request.getPrice() < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }

        kr.artner.domain.performance.entity.Ticket ticket = kr.artner.domain.performance.entity.Ticket.builder()
                .performance(performance)
                .buyer(buyer)
                .price(request.getPrice())
                .status(TicketStatus.RESERVED)
                .purchasedAt(LocalDateTime.now())
                .build();

        kr.artner.domain.performance.entity.Ticket saved = ticketRepository.save(ticket);
        return TicketConverter.toCreateReservationResponse(saved);
    }
}