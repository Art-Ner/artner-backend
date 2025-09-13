package kr.artner.global.auth.controller;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.global.auth.jwt.dto.TokenResponse.TokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LogoutIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("로그아웃을 하면 액세스 토큰이 블랙리스트에 등록되고 리프레시 토큰이 삭제된다.")
    void logout() throws Exception {
        // given
        User user = userRepository.save(User.builder().email("test@test.com").username("test").build());
        TokenDto tokenDto = jwtTokenProvider.generateToken(user.getId());
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        // when
        mockMvc.perform(post("/api/logout")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        // then
        String blacklisted = redisTemplate.opsForValue().get("blacklist:" + accessToken);
        assertThat(blacklisted).isEqualTo("logout");

        String storedRefreshToken = redisTemplate.opsForValue().get("refresh:" + user.getId());
        assertThat(storedRefreshToken).isNull();
    }
}
