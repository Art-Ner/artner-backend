package kr.artner.domain.venue.service;

import kr.artner.domain.venue.dto.VenueAvailabilityConverter;
import kr.artner.domain.venue.dto.VenueAvailabilityRequest;
import kr.artner.domain.venue.dto.VenueAvailabilityResponse;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueAvailability;
import kr.artner.domain.venue.repository.VenueAvailabilityRepository;
import kr.artner.domain.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueAvailabilityService {

    private final VenueAvailabilityRepository venueAvailabilityRepository;
    private final VenueRepository venueRepository;

    @Transactional
    public VenueAvailabilityResponse.CreateAvailabilityResponse addUnavailableSlot(
            Long venueId, 
            VenueAvailabilityRequest.CreateAvailabilityRequest request, 
            Long adminUserId
    ) {
        // 공연장 조회
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));

        // 공연장 관리자 권한 확인
        if (!venue.getAdminProfile().getUser().getId().equals(adminUserId)) {
            throw new IllegalArgumentException("해당 공연장의 일정을 관리할 권한이 없습니다.");
        }

        // 시간 검증
        if (request.getStartDt().isAfter(request.getEndDt()) || request.getStartDt().equals(request.getEndDt())) {
            throw new IllegalArgumentException("시작 시각은 종료 시각보다 앞서야 합니다.");
        }

        if (request.getStartDt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 시각으로는 일정을 등록할 수 없습니다.");
        }

        // VenueAvailability 생성
        VenueAvailability availability = VenueAvailabilityConverter.toEntity(request, venue);
        VenueAvailability savedAvailability = venueAvailabilityRepository.save(availability);

        return VenueAvailabilityConverter.toCreateAvailabilityResponse(savedAvailability);
    }

    @Transactional
    public VenueAvailabilityResponse.UpdateAvailabilityResponse updateAvailabilitySlot(
            Long venueId,
            Long availabilityId,
            VenueAvailabilityRequest.UpdateAvailabilityRequest request,
            Long adminUserId
    ) {
        // 공연장 조회
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));

        // 공연장 관리자 권한 확인
        if (!venue.getAdminProfile().getUser().getId().equals(adminUserId)) {
            throw new IllegalArgumentException("해당 공연장의 일정을 관리할 권한이 없습니다.");
        }

        // VenueAvailability 조회
        VenueAvailability availability = venueAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가용성 슬롯입니다."));

        // 해당 공연장의 슬롯인지 확인
        if (!availability.getVenue().getId().equals(venueId)) {
            throw new IllegalArgumentException("해당 공연장의 가용성 슬롯이 아닙니다.");
        }

        // 시간 검증 (값이 제공된 경우만)
        LocalDateTime newStartDt = request.getStartDt() != null ? request.getStartDt() : availability.getStartDt();
        LocalDateTime newEndDt = request.getEndDt() != null ? request.getEndDt() : availability.getEndDt();

        if (newStartDt.isAfter(newEndDt) || newStartDt.equals(newEndDt)) {
            throw new IllegalArgumentException("시작 시각은 종료 시각보다 앞서야 합니다.");
        }

        if (newStartDt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 시각으로는 일정을 설정할 수 없습니다.");
        }

        // 가용성 슬롯 업데이트
        availability.updateAvailability(request.getStartDt(), request.getEndDt(), request.getNote());

        return VenueAvailabilityConverter.toUpdateAvailabilityResponse(availability);
    }

    @Transactional
    public void deleteAvailabilitySlot(Long venueId, Long availabilityId, Long adminUserId) {
        // 공연장 조회
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));

        // 공연장 관리자 권한 확인
        if (!venue.getAdminProfile().getUser().getId().equals(adminUserId)) {
            throw new IllegalArgumentException("해당 공연장의 일정을 관리할 권한이 없습니다.");
        }

        // VenueAvailability 조회
        VenueAvailability availability = venueAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가용성 슬롯입니다."));

        // 해당 공연장의 슬롯인지 확인
        if (!availability.getVenue().getId().equals(venueId)) {
            throw new IllegalArgumentException("해당 공연장의 가용성 슬롯이 아닙니다.");
        }

        // 가용성 슬롯 삭제
        venueAvailabilityRepository.delete(availability);
    }
}