package kr.artner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import kr.artner.global.config.DotenvApplicationInitializer;

@EnableJpaAuditing
@EnableAsync
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class ArtnerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ArtnerApplication.class)
            .initializers(new DotenvApplicationInitializer())
            .run(args);
    }

}
