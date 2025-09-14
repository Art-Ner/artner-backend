package kr.artner.domain.project.dto;

import kr.artner.domain.artist.dto.ArtistConverter;
import kr.artner.domain.project.entity.Project;

public class ProjectConverter {

    public static ProjectResponse.GetProjectResponse toGetProjectResponse(Project project) {
        return ProjectResponse.GetProjectResponse.builder()
                .id(project.getId())
                .owner(ArtistConverter.toGetArtistProfileResponse(project.getOwner()))
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
