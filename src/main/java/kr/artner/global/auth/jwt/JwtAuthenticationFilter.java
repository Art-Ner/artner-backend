package kr.artner.global.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.artner.global.auth.CustomUserDetailsService;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomUserDetailsService userDetailsService;

    // 인증 예외 경로
    private static final List<String> EXCLUDE_URL_PATTERN_LIST = List.of(
            "/login",
            "/css",
            "/js",
            "/ws",
            "/token",
            "/auth",
            "/api/auth",
            "/api/users/join",
            "/api/user-reviews",
            // "/api/artists",
            "/health",
            "/swagger-ui",
            "/v3/api-docs"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if (!path.startsWith("/api/")) {
            return true;
        }
        return EXCLUDE_URL_PATTERN_LIST.stream()
                .anyMatch(path::startsWith);
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더에서 JWT 토큰 추출
        String token = jwtTokenProvider.getTokenFromRequest(request);

        // 2. 토큰이 없는 경우 -> 401 반환
        if (token == null) {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        // 3. 블랙리스트에 등록된 토큰인 경우
        if (isTokenBlacklisted(token)) {
            throw new GeneralException(ErrorStatus.INVALID_ACCESSTOKEN);
        }

        // 4. 토큰 검증 및 인증 정보 설정
        try {
            // JWT 토큰에서 사용자 ID 추출
            Long userId = jwtTokenProvider.getUserIdFromToken(token);

            if (userId != null) {
                // UserDetailsService를 통해 사용자 정보 로드
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

                // Spring Security에 인증 정보 설정
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("userId가 null 입니다.");
            }
        } catch (Exception e) {
            log.warn("JWT 토큰 검증 실패: " + e.getMessage());
            // 토큰이 있는데 잘못된 경우 401 반환
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist:" + token);
    }
}