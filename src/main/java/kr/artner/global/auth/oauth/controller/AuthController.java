package kr.artner.global.auth.oauth.controller;

import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.global.auth.jwt.dto.TokenReissueRequest;
import kr.artner.global.auth.jwt.dto.TokenResponse.TokenDto;
import kr.artner.global.auth.oauth.service.GoogleOAuthService;
import kr.artner.global.auth.oauth.service.KakaoOAuthService; // Added import
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleOAuthService googleOAuthService;
    private final KakaoOAuthService kakaoOAuthService; // Added injection
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/google/login")
    public ApiResponse<String> getGoogleLoginUrl() {
        String url = googleOAuthService.getGoogleLoginUrl();
        return ApiResponse.success(url);
    }

    @GetMapping("/google/callback")
    public ApiResponse<TokenDto> googleCallback(@RequestParam String code) {
        TokenDto tokens = googleOAuthService.processGoogleLogin(code);
        return ApiResponse.success(tokens);
    }

    // Kakao Login Endpoints
    @GetMapping("/kakao/login")
    public ApiResponse<String> getKakaoLoginUrl() {
        String url = kakaoOAuthService.getKakaoLoginUrl();
        return ApiResponse.success(url);
    }

    @GetMapping("/kakao/callback")
    public ApiResponse<TokenDto> kakaoCallback(@RequestParam String code) {
        TokenDto tokens = kakaoOAuthService.processKakaoLogin(code);
        return ApiResponse.success(tokens);
    }

    @PostMapping("/token/reissue")
    public ApiResponse<TokenDto> reissueToken(@RequestBody TokenReissueRequest request) {
        TokenDto tokens = jwtTokenProvider.reissue(request.getRefreshToken());
        return ApiResponse.success(tokens);
    }
}

    