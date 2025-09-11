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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    @Value("${google.oauth.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${google.oauth.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${google.oauth.redirect-uri}")
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

        return restTemplate.postForObject(tokenRequestUrl, request, GoogleTokenResponse.class);
    }


    private GoogleUserInfo requestUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                request,
                GoogleUserInfo.class
        );

        return response.getBody();
    }


    public TokenResponse.TokenDto processGoogleLogin(String code) {
        // 1. code -> accessToken 받기
        GoogleTokenResponse tokenResponse = requestAccessToken(code);

        // 2. accessToken -> 유저 정보 받기
        GoogleUserInfo userInfo = requestUserInfo(tokenResponse.getAccessToken());

        // Use userService to find or create user
        User user = userService.findOrCreateUser(userInfo.getEmail(), userInfo.getName(), OAuthProvider.GOOGLE);

        // 3. Generate application tokens
        return jwtTokenProvider.generateToken(user.getId());
    }


    public TokenResponse.TokenDto reissueAccessToken(TokenReissueRequest request) {
        return jwtTokenProvider.reissue(request.getRefreshToken());
    }

    public void logout(String accessToken) {
        jwtTokenProvider.logout(accessToken);
    }
}
