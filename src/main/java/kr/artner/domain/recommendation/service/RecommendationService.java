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
    private final VectorBasedRecommendationService vectorBasedRecommendationService;

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

    public RecommendationResponse.GetRecommendationsResponse getRecommendations(User user, Long artistProfileId) {
        ArtistProfile targetArtistProfile = null;

        if (artistProfileId != null) {
            // 특정 아티스트 프로필이 지정된 경우
            Optional<ArtistProfile> artistProfileOpt = artistProfileRepository.findById(artistProfileId);
            if (artistProfileOpt.isPresent() && artistProfileOpt.get().getUser().getId().equals(user.getId())) {
                targetArtistProfile = artistProfileOpt.get();
            } else {
                log.warn("Artist profile {} not found or not owned by user {}", artistProfileId, user.getId());
            }
        }

        if (targetArtistProfile == null) {
            // 아티스트 프로필이 지정되지 않았거나 잘못된 경우, 사용자의 첫 번째 프로필 사용
            Optional<ArtistProfile> artistProfileOpt = artistProfileRepository.findByUser(user);
            if (artistProfileOpt.isEmpty()) {
                log.info("No artist profile found for user {}. Cannot generate personalized recommendations.", user.getId());
                return getPopularProjectsRecommendations();
            }
            targetArtistProfile = artistProfileOpt.get();
        }

        String redisKey = RECOMMENDATION_KEY_PREFIX + "artist:" + targetArtistProfile.getId();
        String recommendationsJson = redisTemplate.opsForValue().get(redisKey);

        if (recommendationsJson == null) {
            log.info("No cached recommendations found for artist profile {}. Generating new recommendations...", targetArtistProfile.getId());

            List<RecommendationResponse.RecommendedProject> recommendations = generateRecommendationsForArtist(targetArtistProfile);
            storeRecommendationsInRedis(targetArtistProfile.getId(), recommendations);

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

            List<RecommendationResponse.RecommendedProject> recommendations = generateRecommendationsForArtist(targetArtistProfile);
            storeRecommendationsInRedis(targetArtistProfile.getId(), recommendations);
            return buildGetRecommendationsResponse(recommendations);
        }
    }

    private RecommendationResponse.GetRecommendationsResponse getPopularProjectsRecommendations() {
        List<Project> popularProjects = projectRepository.findByStatusOrderByCreatedAtDesc(ProjectStatus.RECRUITING);
        List<RecommendationResponse.RecommendedProject> recommendations = popularProjects.stream()
                .limit(MAX_RECOMMENDATIONS_PER_USER)
                .map(project -> mapToRecommendedProject(project, 50.0))
                .toList();
        return buildGetRecommendationsResponse(recommendations);
    }

    private List<RecommendationResponse.RecommendedProject> generateRecommendationsForArtist(ArtistProfile artistProfile) {
        try {
            // 1. 벡터 기반 추천을 우선 시도 (User 대신 ArtistProfile 사용)
            if (vectorBasedRecommendationService.isVectorBasedRecommendationAvailable(artistProfile.getUser())) {
                log.info("Generating vector-based recommendations for artist profile ID: {}", artistProfile.getId());
                List<VectorBasedRecommendationService.ProjectWithScore> vectorRecommendations =
                    vectorBasedRecommendationService.getVectorBasedRecommendationsWithScores(artistProfile.getUser(), MAX_RECOMMENDATIONS_PER_USER);

                if (!vectorRecommendations.isEmpty()) {
                    return vectorRecommendations.stream()
                            .map(projectWithScore -> mapToRecommendedProject(
                                projectWithScore.getProject(),
                                projectWithScore.getScore())) // 실제 벡터 유사도 점수 사용
                            .toList();
                }
            }

            log.info("Falling back to rule-based recommendations for artist profile ID: {}", artistProfile.getId());
            return generateRuleBasedRecommendationsForArtist(artistProfile);

        } catch (Exception e) {
            log.error("Error generating recommendations for artist profile ID: {}, falling back to rule-based", artistProfile.getId(), e);
            return generateRuleBasedRecommendationsForArtist(artistProfile);
        }
    }

    private List<RecommendationResponse.RecommendedProject> generateRecommendationsForUser(User user) {
        // 기존 메서드를 아티스트 프로필 기반으로 리다이렉트
        Optional<ArtistProfile> artistProfileOpt = artistProfileRepository.findByUser(user);
        if (artistProfileOpt.isEmpty()) {
            return getPopularProjectsRecommendations().getProjects();
        }
        return generateRecommendationsForArtist(artistProfileOpt.get());
    }

    private List<RecommendationResponse.RecommendedProject> generateRuleBasedRecommendationsForArtist(ArtistProfile artistProfile) {
        Set<GenreCode> userGenres = getUserGenres(artistProfile);
        List<Project> availableProjects = projectRepository.findByStatusOrderByCreatedAtDesc(ProjectStatus.RECRUITING);

        // 장르 매칭 기반으로 추천 점수 계산하여 정렬
        return availableProjects.stream()
                .map(project -> calculateRecommendationScore(project, userGenres))
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore())) // 점수 높은 순 정렬
                .limit(MAX_RECOMMENDATIONS_PER_USER)
                .toList();
    }

    private List<RecommendationResponse.RecommendedProject> generateRuleBasedRecommendations(User user) {
        // 기존 규칙 기반 추천 로직
        Optional<ArtistProfile> artistProfileOpt = artistProfileRepository.findByUser(user);

        if (artistProfileOpt.isEmpty()) {
            // 아티스트 프로필이 없는 경우 인기순으로 반환
            List<Project> popularProjects = projectRepository.findByStatusOrderByCreatedAtDesc(ProjectStatus.RECRUITING);
            return popularProjects.stream()
                    .limit(MAX_RECOMMENDATIONS_PER_USER)
                    .map(project -> mapToRecommendedProject(project, 50.0)) // 기본 점수
                    .toList();
        }

        ArtistProfile artistProfile = artistProfileOpt.get();
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
