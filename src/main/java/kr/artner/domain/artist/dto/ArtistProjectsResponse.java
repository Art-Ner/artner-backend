package kr.artner.domain.artist.dto;

import kr.artner.domain.project.dto.ProjectResponse;
import kr.artner.response.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistProjectsResponse {
    private ProjectsResult result;
    private PageInfo pageInfo;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectsResult {
        private List<ProjectResponse.GetProjectResponse> projects;
    }
}
