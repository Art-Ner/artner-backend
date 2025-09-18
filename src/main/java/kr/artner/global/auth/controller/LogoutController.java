package kr.artner.global.auth.controller;

import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LogoutController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.getTokenFromRequest(request);
        if (accessToken != null) {
            jwtTokenProvider.logout(accessToken);
        }
        return ApiResponse.success(null);
    }
}
