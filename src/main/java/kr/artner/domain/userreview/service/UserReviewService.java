package kr.artner.domain.userreview.service;

import kr.artner.domain.user.dto.UserConverter;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.domain.userreview.dto.UserReviewRequest;
import kr.artner.domain.userreview.dto.UserReviewResponse;
import kr.artner.domain.userreview.entity.UserReview;
import kr.artner.domain.userreview.repository.UserReviewRepository;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import kr.artner.response.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserReviewResponse.CreateUserReviewResponse createUserReview(User reviewer, Long targetUserId, UserReviewRequest.CreateUserReview request) {
        // 대상 사용자 조회
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 자기 자신에게 리뷰 작성 방지
        if (reviewer.getId().equals(targetUserId)) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST);
        }

        // 이미 리뷰를 작성했는지 확인
        Optional<UserReview> existingReview = userReviewRepository.findByUserAndTargetUser(reviewer, targetUser);
        if (existingReview.isPresent()) {
            throw new GeneralException(ErrorStatus.USER_REVIEW_ALREADY_EXISTS);
        }

        // 리뷰 생성
        UserReview userReview = UserReview.builder()
                .user(reviewer)
                .targetUser(targetUser)
                .content(request.getContent())
                .build();

        UserReview savedReview = userReviewRepository.save(userReview);

        return UserReviewResponse.CreateUserReviewResponse.builder()
                .id(savedReview.getId())
                .content(savedReview.getContent())
                .createdAt(savedReview.getCreatedAt())
                .message("리뷰가 성공적으로 등록되었습니다.")
                .build();
    }

    @Transactional
    public UserReviewResponse.UpdateUserReviewResponse updateUserReview(User reviewer, Long targetUserId, UserReviewRequest.UpdateUserReview request) {
        // 대상 사용자 조회
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 기존 리뷰 조회
        UserReview userReview = userReviewRepository.findByUserAndTargetUser(reviewer, targetUser)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_REVIEW_NOT_FOUND));

        // 리뷰 수정
        userReview.updateContent(request.getContent());

        return UserReviewResponse.UpdateUserReviewResponse.builder()
                .id(userReview.getId())
                .content(userReview.getContent())
                .updatedAt(userReview.getUpdatedAt())
                .message("리뷰가 성공적으로 수정되었습니다.")
                .build();
    }

    @Transactional
    public void deleteUserReview(User reviewer, Long targetUserId) {
        // 대상 사용자 조회
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 기존 리뷰 조회
        UserReview userReview = userReviewRepository.findByUserAndTargetUser(reviewer, targetUser)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_REVIEW_NOT_FOUND));

        // 리뷰 삭제
        userReviewRepository.delete(userReview);
    }

    @Transactional(readOnly = true)
    public UserReviewResponse.GetUserReviewsResponse getUserReviews(Long targetUserId, Integer limit, Integer offset) {
        // 대상 사용자 조회
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        Page<UserReview> reviewPage = userReviewRepository.findByTargetUserOrderByCreatedAtDesc(targetUser, pageable);

        List<UserReviewResponse.UserReviewItem> reviewItems = reviewPage.getContent().stream()
                .map(review -> UserReviewResponse.UserReviewItem.builder()
                        .id(review.getId())
                        .reviewer(UserConverter.toGetUserInfoResponse(review.getUser()))
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .build())
                .toList();

        PageInfo pageInfo = PageInfo.builder()
                .totalCount(reviewPage.getTotalElements())
                .limit(limit)
                .offset(offset)
                .hasMore(reviewPage.hasNext())
                .build();

        return UserReviewResponse.GetUserReviewsResponse.builder()
                .reviews(reviewItems)
                .pageInfo(pageInfo)
                .build();
    }

}
