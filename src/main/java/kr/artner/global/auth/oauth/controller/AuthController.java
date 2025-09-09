package kr.artner.global.auth.oauth.controller;

import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.global.auth.jwt.dto.TokenReissueRequest;
import kr.artner.global.auth.jwt.dto.TokenResponse.TokenDto;
import kr.artner.global.auth.oauth.service.GoogleOAuthService;
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

    @PostMapping("/token/reissue")
    public ApiResponse<TokenDto> reissueToken(@RequestBody TokenReissueRequest request) {
        TokenDto tokens = jwtTokenProvider.reissue(request.getRefreshToken());
        return ApiResponse.success(tokens);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.getTokenFromRequest(request);
        if (accessToken != null) {
            jwtTokenProvider.logout(accessToken);
        }
        return ApiResponse.success(null);
    }
}