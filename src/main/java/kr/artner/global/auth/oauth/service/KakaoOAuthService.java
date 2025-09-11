package kr.artner.global.auth.oauth.service;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.service.UserService;
import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.global.auth.jwt.dto.TokenResponse.TokenDto;
import kr.artner.global.auth.oauth.enums.OAuthProvider; // Import OAuthProvider
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${oauth.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${oauth.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate; // Assuming RestTemplate is configured

    public String getKakaoLoginUrl() {
        // Construct the Kakao authorization URL
        return "https://kauth.kakao.com/oauth/authorize?" +
               "client_id=" + kakaoClientId +
               "&redirect_uri=" + kakaoRedirectUri +
               "&response_type=code";
    }

    @Transactional
    public TokenDto processKakaoLogin(String code) {
        // 1. Exchange authorization code for access token
        // This part would involve making an HTTP POST request to kakaoTokenUri
        // with client_id, redirect_uri, code, and grant_type=authorization_code
        // For now, just a placeholder.

        // 2. Use access token to fetch user info from kakaoUserInfoUri
        // This part would involve making an HTTP GET request to kakaoUserInfoUri
        // with Authorization: Bearer <access_token>
        // For now, just a placeholder.

        // Extract email and other relevant info from Kakao user info
        String email = "kakao_user@example.com"; // Placeholder
        String username = "Kakao User"; // Placeholder

        // 3. Create or retrieve user in your system
        User user = userService.findOrCreateUser(email, username, OAuthProvider.KAKAO); // Use OAuthProvider.KAKAO

        // 4. Generate application tokens
        return jwtTokenProvider.generateToken(user.getId());
    }
}
