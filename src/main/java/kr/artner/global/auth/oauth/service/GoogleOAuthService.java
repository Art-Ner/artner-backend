package kr.artner.global.auth.oauth.service;

import java.util.Optional;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository; // Keep for now if other methods use it
import kr.artner.domain.user.service.UserService; // Import UserService
import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.global.auth.jwt.dto.TokenReissueRequest;
import kr.artner.global.auth.jwt.dto.TokenResponse;
import kr.artner.global.auth.oauth.dto.GoogleTokenResponse;
import kr.artner.global.auth.oauth.dto.GoogleUserInfo;
import kr.artner.global.auth.oauth.enums.OAuthProvider; // Import OAuthProvider
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository; // Keep for now
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService; // Inject UserService

    public String getGoogleLoginUrl() {
        System.out.println(REDIRECT_URI);
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", GOOGLE_CLIENT_ID)
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .queryParam("access_type", "offline") // refreshToken 발급
                .build().toUriString();
    }

    private GoogleTokenResponse requestAccessToken(String code) {
        String tokenRequestUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            log.info("Requesting access token from Google with params: client_id={}, redirect_uri={}", GOOGLE_CLIENT_ID, REDIRECT_URI);
            return restTemplate.postForObject(tokenRequestUrl, request, GoogleTokenResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Google token request failed. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new GeneralException(ErrorStatus.GOOGLE_OAUTH_ERROR, e);
        }
    }


    private GoogleUserInfo requestUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    request,
                    GoogleUserInfo.class
            );
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new GeneralException(ErrorStatus.GOOGLE_OAUTH_ERROR, e);
        }
    }


    public TokenResponse.LoginResponse processGoogleLogin(String code) {
        try {
            log.info("Starting Google OAuth login process with code: {}", code);
            log.info("Using REDIRECT_URI: {}", REDIRECT_URI);
            log.info("Using GOOGLE_CLIENT_ID: {}", GOOGLE_CLIENT_ID);

            // 1. code -> accessToken 받기
            GoogleTokenResponse tokenResponse = requestAccessToken(code);
            log.info("Successfully received access token from Google");

            // 2. accessToken -> 유저 정보 받기
            GoogleUserInfo userInfo = requestUserInfo(tokenResponse.getAccessToken());
            log.info("Successfully received user info from Google: {}", userInfo.getEmail());

            // Generate username with fallback if null
            String username = userInfo.getName() != null && !userInfo.getName().trim().isEmpty()
                    ? userInfo.getName()
                    : generateDefaultUsername(userInfo.getEmail());

            // Use userService to find or create user
            User user = userService.findOrCreateUser(userInfo.getEmail(), username, OAuthProvider.GOOGLE);
            log.info("Successfully found/created user: {}", user.getEmail());

            // 3. Generate application tokens
            TokenResponse.TokenDto tokens = jwtTokenProvider.generateToken(user.getId());
            log.info("Successfully generated JWT tokens for user: {}", user.getId());

            // 4. Create login response with user info
            return TokenResponse.LoginResponse.builder()
                    .accessToken(tokens.getAccessToken())
                    .refreshToken(tokens.getRefreshToken())
                    .user(TokenResponse.LoginResponse.UserInfo.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .oauthProvider(user.getOauthProvider().name().toLowerCase())
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("Error in Google OAuth login process", e);
            throw e;
        }
    }


    public TokenResponse.TokenDto reissueAccessToken(TokenReissueRequest request) {
        return jwtTokenProvider.reissue(request.getRefreshToken());
    }

    public void logout(String accessToken) {
        jwtTokenProvider.logout(accessToken);
    }

    private String generateDefaultUsername(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "google_user_" + System.currentTimeMillis();
        }

        // 이메일 앞부분 추출
        String localPart = email.split("@")[0];

        // 특수문자 제거 및 길이 제한
        String cleanUsername = localPart.replaceAll("[^a-zA-Z0-9가-힣]", "")
                                       .substring(0, Math.min(localPart.length(), 20));

        // 빈 문자열인 경우 기본값
        if (cleanUsername.trim().isEmpty()) {
            cleanUsername = "google_user";
        }

        return cleanUsername;
    }
}
