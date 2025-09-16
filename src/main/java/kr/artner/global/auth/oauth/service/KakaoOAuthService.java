package kr.artner.global.auth.oauth.service;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.service.UserService;
import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.global.auth.jwt.dto.TokenResponse.TokenDto;
import kr.artner.global.auth.oauth.dto.KakaoTokenResponse;
import kr.artner.global.auth.oauth.dto.KakaoUserInfo;
import kr.artner.global.auth.oauth.enums.OAuthProvider;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;

    public String getKakaoLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize?" +
               "client_id=" + kakaoClientId +
               "&redirect_uri=" + kakaoRedirectUri +
               "&response_type=code";
    }

    @Transactional
    public TokenDto processKakaoLogin(String code) {
        KakaoTokenResponse tokenResponse = requestAccessToken(code);
        KakaoUserInfo userInfo = requestUserInfo(tokenResponse.getAccessToken());

        String email = "kakao_" + userInfo.getId() + "@temp.com"; // 임시 이메일
        String username = "Kakao User " + userInfo.getId(); // 기본 사용자명

        User user = userService.findOrCreateUser(email, username, OAuthProvider.KAKAO);

        return jwtTokenProvider.generateToken(user.getId());
    }

    private KakaoTokenResponse requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            return restTemplate.postForObject(kakaoTokenUri, request, KakaoTokenResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new GeneralException(ErrorStatus.KAKAO_OAUTH_ERROR, e);
        }
    }

    private KakaoUserInfo requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(kakaoUserInfoUri, HttpMethod.GET, request, KakaoUserInfo.class).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new GeneralException(ErrorStatus.KAKAO_OAUTH_ERROR, e);
        }
    }
}
