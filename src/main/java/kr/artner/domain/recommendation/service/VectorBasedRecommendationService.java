package kr.artner.domain.recommendation.service;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.service.ArtistEmbeddingService;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VectorBasedRecommendationService {

    private final ProjectRepository projectRepository;
    private final ArtistEmbeddingService artistEmbeddingService;

    public List<ProjectWithScore> getVectorBasedRecommendationsWithScores(User user, int limit) {
        try {
            // 1. 사용자의 아티스트 프로필에서 선호도 벡터 생성
            String userPreferenceVector = getUserPreferenceVector(user);

            if (userPreferenceVector == null) {
                log.info("No user preference vector available for user ID: {}, falling back to popular projects", user.getId());
                return List.of();
            }

            // 2. 벡터 유사도 기반 프로젝트 조회 (거리 값 포함)
            List<Object[]> results = projectRepository.findSimilarProjectsByEmbeddingWithDistance(userPreferenceVector, limit);

            if (results.isEmpty()) {
                log.info("No vector-based recommendations found for user ID: {}, falling back", user.getId());
                return List.of();
            }

            // 3. 거리를 유사도 점수로 변환 (0에 가까우면 높은 점수)
            List<ProjectWithScore> projectsWithScores = new ArrayList<>();
            for (Object[] result : results) {
                Project project = (Project) result[0];
                Double distance = (Double) result[result.length - 1]; // 마지막 컬럼이 similarity_distance

                // 거리를 0-100 점수로 변환 (거리가 0에 가까우면 높은 점수)
                // 일반적으로 코사인 거리는 0-2 범위이므로 적절히 변환
                double score = Math.max(0, 100.0 * (1.0 - Math.min(distance, 1.0)));

                projectsWithScores.add(new ProjectWithScore(project, score));
            }

            log.info("Found {} vector-based recommendations for user ID: {}", projectsWithScores.size(), user.getId());
            return projectsWithScores;

        } catch (Exception e) {
            log.error("Error generating vector-based recommendations for user ID: {}", user.getId(), e);
            return List.of();
        }
    }

    public List<Project> getVectorBasedRecommendations(User user, int limit) {
        return getVectorBasedRecommendationsWithScores(user, limit).stream()
                .map(ProjectWithScore::getProject)
                .toList();
    }

    public static class ProjectWithScore {
        private final Project project;
        private final double score;

        public ProjectWithScore(Project project, double score) {
            this.project = project;
            this.score = score;
        }

        public Project getProject() {
            return project;
        }

        public double getScore() {
            return score;
        }
    }

    private String getUserPreferenceVector(User user) {
        // 사용자의 아티스트 프로필이 있는지 확인하고 임베딩 생성
        // 실제로는 ArtistProfileRepository를 통해 조회해야 함
        try {
            // 임시로 더미 임베딩 반환 (실제 구현에서는 ArtistProfileRepository 주입 필요)
            return getPopularProjectsAverageEmbedding();
        } catch (Exception e) {
            log.error("Error getting user preference vector for user ID: {}", user.getId(), e);
            return null;
        }
    }

    private String getPopularProjectsAverageEmbedding() {
        try {
            // 임베딩이 있는 프로젝트들 중 최신순으로 가져오기
            // 더미 벡터 쿼리 대신 일반 쿼리 사용
            List<Project> projectsWithEmbedding = projectRepository.findByStatusOrderByCreatedAtDesc(
                    kr.artner.domain.project.enums.ProjectStatus.RECRUITING
            );

            // 임베딩이 있는 프로젝트만 필터링
            for (Project project : projectsWithEmbedding) {
                if (project.getConceptEmbedding() != null && !project.getConceptEmbedding().isEmpty()) {
                    return project.getConceptEmbedding();
                }
            }
        } catch (Exception e) {
            log.error("Error getting popular projects average embedding", e);
        }

        return null;
    }

    private List<Project> getFallbackRecommendations(int limit) {
        // 폴백: 기존 규칙 기반 추천 (최신순, 인기순 등)
        List<Project> fallbackProjects = projectRepository.findByStatusOrderByCreatedAtDesc(
                kr.artner.domain.project.enums.ProjectStatus.RECRUITING
        );

        return fallbackProjects.size() > limit ?
                fallbackProjects.subList(0, limit) :
                fallbackProjects;
    }

    public boolean isVectorBasedRecommendationAvailable(User user) {
        return getUserPreferenceVector(user) != null;
    }
}