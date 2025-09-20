package kr.artner.global.config;

import kr.artner.global.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(CorsProperties.class)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsProperties corsProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/users/join",
                                "/health",
                                "/ws/**"
                        ).permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll()
                        .requestMatchers(
                                "/",
                                "/actuator/health",
                                "/api/ping"
                        ).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

        // WebSocket 엔드포인트용 CORS 설정 (credentials 허용)
        var wsConfiguration = new org.springframework.web.cors.CorsConfiguration();
        wsConfiguration.addAllowedOriginPattern("*");
        wsConfiguration.addAllowedMethod("*");
        wsConfiguration.addAllowedHeader("*");
        wsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/ws/**", wsConfiguration);

        // 일반 API용 CORS 설정
        var apiConfiguration = new org.springframework.web.cors.CorsConfiguration();
        for (String origin : corsProperties.getAllowedOrigins()) {
            if ("null".equals(origin)) {
                apiConfiguration.addAllowedOriginPattern("*");
            } else {
                apiConfiguration.addAllowedOrigin(origin);
            }
        }
        apiConfiguration.addAllowedMethod("*");
        apiConfiguration.addAllowedHeader("*");

        boolean hasNullOrigin = corsProperties.getAllowedOrigins().contains("null");
        apiConfiguration.setAllowCredentials(!hasNullOrigin);
        source.registerCorsConfiguration("/**", apiConfiguration);

        return source;
    }

    // @Bean
    // public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
    //     var configuration = new org.springframework.web.cors.CorsConfiguration();
        
    //     // 환경변수에서 허용된 오리진 읽어와서 추가
    //     String[] origins = allowedOrigins.split(",");
    //     for (String origin : origins) {
    //         configuration.addAllowedOrigin(origin.trim());
    //     }
        
    //     // 기본 프로덕션 도메인 추가
    //     configuration.addAllowedOrigin("https://artner.kr");
    //     configuration.addAllowedOriginPattern("https://*.artner.kr");
        
    //     configuration.addAllowedMethod("*");
    //     configuration.addAllowedHeader("*");
    //     configuration.setAllowCredentials(true);

    //     var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }
}
