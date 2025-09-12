package kr.artner.domain.project.dto;

import kr.artner.domain.project.entity.Project;
import kr.artner.domain.user.dto.UserConverter;

public class ProjectConverter {

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
