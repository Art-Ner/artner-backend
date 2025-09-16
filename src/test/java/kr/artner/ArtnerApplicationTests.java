package kr.artner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import kr.artner.global.config.DotenvApplicationInitializer;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = DotenvApplicationInitializer.class)
class ArtnerApplicationTests {

	@MockBean
	private S3Client s3Client;

	@Test
	void contextLoads() {
	}

}