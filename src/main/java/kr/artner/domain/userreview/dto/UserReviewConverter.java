package kr.artner.domain.userreview.dto;

import kr.artner.domain.user.dto.UserConverter;
import kr.artner.domain.userreview.entity.UserReview;

public class UserReviewConverter {

    public static UserReviewResponse.GetUserReviewResponse toGetUserReviewResponse(UserReview userReview) {
        return UserReviewResponse.GetUserReviewResponse.builder()
                .id(userReview.getId())
                .user(UserConverter.toGetUserInfoResponse(userReview.getUser()))
                .targetUser(UserConverter.toGetUserInfoResponse(userReview.getTargetUser()))
                .content(userReview.getContent())
                .createdAt(userReview.getCreatedAt())
                .build();
    }
}
