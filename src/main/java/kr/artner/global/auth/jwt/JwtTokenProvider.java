package kr.artner.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import jakarta.servlet.http.HttpServletRequest;
import kr.artner.global.auth.jwt.dto.TokenResponse.TokenDto;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidTime;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidTime;

    private final RedisTemplate<String, String> redisTemplate;

    public TokenDto generateToken(Long userId) {
        String accessToken = createToken(userId, accessTokenValidTime);
        String refreshToken = createToken(userId, refreshTokenValidTime);

        // Redis에 refreshToken 저장 (key: refresh:{userId})
        redisTemplate.opsForValue().set(
                "refresh:" + userId,
                refreshToken,
                refreshTokenValidTime,
                TimeUnit.MILLISECONDS
        );

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto reissue(String refreshToken) {
        Long userId = getUserIdFromAnyToken(refreshToken);
        String stored = redisTemplate.opsForValue().get("refresh:" + userId);

        if (stored == null || !stored.equals(refreshToken)) {
            throw new GeneralException(ErrorStatus.INVALID_REFRESHTOKEN);
        }

        return generateToken(userId);
    }

    public void logout(String accessToken) {
        Long userId = getUserIdFromToken(accessToken);
        Date expiration = getTokenExpiration(accessToken);

        // 블랙리스트 처리 (accessToken 무효화)
        redisTemplate.opsForValue().set(
                "blacklist:" + accessToken,
                "logout",
                expiration.getTime() - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
        );

        // refreshToken 삭제
        redisTemplate.delete("refresh:" + userId);
    }

    private String createToken(Long userId, long validity) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validity);

        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public Long getUserIdFromAnyToken(String token) {
        try {
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Long.valueOf(claims.getSubject());
        } catch (ExpiredJwtException e) {
            return Long.valueOf(e.getClaims().getSubject());
        }
    }

    public Long getUserIdFromToken(String token) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }

    public Date getTokenExpiration(String token) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}