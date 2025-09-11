package kr.artner.domain.user.controller;

import jakarta.validation.Valid;
import kr.artner.domain.user.dto.UserRequest;
import kr.artner.domain.user.dto.UserResponse;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.service.UserService;
import kr.artner.global.auth.LoginMember;
import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.global.auth.jwt.dto.TokenResponse.TokenDto;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Service s3Service;

    @GetMapping("/projects")
    public ApiResponse<?> getMyProjects(
            @LoginMember User user,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "status", required = false) String status
    ) {
        // TODO: 실제 서비스 로직 구현 필요
        return ApiResponse.success(null);
    }

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
    public ApiResponse<?> createArtistProfile(@LoginMember User user) {
        // TODO: 실제 서비스 로직 구현 필요
        return ApiResponse.success(null);
    }

    @PostMapping("/venue-admin-profile")
    public ApiResponse<?> createVenueAdminProfile(@LoginMember User user) {
        // TODO: 실제 서비스 로직 구현 필요
        return ApiResponse.success(null);
    }

    @GetMapping("/me/rented-venues")
    public ApiResponse<?> getRentedVenues(@LoginMember User user) {
        // TODO: 내가 대관한 공간 목록 조회
        return ApiResponse.success(null);
    }
}
