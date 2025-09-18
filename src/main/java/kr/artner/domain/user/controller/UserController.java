package kr.artner.domain.user.controller;

import jakarta.validation.Valid;
import kr.artner.domain.user.dto.UserRequest;
import kr.artner.domain.user.dto.UserResponse;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.service.UserService;
import kr.artner.domain.userreview.dto.UserReviewResponse;
import kr.artner.domain.userreview.service.UserReviewService;
import kr.artner.global.auth.LoginMember;
import kr.artner.global.service.S3Service;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final S3Service s3Service;
    private final UserReviewService userReviewService;

    @GetMapping("/me")
    public ApiResponse<UserResponse.DetailInfoDTO> getMyInfo(@LoginMember User user) {
        UserResponse.DetailInfoDTO response = userService.getMyInfo(user.getId());
        return ApiResponse.success(response);
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse.UpdateResponse> updateProfile(
            @LoginMember User user,
            @RequestBody @Valid UserRequest.UpdateProfile request
    ) {
        UserResponse.UpdateResponse response = userService.updateProfile(user.getId(), request);
        return ApiResponse.success(response);
    }

    @PostMapping("/me/profile-image")
    public ApiResponse<UserResponse.DetailInfoDTO> updateProfileImage(
            @LoginMember User user,
            @RequestParam("file") MultipartFile file
    ) {
        String imageUrl = s3Service.uploadProfileImage(file);
        
        // 프로필 이미지 URL 업데이트
        UserResponse.DetailInfoDTO response = userService.updateProfileImage(user.getId(), imageUrl);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/me")
    public ApiResponse<Void> deleteUser(@LoginMember User user) {
        userService.deleteUser(user.getId());
        return ApiResponse.success(null);
    }

    @PostMapping("/artist-profile")
    public ApiResponse<kr.artner.domain.artist.dto.ArtistResponse.CreateArtistProfileResponse> createArtistProfile(
            @LoginMember User user,
            @RequestBody @Valid kr.artner.domain.artist.dto.ArtistRequest.CreateArtistProfile request
    ) {
        kr.artner.domain.artist.dto.ArtistResponse.CreateArtistProfileResponse response =
                userService.createArtistProfile(user.getId(), request);
        return ApiResponse.success(response);
    }

    @PostMapping("/venue-admin-profile")
    public ApiResponse<kr.artner.domain.venue.dto.VenueResponse.CreateVenueAdminProfileResponse> createVenueAdminProfile(
            @LoginMember User user,
            @RequestBody @Valid kr.artner.domain.venue.dto.VenueRequest.CreateVenueAdminProfile request
    ) {
        kr.artner.domain.venue.dto.VenueResponse.CreateVenueAdminProfileResponse response =
                userService.createVenueAdminProfile(user.getId(), request);
        return ApiResponse.success(response);
    }

    @GetMapping("/me/rented-venues")
    public ApiResponse<?> getRentedVenues(@LoginMember User user) {
        // TODO: 내가 대관한 공간 목록 조회
        return ApiResponse.success(null);
    }

    @GetMapping("/{userId}/reviews")
    public ApiResponse<UserReviewResponse.GetUserReviewsResponse> getUserReviews(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        UserReviewResponse.GetUserReviewsResponse response = userReviewService.getUserReviews(userId, page, size);
        return ApiResponse.success(response);
    }
}
