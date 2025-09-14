package kr.artner.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "cors") // application.yml에서 "cors"로 시작하는 속성을 매핑
public class CorsProperties {

    /**
     * 허용할 Origin 목록입니다.
     * 이 값은 application.yml 또는 환경 변수(CORS_ALLOWED_ORIGINS)로 오버라이드할 수 있습니다.
     */
    private List<String> allowedOrigins = List.of(
            "http://localhost:3000",
            "http://localhost:8080",
            "https://artner.kr"
    ); // <-- 여기에 기본값을 지정!
}