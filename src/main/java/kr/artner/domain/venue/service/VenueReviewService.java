package kr.artner.domain.venue.service;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.venue.dto.VenueReviewRequest;
import kr.artner.domain.venue.dto.VenueReviewResponse;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.entity.VenueReview;
import kr.artner.domain.venue.repository.VenueRepository;
import kr.artner.domain.venue.repository.VenueReviewRepository;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VenueReviewService {

    private final VenueReviewRepository venueReviewRepository;
    private final VenueRepository venueRepository;

    @Transactional
    public VenueReviewResponse.CreateVenueReviewResponse createVenueReview(User user, Long venueId, VenueReviewRequest.CreateVenueReview request) {
        // 평점 유효성 검사
        validateRating(request.getRate());

        // 대상 공간 조회
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.VENUE_NOT_FOUND));

        // 이미 리뷰를 작성했는지 확인
        Optional<VenueReview> existingReview = venueReviewRepository.findByUserAndVenue(user, venue);
        if (existingReview.isPresent()) {
            throw new GeneralException(ErrorStatus.VENUE_REVIEW_ALREADY_EXISTS);
        }

        // 리뷰 생성
        VenueReview venueReview = VenueReview.builder()
                .user(user)
                .venue(venue)
                .rate(request.getRate())
                .content(request.getContent())
                .build();

        VenueReview savedReview = venueReviewRepository.save(venueReview);

        return VenueReviewResponse.CreateVenueReviewResponse.builder()
                .id(savedReview.getId())
                .rate(savedReview.getRate())
                .content(savedReview.getContent())
                .createdAt(savedReview.getCreatedAt())
                .message("공간 리뷰가 성공적으로 등록되었습니다.")
                .build();
    }

    @Transactional
    public VenueReviewResponse.UpdateVenueReviewResponse updateVenueReview(User user, Long venueId, VenueReviewRequest.UpdateVenueReview request) {
        // 평점 유효성 검사
        validateRating(request.getRate());

        // 대상 공간 조회
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.VENUE_NOT_FOUND));

        // 기존 리뷰 조회
        VenueReview venueReview = venueReviewRepository.findByUserAndVenue(user, venue)
                .orElseThrow(() -> new GeneralException(ErrorStatus.VENUE_REVIEW_NOT_FOUND));

        // 리뷰 수정
        venueReview.updateReview(request.getRate(), request.getContent());

        return VenueReviewResponse.UpdateVenueReviewResponse.builder()
                .id(venueReview.getId())
                .rate(venueReview.getRate())
                .content(venueReview.getContent())
                .updatedAt(venueReview.getUpdatedAt())
                .message("공간 리뷰가 성공적으로 수정되었습니다.")
                .build();
    }

    @Transactional
    public void deleteVenueReview(User user, Long venueId) {
        // 대상 공간 조회
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.VENUE_NOT_FOUND));

        // 기존 리뷰 조회
        VenueReview venueReview = venueReviewRepository.findByUserAndVenue(user, venue)
                .orElseThrow(() -> new GeneralException(ErrorStatus.VENUE_REVIEW_NOT_FOUND));

        // 리뷰 삭제
        venueReviewRepository.delete(venueReview);
    }

    private void validateRating(BigDecimal rate) {
        if (rate == null) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST);
        }

        BigDecimal minRate = new BigDecimal("0.5");
        BigDecimal maxRate = new BigDecimal("5.0");

        if (rate.compareTo(minRate) < 0 || rate.compareTo(maxRate) > 0) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST);
        }

        // 소수점 한 자리까지만 허용
        if (rate.scale() > 1) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST);
        }
    }
}