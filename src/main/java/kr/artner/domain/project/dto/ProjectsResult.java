package kr.artner.domain.project.dto;

import kr.artner.domain.project.dto.ProjectResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectsResult {
    private List<ProjectResponse.GetProjectResponse> projects;
}
