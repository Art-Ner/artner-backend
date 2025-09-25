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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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

        log.info("프로젝트 목록 조회 시작 - keyword: {}, status: {}, genre: {}, region: {}, ownerId: {}, page: {}, size: {}, sort: {}", 
                keyword, status, genre, region, ownerId, page, size, sort);

        try {
            int pageNumber = (page != null && page >= 0) ? page : 0;
            int pageSize = (size != null && size > 0) ? size : 10;

            if (page != null && page < 0) {
                log.warn("잘못된 페이지 번호 요청: {}, 기본값 0으로 설정", page);
            }
            if (size != null && size <= 0) {
                log.warn("잘못된 페이지 크기 요청: {}, 기본값 10으로 설정", size);
            }

            Pageable pageable = createPageable(pageNumber, pageSize, sort);

            Page<Project> projectPage;
            try {
                log.info("정확한 매칭 검색으로 프로젝트 조회 시작");
                projectPage = projectRepository.findProjectsWithFilters(
                        keyword, status, genre, region, ownerId, pageable);
                log.info("정확한 매칭 검색 완료 - {}개 조회됨", projectPage.getContent().size());
            } catch (Exception e) {
                log.error("정확한 매칭 검색에서 오류 발생, 기본 조회로 전환", e);
                try {
                    // 오류 발생시 기본 조회로 fallback
                    List<Project> allProjects = projectRepository.findAll();
                    log.info("기본 findAll로 fallback 성공 - {}개", allProjects.size());
                    projectPage = new PageImpl<>(allProjects, pageable, allProjects.size());
                } catch (Exception fallbackException) {
                    log.error("기본 조회에서도 오류 발생", fallbackException);
                    throw fallbackException;
                }
            }

            log.info("프로젝트 조회 완료 - 총 {}개 중 {}개 조회됨 ({}페이지)", 
                    projectPage.getTotalElements(), projectPage.getContent().size(), pageNumber);

            // 참여자 정보 포함해서 응답 생성
            List<Long> projectIds = projectPage.getContent().stream()
                    .map(Project::getId)
                    .collect(Collectors.toList());

            Map<Long, Integer> participantCounts = getParticipantCounts(projectIds);
            Map<Long, List<ProjectMember>> participantsMap = getProjectParticipants(projectIds);

            List<ProjectResponse.ProjectSummary> summaries = projectPage.getContent().stream()
                    .map(project -> {
                        try {
                            Long projectId = project.getId();
                            int participantCount = participantCounts.getOrDefault(projectId, 0);
                            List<ProjectMember> participants = participantsMap.getOrDefault(projectId, List.of());
                            
                            log.debug("프로젝트 {}의 참여자 수: {}, 참여자 목록 크기: {}", 
                                    projectId, participantCount, participants.size());
                            
                            return ProjectConverter.toProjectSummary(project, participantCount, participants);
                        } catch (Exception e) {
                            log.error("프로젝트 요약 생성 중 오류 발생 - projectId: {}", project.getId(), e);
                            return null;
                        }
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());

            log.info("프로젝트 목록 조회 완료 - 최종 {}개 프로젝트 반환", summaries.size());
            return ProjectConverter.toProjectListResponse(projectPage, summaries);

        } catch (Exception e) {
            log.error("프로젝트 목록 조회 중 오류 발생 - keyword: {}, status: {}, genre: {}, region: {}, ownerId: {}", 
                    keyword, status, genre, region, ownerId, e);
            throw new RuntimeException("프로젝트 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    private Pageable createPageable(int page, int size, String sort) {
        try {
            if (sort == null || sort.isBlank()) {
                return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            }

            String[] sortParts = sort.split(",");
            String field = (sortParts.length > 0 && !sortParts[0].trim().isEmpty())
                    ? sortParts[0].trim()
                    : "createdAt";

            if (!isValidSortField(field)) {
                log.warn("유효하지 않은 정렬 필드: {}, 기본값 'createdAt' 사용", field);
                field = "createdAt";
            }

            Sort.Direction direction = Sort.Direction.DESC;
            if (sortParts.length > 1) {
                String directionToken = sortParts[1].trim();
                direction = "asc".equalsIgnoreCase(directionToken)
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;
            }

            return PageRequest.of(page, size, Sort.by(direction, field));
        } catch (Exception e) {
            log.error("페이지 생성 중 오류 발생 - page: {}, size: {}, sort: {}", page, size, sort, e);
            return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }
    }

    private boolean isValidSortField(String field) {
        return List.of("createdAt", "updatedAt", "title", "status", "targetGenre").contains(field);
    }

    private Map<Long, Integer> getParticipantCounts(List<Long> projectIds) {
        Map<Long, Integer> counts = new HashMap<>();
        if (projectIds.isEmpty()) {
            log.debug("참여자 수 조회 대상 프로젝트가 없습니다.");
            return counts;
        }

        try {
            log.debug("{}개 프로젝트의 참여자 수 조회 시작", projectIds.size());
            List<ProjectMemberRepository.ProjectMemberCount> results =
                    projectMemberRepository.countMembersByProjectIds(projectIds);

            for (ProjectMemberRepository.ProjectMemberCount result : results) {
                counts.put(result.getProjectId(), result.getCnt().intValue());
            }
            log.debug("참여자 수 조회 완료 - {}개 프로젝트 처리됨", results.size());
        } catch (Exception e) {
            log.error("참여자 수 조회 중 오류 발생 - projectIds: {}", projectIds, e);
            for (Long projectId : projectIds) {
                counts.put(projectId, 0);
            }
        }
        return counts;
    }

    private Map<Long, List<ProjectMember>> getProjectParticipants(List<Long> projectIds) {
        Map<Long, List<ProjectMember>> participantsMap = new HashMap<>();
        if (projectIds.isEmpty()) {
            log.debug("참여자 목록 조회 대상 프로젝트가 없습니다.");
            return participantsMap;
        }

        try {
            log.debug("{}개 프로젝트의 참여자 목록 조회 시작", projectIds.size());
            List<ProjectMember> allMembers = projectMemberRepository.findByProjectIdInOrderByJoinedAt(projectIds);
            
            for (ProjectMember member : allMembers) {
                Long projectId = member.getProject().getId();
                participantsMap.computeIfAbsent(projectId, k -> new ArrayList<>()).add(member);
            }
            log.debug("참여자 목록 조회 완료 - 총 {}명의 참여자 처리됨", allMembers.size());
        } catch (Exception e) {
            log.error("참여자 목록 조회 중 오류 발생 - projectIds: {}", projectIds, e);
            for (Long projectId : projectIds) {
                participantsMap.put(projectId, new ArrayList<>());
            }
        }

        return participantsMap;
    }

    public ProjectResponse.ProjectDetailResponse getProjectDetail(Long projectId) {
        Project project = projectRepository.findByIdWithOwnerAndUser(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        // Use derived method with @EntityGraph to fetch artist
        List<ProjectMember> members = projectMemberRepository.findByProjectIdOrderByJoinedAt(projectId);

        return ProjectConverter.toProjectDetailResponse(project, members);
    }
}
