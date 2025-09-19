package kr.artner.domain.project.service;

import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.global.service.OpenAIEmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectEmbeddingService {

    private final ProjectRepository projectRepository;
    private final OpenAIEmbeddingService openAIEmbeddingService;

    public void generateAndSaveProjectEmbedding(Project project) {
        try {
            String embedding = openAIEmbeddingService.generateProjectEmbedding(
                    project.getTitle(),
                    project.getConcept(),
                    project.getTargetGenre().name(),
                    project.getTargetRegion()
            );

            if (embedding != null) {
                project.updateConceptEmbedding(embedding);
                projectRepository.save(project);
                log.info("Successfully generated and saved embedding for project ID: {}", project.getId());
            } else {
                log.warn("Failed to generate embedding for project ID: {}", project.getId());
            }
        } catch (Exception e) {
            log.error("Error generating embedding for project ID: {}", project.getId(), e);
        }
    }

    public void generateEmbeddingsForAllProjects() {
        List<Project> projects = projectRepository.findAllByConceptEmbeddingIsNull();
        log.info("Generating embeddings for {} projects without embeddings", projects.size());

        for (Project project : projects) {
            generateAndSaveProjectEmbedding(project);
        }
    }

    public void regenerateEmbeddingForProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
        generateAndSaveProjectEmbedding(project);
    }
}