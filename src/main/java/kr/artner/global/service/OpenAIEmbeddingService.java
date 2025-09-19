package kr.artner.global.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIEmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key:}")
    private String openaiApiKey;

    private static final String OPENAI_EMBEDDING_URL = "https://api.openai.com/v1/embeddings";
    private static final String MODEL = "text-embedding-3-small";

    public String generateEmbedding(String text) {
        try {
            // API 할당량 초과로 인해 임시로 더미 벡터 반환 (개발용, 실제 운영시 주석 처리)
            // log.info("Generating mock embedding for text: {}", text.substring(0, Math.min(50, text.length())));
            // return generateMockEmbedding(text);

            if (openaiApiKey == null || openaiApiKey.isEmpty()) {
                log.warn("OpenAI API key not configured. Returning null embedding.");
                return null;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            EmbeddingRequest request = new EmbeddingRequest();
            request.setModel(MODEL);
            request.setInput(text);

            HttpEntity<EmbeddingRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<EmbeddingResponse> response = restTemplate.exchange(
                    OPENAI_EMBEDDING_URL,
                    HttpMethod.POST,
                    entity,
                    EmbeddingResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                EmbeddingResponse embeddingResponse = response.getBody();
                if (!embeddingResponse.getData().isEmpty()) {
                    List<Double> embedding = embeddingResponse.getData().get(0).getEmbedding();
                    return convertEmbeddingToVectorString(embedding);
                }
            }

            log.error("Failed to generate embedding for text: {}", text);
            return null;

        } catch (Exception e) {
            log.error("Error generating embedding for text: {}", text, e);
            return null;
        }
    }

    private String generateMockEmbedding(String text) {
        // 텍스트의 해시값을 기반으로 1536차원의 더미 벡터 생성
        int hash = text.hashCode();
        java.util.Random random = new java.util.Random(hash);

        StringBuilder vectorBuilder = new StringBuilder("[");
        for (int i = 0; i < 1536; i++) {
            if (i > 0) vectorBuilder.append(",");
            // -1에서 1 사이의 랜덤 값 생성 (일반적인 임베딩 벡터 범위)
            double value = (random.nextDouble() - 0.5) * 2;
            vectorBuilder.append(String.format("%.6f", value));
        }
        vectorBuilder.append("]");

        return vectorBuilder.toString();
    }

    private String convertEmbeddingToVectorString(List<Double> embedding) {
        String vectorString = embedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return "[" + vectorString + "]";
    }

    public String generateProjectEmbedding(String title, String concept, String genre, String region) {
        String combinedText = String.format("제목: %s, 컨셉: %s, 장르: %s, 지역: %s",
                title, concept, genre, region);
        return generateEmbedding(combinedText);
    }

    public String generateArtistEmbedding(String headline, String bio, List<String> genres, List<String> roles) {
        String genreText = genres != null ? String.join(", ", genres) : "";
        String roleText = roles != null ? String.join(", ", roles) : "";
        String combinedText = String.format("헤드라인: %s, 소개: %s, 장르: %s, 역할: %s",
                headline, bio, genreText, roleText);
        return generateEmbedding(combinedText);
    }

    @Data
    static class EmbeddingRequest {
        private String model;
        private String input;
    }

    @Data
    static class EmbeddingResponse {
        private List<EmbeddingData> data;
        private Usage usage;
    }

    @Data
    static class EmbeddingData {
        private List<Double> embedding;
        private int index;
    }

    @Data
    static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }
}