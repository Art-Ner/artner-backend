package kr.artner.domain.userreview.controller;

import jakarta.validation.Valid;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.userreview.dto.UserReviewRequest;
import kr.artner.domain.userreview.dto.UserReviewResponse;
import kr.artner.domain.userreview.service.UserReviewService;
import kr.artner.global.auth.LoginMember;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-reviews")
@RequiredArgsConstructor
public class UserReviewController {

    private final UserReviewService userReviewService;

    @PostMapping("/{userId}")
    public ApiResponse<UserReviewResponse.CreateUserReviewResponse> createUserReview(
            @LoginMember User user,
            @PathVariable Long userId,
            @Valid @RequestBody UserReviewRequest.CreateUserReview request
    ) {
        UserReviewResponse.CreateUserReviewResponse response = userReviewService.createUserReview(user, userId, request);
        return ApiResponse.success(response);
    }

    @PatchMapping("/{userId}")
    public ApiResponse<UserReviewResponse.UpdateUserReviewResponse> updateUserReview(
            @LoginMember User user,
            @PathVariable Long userId,
            @Valid @RequestBody UserReviewRequest.UpdateUserReview request
    ) {
        UserReviewResponse.UpdateUserReviewResponse response = userReviewService.updateUserReview(user, userId, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUserReview(
            @LoginMember User user,
            @PathVariable Long userId
    ) {
        userReviewService.deleteUserReview(user, userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserReviewResponse.GetUserReviewsResponse> getUserReviews(
            @PathVariable Long userId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        UserReviewResponse.GetUserReviewsResponse response = userReviewService.getUserReviews(userId, limit, offset);
        return ApiResponse.success(response);
    }
}
