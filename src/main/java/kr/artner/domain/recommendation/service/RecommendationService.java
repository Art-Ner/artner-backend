package kr.artner.domain.recommendation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.entity.ArtistGenre;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.enums.ProjectStatus;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.recommendation.dto.RecommendationResponse;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String RECOMMENDATION_KEY_PREFIX = "recommendations:user:";
    private static final int RECOMMENDATION_TTL_HOURS = 24;
    private static final int MAX_RECOMMENDATIONS_PER_USER = 10;

    @Transactional
    public RecommendationResponse.BatchGenerateResponse generateBatchRecommendations() {
        log.info("Starting batch recommendation generation...");

        List<User> activeUsers = userRepository.findAll();
        int processedUserCount = 0;
        int totalRecommendationsGenerated = 0;

        for (User user : activeUsers) {
            try {
                List<RecommendationResponse.RecommendedProject> recommendations =
                    generateRecommendationsForUser(user);

                if (!recommendations.isEmpty()) {
                    storeRecommendationsInRedis(user.getId(), recommendations);
                    totalRecommendationsGenerated += recommendations.size();
                }

                processedUserCount++;
            } catch (Exception e) {
                log.error("Failed to generate recommendations for user {}: {}", user.getId(), e.getMessage());
            }
        }

        log.info("Batch recommendation generation completed. Processed {} users, generated {} recommendations",
                processedUserCount, totalRecommendationsGenerated);

        return RecommendationResponse.BatchGenerateResponse.builder()
                .message("배치 추천 생성이 완료되었습니다.")
                .processedUserCount(processedUserCount)
                .totalRecommendationsGenerated(totalRecommendationsGenerated)
                .generatedAt(LocalDateTime.now())
                .build();
    }

    public RecommendationResponse.GetRecommendationsResponse getRecommendations(User user) {
        String redisKey = RECOMMENDATION_KEY_PREFIX + user.getId();
        String recommendationsJson = redisTemplate.opsForValue().get(redisKey);

        if (recommendationsJson == null) {
            log.info("No cached recommendations found for user {}. Generating new recommendations...", user.getId());

            List<RecommendationResponse.RecommendedProject> recommendations = generateRecommendationsForUser(user);
            storeRecommendationsInRedis(user.getId(), recommendations);

            return buildGetRecommendationsResponse(recommendations);
        }

        try {
            RecommendationData cachedData = objectMapper.readValue(recommendationsJson, RecommendationData.class);
            return RecommendationResponse.GetRecommendationsResponse.builder()
                    .projects(cachedData.getProjects())
                    .totalCount(cachedData.getProjects().size())
                    .generatedAt(cachedData.getGeneratedAt())
                    .expiresAt(cachedData.getGeneratedAt().plusHours(RECOMMENDATION_TTL_HOURS))
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Failed to parse cached recommendations for user {}: {}", user.getId(), e.getMessage());

            List<RecommendationResponse.RecommendedProject> recommendations = generateRecommendationsForUser(user);
            storeRecommendationsInRedis(user.getId(), recommendations);
            return buildGetRecommendationsResponse(recommendations);
        }
    }

    private List<RecommendationResponse.RecommendedProject> generateRecommendationsForUser(User user) {
        // 사용자의 아티스트 프로필 조회
        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        Set<GenreCode> userGenres = getUserGenres(artistProfile);
        List<Project> availableProjects = projectRepository.findByStatusOrderByCreatedAtDesc(ProjectStatus.RECRUITING);

        // 장르 매칭 기반으로 추천 점수 계산하여 정렬
        return availableProjects.stream()
                .map(project -> calculateRecommendationScore(project, userGenres))
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore())) // 점수 높은 순 정렬
                .limit(MAX_RECOMMENDATIONS_PER_USER)
                .toList();
    }

    private Set<GenreCode> getUserGenres(ArtistProfile artistProfile) {
        return artistProfile.getGenres().stream()
                .map(ArtistGenre::getGenreCode)
                .collect(Collectors.toSet());
    }

    private RecommendationResponse.RecommendedProject calculateRecommendationScore(Project project, Set<GenreCode> userGenres) {
        double score = 0.0;

        // 장르 매칭 점수 (가장 중요한 요소)
        if (userGenres.contains(project.getTargetGenre())) {
            score += 80.0; // 장르 매칭 시 높은 점수
        }

        // 최신성 점수 (최근 프로젝트일수록 높은 점수)
        long daysSinceCreated = Duration.between(project.getCreatedAt(), LocalDateTime.now()).toDays();
        double recencyScore = Math.max(0, 20.0 - (daysSinceCreated * 0.5)); // 최대 20점
        score += recencyScore;

        // 랜덤 요소 추가 (다양성을 위해)
        score += Math.random() * 5.0;

        return mapToRecommendedProject(project, score);
    }

    private RecommendationResponse.RecommendedProject mapToRecommendedProject(Project project, double score) {
        ArtistProfile owner = project.getOwner();

        return RecommendationResponse.RecommendedProject.builder()
                .id(project.getId())
                .title(project.getTitle())
                .concept(project.getConcept())
                .targetGenre(project.getTargetGenre())
                .targetRegion(project.getTargetRegion())
                .status(project.getStatus())
                .owner(RecommendationResponse.OwnerInfo.builder()
                        .id(owner.getId())
                        .username(owner.getUser().getUsername())
                        .artistName(owner.getArtistName())
                        .build())
                .score(score)
                .createdAt(project.getCreatedAt())
                .build();
    }

    private void storeRecommendationsInRedis(Long userId, List<RecommendationResponse.RecommendedProject> recommendations) {
        try {
            String redisKey = RECOMMENDATION_KEY_PREFIX + userId;
            RecommendationData data = new RecommendationData(recommendations, LocalDateTime.now());
            String jsonData = objectMapper.writeValueAsString(data);

            redisTemplate.opsForValue().set(redisKey, jsonData, Duration.ofHours(RECOMMENDATION_TTL_HOURS));
            log.debug("Stored {} recommendations for user {} in Redis", recommendations.size(), userId);
        } catch (JsonProcessingException e) {
            log.error("Failed to store recommendations in Redis for user {}: {}", userId, e.getMessage());
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private RecommendationResponse.GetRecommendationsResponse buildGetRecommendationsResponse(
            List<RecommendationResponse.RecommendedProject> recommendations) {
        LocalDateTime now = LocalDateTime.now();
        return RecommendationResponse.GetRecommendationsResponse.builder()
                .projects(recommendations)
                .totalCount(recommendations.size())
                .generatedAt(now)
                .expiresAt(now.plusHours(RECOMMENDATION_TTL_HOURS))
                .build();
    }

    private static class RecommendationData {
        private List<RecommendationResponse.RecommendedProject> projects;
        private LocalDateTime generatedAt;

        public RecommendationData() {}

        public RecommendationData(List<RecommendationResponse.RecommendedProject> projects, LocalDateTime generatedAt) {
            this.projects = projects;
            this.generatedAt = generatedAt;
        }

        public List<RecommendationResponse.RecommendedProject> getProjects() {
            return projects;
        }

        public void setProjects(List<RecommendationResponse.RecommendedProject> projects) {
            this.projects = projects;
        }

        public LocalDateTime getGeneratedAt() {
            return generatedAt;
        }

        public void setGeneratedAt(LocalDateTime generatedAt) {
            this.generatedAt = generatedAt;
        }
    }
}
