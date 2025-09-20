package kr.artner.global.auth.oauth.controller;

import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.global.auth.jwt.dto.TokenReissueRequest;
import kr.artner.global.auth.jwt.dto.TokenResponse;
import kr.artner.global.auth.oauth.service.GoogleOAuthService;
import kr.artner.global.auth.oauth.service.KakaoOAuthService; // Added import
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleOAuthService googleOAuthService;
    private final KakaoOAuthService kakaoOAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/google/login")
    public ApiResponse<String> getGoogleLoginUrl() {
        String url = googleOAuthService.getGoogleLoginUrl();
        return ApiResponse.success(url);
    }

    @GetMapping("/google/callback")
    public ApiResponse<String> googleCallback(@RequestParam String code) {
        try {
            log.info("Google OAuth callback received with code: {}", code);
            TokenResponse.LoginResponse loginResponse = googleOAuthService.processGoogleLogin(code);
            log.info("Google OAuth login successful");
            return ApiResponse.success("Login successful");
        } catch (Exception e) {
            log.error("Google OAuth callback failed", e);
            // 임시로 오류 정보를 응답에 포함
            String errorMessage = "Error: " + e.getClass().getSimpleName() + " - " + e.getMessage();
            if (e.getCause() != null) {
                errorMessage += " | Cause: " + e.getCause().getMessage();
            }
            return ApiResponse.failure(errorMessage);
        }
    }

    // Kakao Login Endpoints
    @GetMapping("/kakao/login")
    public ApiResponse<String> getKakaoLoginUrl() {
        String url = kakaoOAuthService.getKakaoLoginUrl();
        return ApiResponse.success(url);
    }

    @GetMapping("/kakao/callback")
    public ApiResponse<TokenResponse.LoginResponse> kakaoCallback(@RequestParam String code) {
        TokenResponse.LoginResponse loginResponse = kakaoOAuthService.processKakaoLogin(code);
        return ApiResponse.success(loginResponse);
    }

    @PostMapping("/token/reissue")
    public ApiResponse<TokenResponse.TokenDto> reissueToken(@RequestBody TokenReissueRequest request) {
        TokenResponse.TokenDto tokens = jwtTokenProvider.reissue(request.getRefreshToken());
        return ApiResponse.success(tokens);
    }
}

    