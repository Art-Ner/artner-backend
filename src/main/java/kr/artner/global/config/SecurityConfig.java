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
                .cors(AbstractHttpConfigurer::disable)
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

    // CORS는 Nginx 리버스 프록시에서 처리함
    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    //     var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

    //     // 통합 CORS 설정 (모든 엔드포인트용)
    //     var configuration = new org.springframework.web.cors.CorsConfiguration();

    //     // 허용된 origin들 추가
    //     for (String origin : corsProperties.getAllowedOrigins()) {
    //         configuration.addAllowedOrigin(origin);
    //     }

    //     configuration.addAllowedMethod("*");
    //     configuration.addAllowedHeader("*");
    //     configuration.setAllowCredentials(true);

    //     source.registerCorsConfiguration("/**", configuration);

    //     return source;
    // }
}
