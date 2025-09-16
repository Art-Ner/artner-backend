package kr.artner.domain.project.service;

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

    @Transactional
    public ProjectResponse.CreateProjectResponse createProject(ProjectRequest.CreateProjectRequest request, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Project project = ProjectConverter.toEntity(request, owner);
        Project savedProject = projectRepository.save(project);

        return ProjectConverter.toCreateProjectResponse(savedProject);
    }

    @Transactional
    public ProjectResponse.UpdateProjectResponse updateProject(Long projectId, ProjectRequest.UpdateProjectRequest request, Long ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        if (!project.getOwner().getId().equals(ownerId)) {
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

        if (!project.getOwner().getId().equals(ownerId)) {
            throw new IllegalArgumentException("프로젝트를 삭제할 권한이 없습니다.");
        }

        projectRepository.delete(project);
    }

    public ProjectResponse.ProjectListResponse getProjects(
            String keyword, ProjectStatus status, GenreCode genre, String region, Long ownerId,
            Integer page, Integer size, String sort) {
        
        // 기본값 설정
        page = page != null ? page : 0;
        size = size != null ? size : 20;
        sort = sort != null ? sort : "createdAt,desc";
        
        // 정렬 파싱
        Pageable pageable = createPageable(page, size, sort);
        
        // 프로젝트 검색
        Page<Project> projectPage = projectRepository.findProjectsWithFilters(
                keyword, status, genre, region, ownerId, pageable);
        
        // 참여자 수 계산
        List<Long> projectIds = projectPage.getContent().stream()
                .map(Project::getId)
                .collect(Collectors.toList());
        
        Map<Long, Integer> participantCounts = getParticipantCounts(projectIds);
        
        // DTO 변환
        List<ProjectResponse.ProjectSummary> summaries = projectPage.getContent().stream()
                .map(project -> ProjectConverter.toProjectSummary(
                        project, 
                        participantCounts.getOrDefault(project.getId(), 0)))
                .collect(Collectors.toList());
        
        return ProjectConverter.toProjectListResponse(projectPage, summaries);
    }
    
    private Pageable createPageable(Integer page, Integer size, String sort) {
        String[] sortParts = sort.split(",");
        String field = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1]) 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        return PageRequest.of(page, size, Sort.by(direction, field));
    }
    
    private Map<Long, Integer> getParticipantCounts(List<Long> projectIds) {
        if (projectIds.isEmpty()) {
            return new HashMap<>();
        }
        
        List<Object[]> results = projectMemberRepository.countMembersByProjectIds(projectIds);
        Map<Long, Integer> counts = new HashMap<>();
        
        for (Object[] result : results) {
            Long projectId = (Long) result[0];
            Long count = (Long) result[1];
            counts.put(projectId, count.intValue());
        }
        
        return counts;
    }

    public ProjectResponse.ProjectDetailResponse getProjectDetail(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        
        List<ProjectMember> members = projectMemberRepository.findByProjectIdWithArtist(projectId);
        
        return ProjectConverter.toProjectDetailResponse(project, members);
    }
}
