package kr.artner.domain.admin.controller;

import kr.artner.domain.project.service.ProjectEmbeddingService;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/embeddings")
@RequiredArgsConstructor
public class EmbeddingAdminController {

    private final ProjectEmbeddingService projectEmbeddingService;

    @PostMapping("/projects/generate-all")
    public ApiResponse<String> generateAllProjectEmbeddings() {
        log.info("Starting to generate embeddings for all projects without embeddings");

        try {
            projectEmbeddingService.generateEmbeddingsForAllProjects();
            return ApiResponse.success("모든 프로젝트 임베딩 생성이 시작되었습니다.");
        } catch (Exception e) {
            log.error("Error generating all project embeddings", e);
            return ApiResponse.failure("임베딩 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/projects/{projectId}/regenerate")
    public ApiResponse<String> regenerateProjectEmbedding(@PathVariable Long projectId) {
        log.info("Regenerating embedding for project ID: {}", projectId);

        try {
            projectEmbeddingService.regenerateEmbeddingForProject(projectId);
            return ApiResponse.success("프로젝트 ID " + projectId + "의 임베딩이 재생성되었습니다.");
        } catch (Exception e) {
            log.error("Error regenerating embedding for project ID: {}", projectId, e);
            return ApiResponse.failure("임베딩 재생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("Embedding service is running");
    }
}