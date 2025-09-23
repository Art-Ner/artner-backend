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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleOAuthService googleOAuthService;
    private final KakaoOAuthService kakaoOAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/google/login")
    public ApiResponse<String> getGoogleLoginUrl() {
        String url = googleOAuthService.getGoogleLoginUrl();
        return ApiResponse.success(url);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<Void> googleCallback(@RequestParam(required = false) String code,
                                               @RequestParam(required = false) String state) {
        try {
            // 0) Guard: if code is missing, bounce to front with error
            if (code == null || code.isBlank()) {
                URI errLocation = URI.create("https://artner.kr/auth/google/callback?status=error&reason=missing_code");
                return ResponseEntity.status(303).location(errLocation).build();
            }

            // 1) Exchange code -> tokens and upsert user
            TokenResponse.LoginResponse loginResponse = googleOAuthService.processGoogleLogin(code);

            // 2) Set HttpOnly cookies (access/refresh)
            ResponseCookie accessCookie = ResponseCookie.from("access_token", loginResponse.getAccessToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .domain(".artner.kr")
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .build();

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", loginResponse.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .domain(".artner.kr")
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();

            // 3) Final redirect to front page route (page, not API)
            URI successLocation = URI.create("https://artner.kr/auth/google/callback?status=success");

            return ResponseEntity.status(303)
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .location(successLocation)
                    .build();

        } catch (Exception e) {
            // Log and redirect to front with error flag (no sensitive info)
            log.error("Google OAuth callback failed", e);
            URI errLocation = URI.create("https://artner.kr/auth/google/callback?status=error");
            return ResponseEntity.status(303).location(errLocation).build();
        }
    }

    // Kakao Login Endpoints
    @PostMapping("/kakao/login")
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