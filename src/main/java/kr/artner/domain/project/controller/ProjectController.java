package kr.artner.domain.project.controller;

import jakarta.validation.Valid;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.project.dto.ProjectCollabRequestRequest;
import kr.artner.domain.project.dto.ProjectCollabRequestResponse;
import kr.artner.domain.project.dto.ProjectRequest;
import kr.artner.domain.project.dto.ProjectResponse;
import kr.artner.domain.project.enums.ProjectStatus;
import kr.artner.domain.project.service.ProjectCollabRequestService;
import kr.artner.domain.project.service.ProjectService;
import kr.artner.domain.user.entity.User;
import kr.artner.global.auth.CustomUserDetails;
import kr.artner.global.auth.LoginMember;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;
    private final ProjectCollabRequestService collabRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, Object>> createProject(
            @LoginMember CustomUserDetails userDetails,
            @RequestBody @Valid ProjectRequest.CreateProjectRequest request
    ) {
        User user = userDetails.getUser();
        ProjectResponse.CreateProjectResponse response = projectService.createProject(request, user.getId());
        
        return ApiResponse.success(
                "프로젝트가 생성되었습니다.",
                Map.of("project", response)
        );
    }


    
    @PatchMapping("/{projectId}")
    public ApiResponse<Map<String, Object>> updateProject(
            @PathVariable Long projectId,
            @LoginMember CustomUserDetails userDetails,
            @RequestBody @Valid ProjectRequest.UpdateProjectRequest request
    ) {
        User user = userDetails.getUser();
        ProjectResponse.UpdateProjectResponse response = projectService.updateProject(projectId, request, user.getId());
        
        return ApiResponse.success(
                "프로젝트가 수정되었습니다.",
                Map.of("project", response)
        );
    }

    @DeleteMapping("/{projectId}")
    public ApiResponse<Void> deleteProject(
            @PathVariable Long projectId,
            @LoginMember CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        projectService.deleteProject(projectId, user.getId());
        
        return ApiResponse.success("프로젝트가 삭제되었습니다.", null);
    }

    @GetMapping
    public ApiResponse<ProjectResponse.ProjectListResponse> getProjects(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) GenreCode genre,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort
    ) {
        ProjectResponse.ProjectListResponse response = projectService.getProjects(
                keyword, status, genre, region, ownerId, page, size, sort);
        
        return ApiResponse.success("프로젝트 목록을 불러왔습니다.", response);
    }

    @GetMapping("/{projectId}")
    public ApiResponse<ProjectResponse.ProjectDetailResponse> getProjectDetail(@PathVariable Long projectId) {
        ProjectResponse.ProjectDetailResponse response = projectService.getProjectDetail(projectId);
        
        return ApiResponse.success("프로젝트 상세 정보를 불러왔습니다.", response);
    }

    @PostMapping("/{projectId}/collab-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, Object>> requestCollab(
            @PathVariable Long projectId,
            @LoginMember CustomUserDetails userDetails,
            @RequestBody ProjectCollabRequestRequest.CreateRequest request
    ) {
        User user = userDetails.getUser();
        ProjectCollabRequestResponse.CreateResponse response = 
                collabRequestService.createCollabRequest(projectId, request, user.getId());
        
        return ApiResponse.success(
                "협업 요청이 접수되었습니다.",
                Map.of("request", response)
        );
    }

    @PatchMapping("/{projectId}/collab-requests/{requestId}/accept")
    public ApiResponse<ProjectCollabRequestResponse.AcceptResponse> acceptCollab(
            @PathVariable Long projectId,
            @PathVariable Long requestId,
            @LoginMember CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        ProjectCollabRequestResponse.AcceptResponse response = 
                collabRequestService.acceptCollabRequest(projectId, requestId, user.getId());
        
        return ApiResponse.success("협업 요청을 수락했습니다.", response);
    }

    @PatchMapping("/{projectId}/collab-requests/{requestId}/reject")
    public ApiResponse<ProjectCollabRequestResponse.RejectResponse> rejectCollab(
            @PathVariable Long projectId,
            @PathVariable Long requestId,
            @LoginMember CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        ProjectCollabRequestResponse.RejectResponse response = 
                collabRequestService.rejectCollabRequest(projectId, requestId, user.getId());
        
        return ApiResponse.success("협업 요청을 거절했습니다.", response);
    }
}
