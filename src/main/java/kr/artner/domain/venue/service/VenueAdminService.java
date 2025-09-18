package kr.artner.domain.venue.service;

import kr.artner.domain.venue.dto.VenueRequest;
import kr.artner.domain.venue.dto.VenueResponse;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueAdminProfile;
import kr.artner.domain.venue.repository.VenueAdminProfileRepository;
import kr.artner.domain.venue.repository.VenueRepository;
import kr.artner.domain.user.entity.User;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import kr.artner.response.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueAdminService {

    private final VenueAdminProfileRepository venueAdminProfileRepository;
    private final VenueRepository venueRepository;

    @Transactional(readOnly = true)
    public VenueResponse.VenueAdminProfileResponse getVenueAdminProfile(Long venueAdminId) {
        VenueAdminProfile venueAdminProfile = venueAdminProfileRepository.findById(venueAdminId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.VENUE_ADMIN_PROFILE_NOT_FOUND));

        return VenueResponse.VenueAdminProfileResponse.builder()
                .id(venueAdminProfile.getId())
                .profileImageUrl(venueAdminProfile.getProfileImageUrl())
                .businessRegNumber(venueAdminProfile.getBusinessRegNumber())
                .businessName(venueAdminProfile.getBusinessName())
                .phone(venueAdminProfile.getPhone())
                .description(venueAdminProfile.getDescription())
                .userEmail(venueAdminProfile.getUser().getEmail())
                .userName(venueAdminProfile.getUser().getUsername())
                .build();
    }

    @Transactional(readOnly = true)
    public VenueResponse.VenueListResponse getVenuesByAdmin(Long venueAdminId, Pageable pageable) {
        VenueAdminProfile venueAdminProfile = venueAdminProfileRepository.findById(venueAdminId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.VENUE_ADMIN_PROFILE_NOT_FOUND));

        Page<Venue> venuePage = venueRepository.findAllByAdminProfileOrderByCreatedAtDesc(venueAdminProfile, pageable);

        List<VenueResponse.VenueItem> venues = venuePage.getContent().stream()
                .map(this::convertToVenueItem)
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.builder()
                .totalCount(venuePage.getTotalElements())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .hasMore(venuePage.hasNext())
                .build();

        return VenueResponse.VenueListResponse.builder()
                .venues(venues)
                .pageInfo(pageInfo)
                .build();
    }

    @Transactional
    public VenueResponse.CreateVenueAdminProfileResponse createVenueAdminProfile(User user, VenueRequest.CreateVenueAdminProfile request) {
        // 이미 공간 사업자 프로필이 있는지 확인
        if (venueAdminProfileRepository.findByUser(user).isPresent()) {
            throw new GeneralException(ErrorStatus.VENUE_ADMIN_PROFILE_ALREADY_EXISTS);
        }

        VenueAdminProfile venueAdminProfile = VenueAdminProfile.builder()
                .user(user)
                .businessRegNumber(request.getBusinessRegNumber())
                .businessName(request.getBusinessName())
                .phone(request.getPhone())
                .description(request.getDescription())
                .profileImageUrl(request.getProfileImageUrl())
                .build();

        VenueAdminProfile savedProfile = venueAdminProfileRepository.save(venueAdminProfile);

        return VenueResponse.CreateVenueAdminProfileResponse.builder()
                .id(savedProfile.getId())
                .businessRegNumber(savedProfile.getBusinessRegNumber())
                .businessName(savedProfile.getBusinessName())
                .phone(savedProfile.getPhone())
                .description(savedProfile.getDescription())
                .profileImageUrl(savedProfile.getProfileImageUrl())
                .build();
    }

    private VenueResponse.VenueItem convertToVenueItem(Venue venue) {
        return VenueResponse.VenueItem.builder()
                .id(venue.getId())
                .name(venue.getName())
                .region(venue.getRegion())
                .address(venue.getAddress())
                .imageUrl(venue.getImageUrl())
                .seatCapacity(venue.getSeatCapacity())
                .baseRentalFee(venue.getBaseRentalFee())
                .description(venue.getDescription())
                .build();
    }
}