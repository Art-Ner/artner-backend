package kr.artner.domain.project.dto;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.entity.ProjectCollabRequest;
import kr.artner.domain.project.entity.ProjectMember;
import kr.artner.domain.project.enums.CollabStatus;
import kr.artner.domain.user.entity.User;

import java.time.LocalDateTime;

public class ProjectCollabRequestConverter {

    public static ProjectCollabRequest toEntity(ProjectCollabRequestRequest.CreateRequest request, Project project, User requester) {
        return ProjectCollabRequest.builder()
                .project(project)
                .requester(requester)
                .status(CollabStatus.PENDING)
                .message(request.getMessage())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ProjectCollabRequestResponse.CreateResponse toCreateResponse(ProjectCollabRequest collabRequest) {
        return ProjectCollabRequestResponse.CreateResponse.builder()
                .id(collabRequest.getId())
                .projectId(collabRequest.getProject().getId())
                .requesterId(collabRequest.getRequester().getId())
                .status(collabRequest.getStatus())
                .message(collabRequest.getMessage())
                .createdAt(collabRequest.getCreatedAt())
                .build();
    }

    public static ProjectCollabRequestResponse.AcceptResponse toAcceptResponse(ProjectCollabRequest collabRequest, ProjectMember member) {
        ProjectCollabRequestResponse.AcceptResponse.RequestInfo requestInfo = 
                ProjectCollabRequestResponse.AcceptResponse.RequestInfo.builder()
                        .id(collabRequest.getId())
                        .projectId(collabRequest.getProject().getId())
                        .requesterId(collabRequest.getRequester().getId())
                        .status(collabRequest.getStatus())
                        .decidedAt(collabRequest.getDecidedAt())
                        .decidedBy(collabRequest.getDecidedBy().getId())
                        .build();

        ProjectCollabRequestResponse.AcceptResponse.MemberInfo memberInfo = 
                ProjectCollabRequestResponse.AcceptResponse.MemberInfo.builder()
                        .artistId(member.getArtist().getId())
                        .joinedAt(member.getJoinedAt())
                        .build();

        return ProjectCollabRequestResponse.AcceptResponse.builder()
                .request(requestInfo)
                .member(memberInfo)
                .build();
    }

    public static ProjectCollabRequestResponse.RejectResponse toRejectResponse(ProjectCollabRequest collabRequest) {
        ProjectCollabRequestResponse.RejectResponse.RequestInfo requestInfo = 
                ProjectCollabRequestResponse.RejectResponse.RequestInfo.builder()
                        .id(collabRequest.getId())
                        .projectId(collabRequest.getProject().getId())
                        .requesterId(collabRequest.getRequester().getId())
                        .status(collabRequest.getStatus())
                        .decidedAt(collabRequest.getDecidedAt())
                        .decidedBy(collabRequest.getDecidedBy().getId())
                        .build();

        return ProjectCollabRequestResponse.RejectResponse.builder()
                .request(requestInfo)
                .build();
    }
}