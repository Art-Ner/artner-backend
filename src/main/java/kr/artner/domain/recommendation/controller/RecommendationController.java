package kr.artner.domain.recommendation.controller;

import kr.artner.domain.recommendation.dto.RecommendationResponse;
import kr.artner.domain.recommendation.service.RecommendationService;
import kr.artner.domain.user.entity.User;
import kr.artner.global.auth.LoginMember;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/batch/generate")
    public ApiResponse<RecommendationResponse.BatchGenerateResponse> generateBatchRecommendations() {
        RecommendationResponse.BatchGenerateResponse response = recommendationService.generateBatchRecommendations();
        return ApiResponse.success(response);
    }

    @GetMapping("/projects")
    public ApiResponse<RecommendationResponse.GetRecommendationsResponse> getRecommendedProjects(
            @LoginMember User user) {
        RecommendationResponse.GetRecommendationsResponse response = recommendationService.getRecommendations(user);
        return ApiResponse.success(response);
    }
}
