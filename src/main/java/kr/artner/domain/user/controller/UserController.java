package kr.artner.domain.user.controller;

import jakarta.validation.Valid;
import kr.artner.domain.user.User;
import kr.artner.domain.user.dto.UserRequest;
import kr.artner.domain.user.dto.UserResponse;
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

    @PostMapping("/join")
    public ApiResponse<TokenDto> join(@RequestBody @Valid UserRequest.JoinDTO request) {
        UserResponse.JoinResponse joinResponse = userService.join(request);
        TokenDto tokens = jwtTokenProvider.generateToken(joinResponse.getId());
        
        log.info("회원가입 완료: email={}, username={}, oauthProvider={}", 
                request.getEmail(), request.getUsername(), request.getOauthProvider());
        return ApiResponse.success(tokens);
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
        
        // 기존 프로필 업데이트 (이미지 URL만 변경)
        UserRequest.UpdateProfile updateRequest = new UserRequest.UpdateProfile(
                user.getUsername(), 
                user.getPhone()
        );
        userService.updateProfile(user.getId(), updateRequest);
        
        UserResponse.DetailInfoDTO response = userService.getMyInfo(user.getId());
        return ApiResponse.success(response);
    }

    @DeleteMapping("/me")
    public ApiResponse<Void> deleteUser(@LoginMember User user) {
        userService.deleteUser(user.getId());
        return ApiResponse.success(null);
    }
}
