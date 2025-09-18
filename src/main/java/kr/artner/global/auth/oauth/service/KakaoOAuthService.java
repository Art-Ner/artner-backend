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
               "&response_type=code" +
               "&scope=profile_nickname,account_email";
    }

    @Transactional
    public TokenDto processKakaoLogin(String code) {
        KakaoTokenResponse tokenResponse = requestAccessToken(code);
        KakaoUserInfo userInfo = requestUserInfo(tokenResponse.getAccessToken());

        // 실제 이메일 사용, 없으면 임시 이메일
        String email = userInfo.getKakaoAccount() != null && userInfo.getKakaoAccount().getEmail() != null
                ? userInfo.getKakaoAccount().getEmail()
                : "kakao_" + userInfo.getId() + "@temp.com";

        // 실제 이름 사용, 없으면 기본값
        String username = getUsernameFromKakaoInfo(userInfo);

        // 카카오톡 닉네임 사용, 없으면 username과 동일하게
        String nickname = getNicknameFromKakaoInfo(userInfo, username);

        User user = userService.findOrCreateUser(email, username, nickname, OAuthProvider.KAKAO);

        return jwtTokenProvider.generateToken(user.getId());
    }

    private String getUsernameFromKakaoInfo(KakaoUserInfo userInfo) {
        if (userInfo.getKakaoAccount() != null &&
            userInfo.getKakaoAccount().getProfile() != null &&
            userInfo.getKakaoAccount().getProfile().getNickname() != null) {
            // 닉네임을 username으로 사용
            return userInfo.getKakaoAccount().getProfile().getNickname();
        }
        // 기본값 (닉네임이 없으면)
        return "Kakao User " + userInfo.getId();
    }

    private String getNicknameFromKakaoInfo(KakaoUserInfo userInfo, String fallback) {
        if (userInfo.getKakaoAccount() != null &&
            userInfo.getKakaoAccount().getProfile() != null &&
            userInfo.getKakaoAccount().getProfile().getNickname() != null) {
            return userInfo.getKakaoAccount().getProfile().getNickname();
        }
        // 닉네임이 없으면 username과 동일하게
        return fallback;
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
