package kr.artner.global.auth.oauth.service;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.global.auth.oauth.dto.GoogleTokenResponse;
import kr.artner.global.auth.oauth.dto.GoogleUserInfo;
import kr.artner.global.auth.oauth.enums.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class GoogleOAuthIntegrationTest {

    @Autowired
    private GoogleOAuthService googleOAuthService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    @DisplayName("구글 로그인을 처리하고 새로운 사용자를 데이터베이스에 저장한다.")
    void processGoogleLogin_createNewUser() {
        // given
        String code = "test_code";
        String email = "test@example.com";
        String name = "Test User";

        GoogleTokenResponse tokenResponse = new GoogleTokenResponse();
        setField(tokenResponse, "accessToken", "access_token");

        GoogleUserInfo userInfo = new GoogleUserInfo();
        setField(userInfo, "email", email);
        setField(userInfo, "name", name);

        given(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(GoogleTokenResponse.class)))
                .willReturn(tokenResponse);

        given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(GoogleUserInfo.class)))
                .willReturn(ResponseEntity.ok(userInfo));

        // when
        googleOAuthService.processGoogleLogin(code);

        // then
        User user = userRepository.findByOauthProviderAndEmail(OAuthProvider.GOOGLE, email).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getUsername()).isEqualTo(name);
        assertThat(user.getOauthProvider()).isEqualTo(OAuthProvider.GOOGLE);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = ReflectionUtils.findField(target.getClass(), fieldName);
            field.setAccessible(true);
            ReflectionUtils.setField(field, target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
