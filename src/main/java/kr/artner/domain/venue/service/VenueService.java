package kr.artner.domain.venue.service;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.domain.venue.dto.VenueConverter;
import kr.artner.domain.venue.dto.VenueRequest;
import kr.artner.domain.venue.dto.VenueResponse;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueAdminProfile;
import kr.artner.domain.venue.entity.VenueAvailability;
import kr.artner.domain.venue.repository.VenueAdminProfileRepository;
import kr.artner.domain.venue.repository.VenueAvailabilityRepository;
import kr.artner.domain.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueService {
    
    private final VenueRepository venueRepository;
    private final VenueAdminProfileRepository venueAdminProfileRepository;
    private final VenueAvailabilityRepository venueAvailabilityRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public VenueResponse.CreateVenueResponse createVenue(VenueRequest.CreateVenueRequest request, Long adminUserId) {
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        
        // 해당 유저의 VenueAdminProfile을 찾거나 생성
        VenueAdminProfile adminProfile = venueAdminProfileRepository.findByUser(adminUser)
                .orElseThrow(() -> new IllegalArgumentException("공연장 관리자 프로필이 필요합니다."));
        
        Venue venue = VenueConverter.toEntity(request, adminProfile);
        Venue savedVenue = venueRepository.save(venue);
        
        return VenueConverter.toCreateVenueResponse(savedVenue);
    }

    @Transactional
    public VenueResponse.UpdateVenueResponse updateVenue(Long venueId, VenueRequest.UpdateVenueRequest request, Long adminUserId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));
        
        // 공연장 관리자 권한 확인
        if (!venue.getAdminProfile().getUser().getId().equals(adminUserId)) {
            throw new IllegalArgumentException("공연장을 수정할 권한이 없습니다.");
        }
        
        venue.updateVenue(
                request.getName(),
                request.getRegion(),
                request.getAddress(),
                request.getImageUrl(),
                request.getSeatCapacity(),
                request.getBaseRentalFee(),
                request.getDescription()
        );
        
        return VenueConverter.toUpdateVenueResponse(venue);
    }

    @Transactional
    public void deleteVenue(Long venueId, Long adminUserId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));
        
        // 공연장 관리자 권한 확인
        if (!venue.getAdminProfile().getUser().getId().equals(adminUserId)) {
            throw new IllegalArgumentException("공연장을 삭제할 권한이 없습니다.");
        }
        
        venueRepository.delete(venue);
    }

    public VenueResponse.VenueListResponse getVenues(String keyword, String region, Integer limit, Integer offset) {
        // 기본값 설정
        limit = limit != null ? limit : 20;
        offset = offset != null ? offset : 0;
        
        // Pageable 생성 (offset 기반)
        int page = offset / limit;
        org.springframework.data.domain.Pageable pageable = 
                org.springframework.data.domain.PageRequest.of(page, limit);
        
        // 조건에 따른 검색
        org.springframework.data.domain.Page<Venue> venuePage;
        
        if (keyword != null && !keyword.trim().isEmpty() && region != null && !region.trim().isEmpty()) {
            // 키워드와 지역 모두 있을 때
            venuePage = venueRepository.findByNameContainingIgnoreCaseOrRegionContainingIgnoreCaseAndRegionContainingIgnoreCaseOrderByCreatedAtDesc(
                    keyword.trim(), keyword.trim(), region.trim(), pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            // 키워드만 있을 때
            venuePage = venueRepository.findByNameContainingIgnoreCaseOrRegionContainingIgnoreCaseOrderByCreatedAtDesc(
                    keyword.trim(), keyword.trim(), pageable);
        } else if (region != null && !region.trim().isEmpty()) {
            // 지역만 있을 때
            venuePage = venueRepository.findByRegionContainingIgnoreCaseOrderByCreatedAtDesc(region.trim(), pageable);
        } else {
            // 필터 없음
            venuePage = venueRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        
        // DTO 변환
        java.util.List<VenueResponse.VenueItem> venues = venuePage.getContent().stream()
                .map(VenueConverter::toVenueItem)
                .collect(java.util.stream.Collectors.toList());
        
        return VenueResponse.VenueListResponse.builder()
                .venues(venues)
                .limit(limit)
                .offset(offset)
                .total(venuePage.getTotalElements())
                .pageInfo(VenueResponse.VenueListResponse.PageInfo.builder()
                        .totalCount(venuePage.getTotalElements())
                        .limit(limit)
                        .offset(offset)
                        .hasMore(venuePage.hasNext())
                        .build())
                .build();
    }

    public VenueResponse.VenueDetailResponse getVenueDetail(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));
        
        return VenueConverter.toVenueDetailResponse(venue);
    }

    public VenueResponse.VenueCalendarResponse getVenueCalendar(Long venueId, String startDate, String endDate, String month) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));

        // 날짜 범위 결정
        LocalDate rangeStart;
        LocalDate rangeEnd;

        if (month != null && !month.trim().isEmpty()) {
            // 월 단위 조회
            try {
                String[] parts = month.trim().split("-");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("월 형식이 올바르지 않습니다. YYYY-MM 형식을 사용해주세요.");
                }
                int year = Integer.parseInt(parts[0]);
                int monthValue = Integer.parseInt(parts[1]);
                rangeStart = LocalDate.of(year, monthValue, 1);
                rangeEnd = rangeStart.plusMonths(1);
            } catch (Exception e) {
                throw new IllegalArgumentException("월 형식이 올바르지 않습니다. YYYY-MM 형식을 사용해주세요.");
            }
        } else {
            // 시작일/종료일 조회
            if (startDate != null && !startDate.trim().isEmpty()) {
                try {
                    rangeStart = LocalDate.parse(startDate.trim());
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("시작일 형식이 올바르지 않습니다. YYYY-MM-DD 형식을 사용해주세요.");
                }
            } else {
                rangeStart = LocalDate.now();
            }

            if (endDate != null && !endDate.trim().isEmpty()) {
                try {
                    rangeEnd = LocalDate.parse(endDate.trim());
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("종료일 형식이 올바르지 않습니다. YYYY-MM-DD 형식을 사용해주세요.");
                }
            } else {
                rangeEnd = rangeStart.plusMonths(1);
            }
        }

        // VenueAvailability 조회
        LocalDateTime startDateTime = rangeStart.atStartOfDay();
        LocalDateTime endDateTime = rangeEnd.atTime(LocalTime.MAX);
        
        List<VenueAvailability> availabilities = venueAvailabilityRepository
                .findByVenueAndStartDtLessThanEqualAndEndDtGreaterThanEqualOrderByStartDtAsc(venue, endDateTime, startDateTime);

        // 날짜별로 그룹화
        Map<LocalDate, List<VenueAvailability>> dailyAvailabilities = new HashMap<>();
        for (VenueAvailability availability : availabilities) {
            LocalDate date = availability.getStartDt().toLocalDate();
            dailyAvailabilities.computeIfAbsent(date, k -> new ArrayList<>()).add(availability);
        }

        // 응답 생성
        List<VenueResponse.VenueCalendarResponse.DayInfo> days = new ArrayList<>();
        
        for (LocalDate date = rangeStart; date.isBefore(rangeEnd); date = date.plusDays(1)) {
            List<VenueAvailability> dayAvailabilities = dailyAvailabilities.getOrDefault(date, new ArrayList<>());
            
            boolean isBlocked = dayAvailabilities.stream().anyMatch(VenueAvailability::getIsBlocked);
            List<String> notes = dayAvailabilities.stream()
                    .map(VenueAvailability::getNote)
                    .filter(note -> note != null && !note.trim().isEmpty())
                    .distinct()
                    .collect(Collectors.toList());

            days.add(VenueResponse.VenueCalendarResponse.DayInfo.builder()
                    .date(date.toString())
                    .isBlocked(isBlocked)
                    .notes(notes)
                    .build());
        }

        VenueResponse.VenueCalendarResponse.Meta meta = VenueResponse.VenueCalendarResponse.Meta.builder()
                .venueId(venueId)
                .rangeStart(rangeStart.toString())
                .rangeEnd(rangeEnd.toString())
                .timezone("Asia/Seoul")
                .generatedAt(LocalDateTime.now())
                .build();

        return VenueResponse.VenueCalendarResponse.builder()
                .meta(meta)
                .days(days)
                .build();
    }
}
