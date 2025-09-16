package kr.artner.domain.venue.service;

import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.domain.venue.dto.BookingConverter;
import kr.artner.domain.venue.dto.BookingRequest;
import kr.artner.domain.venue.dto.BookingResponse;
import kr.artner.domain.venue.entity.Booking;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.enums.BookingStatus;
import kr.artner.domain.venue.repository.BookingRepository;
import kr.artner.domain.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VenueRepository venueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponse.CreateBookingResponse createBooking(BookingRequest.CreateBookingRequest request, Long userId) {
        
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 공연장 조회
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));

        // 프로젝트 조회
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        // 시간 검증
        if (request.getStartDt().isAfter(request.getEndDt())) {
            throw new IllegalArgumentException("시작 시각은 종료 시각보다 앞서야 합니다.");
        }

        if (request.getStartDt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("시작 시각은 현재 시각보다 미래여야 합니다.");
        }

        // 겹치는 예약 확인
        List<Booking> conflictingBookings = bookingRepository
                .findByVenueAndStartDtLessThanAndEndDtGreaterThan(venue, request.getEndDt(), request.getStartDt());

        if (!conflictingBookings.isEmpty()) {
            throw new IllegalArgumentException("해당 시간대에 이미 예약이 존재합니다.");
        }

        // 예약 생성
        Booking booking = BookingConverter.toEntity(request, user, venue, project);
        Booking savedBooking = bookingRepository.save(booking);

        return BookingConverter.toCreateBookingResponse(savedBooking);
    }

    public BookingResponse.BookingListResponse getBookings(Long venueId, String status, Integer limit, Integer offset) {
        // 기본값 설정
        limit = limit != null ? limit : 20;
        offset = offset != null ? offset : 0;
        
        // Pageable 생성 (offset 기반)
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        
        // 조건에 따른 검색
        Page<Booking> bookingPage;
        
        if (venueId != null && status != null && !status.trim().isEmpty()) {
            // 공연장 ID와 상태 모두 있을 때
            Venue venue = venueRepository.findById(venueId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));
            BookingStatus bookingStatus = BookingStatus.valueOf(status.trim().toUpperCase());
            bookingPage = bookingRepository.findByVenueAndStatusOrderByCreatedAtDesc(venue, bookingStatus, pageable);
        } else if (venueId != null) {
            // 공연장 ID만 있을 때
            Venue venue = venueRepository.findById(venueId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));
            bookingPage = bookingRepository.findByVenueOrderByCreatedAtDesc(venue, pageable);
        } else if (status != null && !status.trim().isEmpty()) {
            // 상태만 있을 때
            BookingStatus bookingStatus = BookingStatus.valueOf(status.trim().toUpperCase());
            bookingPage = bookingRepository.findByStatusOrderByCreatedAtDesc(bookingStatus, pageable);
        } else {
            // 필터 없음
            bookingPage = bookingRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        
        // DTO 변환
        List<BookingResponse.BookingItem> bookings = bookingPage.getContent().stream()
                .map(BookingConverter::toBookingItem)
                .collect(Collectors.toList());
        
        return BookingResponse.BookingListResponse.builder()
                .bookings(bookings)
                .pageInfo(BookingResponse.BookingListResponse.PageInfo.builder()
                        .totalCount(bookingPage.getTotalElements())
                        .limit(limit)
                        .offset(offset)
                        .hasMore(bookingPage.hasNext())
                        .build())
                .build();
    }

    public BookingResponse.BookingDetailResponse getBookingDetail(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대관 요청입니다."));
        
        return BookingConverter.toBookingDetailResponse(booking);
    }

    @Transactional
    public BookingResponse.BookingApprovalResponse approveBooking(Long bookingId, Long adminUserId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대관 요청입니다."));

        // 공연장 관리자 권한 확인
        if (!booking.getVenue().getAdminProfile().getUser().getId().equals(adminUserId)) {
            throw new IllegalArgumentException("해당 공연장의 대관 요청을 승인할 권한이 없습니다.");
        }

        // 승인 처리
        booking.approve();
        
        return BookingConverter.toBookingApprovalResponse(booking);
    }

    @Transactional
    public BookingResponse.BookingRejectionResponse rejectBooking(Long bookingId, Long adminUserId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대관 요청입니다."));

        // 공연장 관리자 권한 확인
        if (!booking.getVenue().getAdminProfile().getUser().getId().equals(adminUserId)) {
            throw new IllegalArgumentException("해당 공연장의 대관 요청을 거절할 권한이 없습니다.");
        }

        // 거절 처리
        booking.reject();
        
        return BookingConverter.toBookingRejectionResponse(booking);
    }

    @Transactional
    public BookingResponse.BookingCancellationResponse cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대관 요청입니다."));

        // 요청자 본인만 취소 가능
        if (!booking.getRequestedBy().getId().equals(userId)) {
            throw new IllegalArgumentException("본인이 요청한 대관만 취소할 수 있습니다.");
        }

        // 취소 처리
        booking.cancel();
        
        return BookingConverter.toBookingCancellationResponse(booking);
    }
}