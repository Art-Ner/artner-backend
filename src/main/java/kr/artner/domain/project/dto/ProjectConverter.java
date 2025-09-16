package kr.artner.domain.project.dto;

import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.entity.ProjectMember;
import kr.artner.domain.project.enums.ProjectStatus;
import kr.artner.domain.user.dto.UserConverter;
import kr.artner.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectConverter {

    public static Project toEntity(ProjectRequest.CreateProjectRequest request, User owner) {
        return Project.builder()
                .owner(owner)
                .title(request.getTitle())
                .concept(request.getConcept())
                .targetRegion(request.getTargetRegion())
                .targetGenre(request.getTargetGenre())
                .expectedScale(request.getExpectedScale())
                .status(ProjectStatus.RECRUITING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ProjectResponse.CreateProjectResponse toCreateProjectResponse(Project project) {
        return ProjectResponse.CreateProjectResponse.builder()
                .id(project.getId())
                .ownerId(project.getOwner().getId())
                .title(project.getTitle())
                .concept(project.getConcept())
                .targetRegion(project.getTargetRegion())
                .targetGenre(project.getTargetGenre())
                .expectedScale(project.getExpectedScale())
                .status(project.getStatus())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public static ProjectResponse.UpdateProjectResponse toUpdateProjectResponse(Project project) {
        return ProjectResponse.UpdateProjectResponse.builder()
                .id(project.getId())
                .ownerId(project.getOwner().getId())
                .title(project.getTitle())
                .concept(project.getConcept())
                .targetRegion(project.getTargetRegion())
                .targetGenre(project.getTargetGenre())
                .expectedScale(project.getExpectedScale())
                .status(project.getStatus())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public static ProjectResponse.ProjectSummary toProjectSummary(Project project, Integer currentParticipants) {
        return ProjectResponse.ProjectSummary.builder()
                .id(project.getId())
                .title(project.getTitle())
                .targetRegion(project.getTargetRegion())
                .targetGenre(project.getTargetGenre())
                .status(project.getStatus())
                .currentParticipants(currentParticipants)
                .createdAt(project.getCreatedAt())
                .owner(ProjectResponse.ProjectSummary.OwnerSummary.builder()
                        .id(project.getOwner().getId())
                        .username(project.getOwner().getUsername())
                        .build())
                .build();
    }

    public static ProjectResponse.ProjectListResponse toProjectListResponse(Page<Project> projectPage, List<ProjectResponse.ProjectSummary> summaries) {
        List<ProjectResponse.ProjectListResponse.SortInfo> sortInfos = projectPage.getSort().stream()
                .map(order -> ProjectResponse.ProjectListResponse.SortInfo.builder()
                        .field(order.getProperty())
                        .dir(order.getDirection().name().toLowerCase())
                        .build())
                .collect(Collectors.toList());

        return ProjectResponse.ProjectListResponse.builder()
                .items(summaries)
                .page(projectPage.getNumber())
                .size(projectPage.getSize())
                .totalItems(projectPage.getTotalElements())
                .totalPages(projectPage.getTotalPages())
                .hasNext(projectPage.hasNext())
                .sort(sortInfos)
                .build();
    }

    public static ProjectResponse.ProjectDetailResponse toProjectDetailResponse(Project project, List<ProjectMember> members) {
        ProjectResponse.ProjectDetailResponse.ProjectDetail projectDetail = 
                ProjectResponse.ProjectDetailResponse.ProjectDetail.builder()
                        .id(project.getId())
                        .ownerId(project.getOwner().getId())
                        .title(project.getTitle())
                        .concept(project.getConcept())
                        .targetRegion(project.getTargetRegion())
                        .targetGenre(project.getTargetGenre())
                        .expectedScale(project.getExpectedScale())
                        .status(project.getStatus())
                        .createdAt(project.getCreatedAt())
                        .updatedAt(project.getUpdatedAt())
                        .build();

        List<ProjectResponse.MemberSummary> memberSummaries = members.stream()
                .map(member -> ProjectResponse.MemberSummary.builder()
                        .artistProfileId(member.getArtist().getId())
                        .artistName(member.getArtist().getArtistName())
                        .profileImageUrl(member.getArtist().getProfileImageUrl())
                        .joinedAt(member.getJoinedAt())
                        .build())
                .collect(Collectors.toList());

        return ProjectResponse.ProjectDetailResponse.builder()
                .project(projectDetail)
                .members(memberSummaries)
                .build();
    }

    public static ProjectResponse.GetProjectResponse toGetProjectResponse(Project project) {
        return ProjectResponse.GetProjectResponse.builder()
                .id(project.getId())
                .owner(UserConverter.toGetUserInfoResponse(project.getOwner()))
                .title(project.getTitle())
                .concept(project.getConcept())
                .targetRegion(project.getTargetRegion())
                .targetGenre(project.getTargetGenre().name())
                .expectedScale(project.getExpectedScale())
                .status(project.getStatus().name())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
