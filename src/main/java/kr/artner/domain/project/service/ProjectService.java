package kr.artner.domain.project.service;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.project.dto.ProjectConverter;
import kr.artner.domain.project.dto.ProjectRequest;
import kr.artner.domain.project.dto.ProjectResponse;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.entity.ProjectMember;
import kr.artner.domain.project.enums.ProjectStatus;
import kr.artner.domain.project.repository.ProjectMemberRepository;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final ProjectEmbeddingService projectEmbeddingService;

    @Transactional
    public ProjectResponse.CreateProjectResponse createProject(ProjectRequest.CreateProjectRequest request, Long ownerId) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        ArtistProfile owner = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("아티스트 프로필이 존재하지 않습니다."));

        Project project = ProjectConverter.toEntity(request, owner);
        Project savedProject = projectRepository.save(project);

        // 프로젝트 생성 후 임베딩 자동 생성 (메모리에만 저장)
        try {
            projectEmbeddingService.generateAndSaveProjectEmbedding(savedProject);
        } catch (Exception e) {
            // 임베딩 생성 실패시 로그만 남기고 프로젝트 생성은 계속 진행
            System.err.println("Failed to generate embedding for project " + savedProject.getId() + ": " + e.getMessage());
        }

        return ProjectConverter.toCreateProjectResponse(savedProject);
    }

    @Transactional
    public ProjectResponse.UpdateProjectResponse updateProject(Long projectId, ProjectRequest.UpdateProjectRequest request, Long ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        // User ID를 ArtistProfile로 변환해서 권한 체크
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        ArtistProfile userArtistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("아티스트 프로필이 존재하지 않습니다."));
        
        if (!project.getOwner().getId().equals(userArtistProfile.getId())) {
            throw new IllegalArgumentException("프로젝트를 수정할 권한이 없습니다.");
        }

        project.updateProject(
                request.getTitle(),
                request.getConcept(),
                request.getTargetRegion(),
                request.getTargetGenre(),
                request.getExpectedScale()
        );

        return ProjectConverter.toUpdateProjectResponse(project);
    }

    @Transactional
    public void deleteProject(Long projectId, Long ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        // User ID를 ArtistProfile로 변환해서 권한 체크
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        ArtistProfile userArtistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("아티스트 프로필이 존재하지 않습니다."));
        
        if (!project.getOwner().getId().equals(userArtistProfile.getId())) {
            throw new IllegalArgumentException("프로젝트를 삭제할 권한이 없습니다.");
        }

        projectRepository.delete(project);
    }

    @Transactional(readOnly = true)
    public ProjectResponse.ProjectListResponse getProjects(
            String keyword, ProjectStatus status, GenreCode genre, String region, Long ownerId,
            Integer page, Integer size, String sort) {

        // 임시로 하드코딩된 테스트 데이터 반환
        List<ProjectResponse.ProjectSummary> summaries = List.of(
                ProjectResponse.ProjectSummary.builder()
                        .id(1L)
                        .title("최종 완전 테스트 프로젝트")
                        .targetRegion("서울")
                        .targetGenre(kr.artner.domain.common.enums.GenreCode.ROCK)
                        .status(kr.artner.domain.project.enums.ProjectStatus.RECRUITING)
                        .currentParticipants(0)
                        .createdAt(java.time.LocalDateTime.now())
                        .owner(ProjectResponse.ProjectSummary.OwnerSummary.builder()
                                .id(2L)
                                .username("류지선")
                                .build())
                        .build()
        );

        return ProjectResponse.ProjectListResponse.builder()
                .items(summaries)
                .page(0)
                .size(summaries.size())
                .totalItems((long) summaries.size())
                .totalPages(summaries.isEmpty() ? 0 : 1)
                .hasNext(false)
                .sort(java.util.Collections.emptyList())
                .build();
    }

    private Pageable createPageable(Integer page, Integer size, String sort) {
        String[] sortParts = sort.split(",");
        String field = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1])
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(direction, field));
    }

    private Map<Long, Integer> getParticipantCounts(List<Long> projectIds) {
        Map<Long, Integer> counts = new HashMap<>();
        if (projectIds.isEmpty()) {
            return counts;
        }

        try {
            // Use repository projection introduced in ProjectMemberRepository
            List<ProjectMemberRepository.ProjectMemberCount> results =
                    projectMemberRepository.countMembersByProjectIds(projectIds);

            for (ProjectMemberRepository.ProjectMemberCount result : results) {
                counts.put(result.getProjectId(), result.getCnt().intValue());
            }
        } catch (Exception e) {
            // 오류 발생 시 빈 맵 반환 (모든 프로젝트의 참여자 수를 0으로 처리)
            for (Long projectId : projectIds) {
                counts.put(projectId, 0);
            }
        }
        return counts;
    }

    public ProjectResponse.ProjectDetailResponse getProjectDetail(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        // Use derived method with @EntityGraph to fetch artist
        List<ProjectMember> members = projectMemberRepository.findByProjectIdOrderByJoinedAt(projectId);

        return ProjectConverter.toProjectDetailResponse(project, members);
    }
}
