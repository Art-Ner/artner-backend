package kr.artner.domain.project.service;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.project.dto.ProjectCollabRequestConverter;
import kr.artner.domain.project.dto.ProjectCollabRequestRequest;
import kr.artner.domain.project.dto.ProjectCollabRequestResponse;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.entity.ProjectCollabRequest;
import kr.artner.domain.project.entity.ProjectMember;
import kr.artner.domain.project.enums.CollabStatus;
import kr.artner.domain.project.repository.ProjectCollabRequestRepository;
import kr.artner.domain.project.repository.ProjectMemberRepository;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectCollabRequestService {

    private final ProjectCollabRequestRepository collabRequestRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectCollabRequestResponse.CreateResponse createCollabRequest(
            Long projectId, 
            ProjectCollabRequestRequest.CreateRequest request, 
            Long requesterId) {
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        
        User user = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        
        ArtistProfile requester = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("아티스트 프로필이 존재하지 않습니다."));
        
        // 프로젝트 소유자는 자신의 프로젝트에 협업 요청할 수 없음
        if (project.getOwner().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("자신이 소유한 프로젝트에는 협업 요청할 수 없습니다.");
        }
        
        // 이미 협업 요청한 경우 중복 요청 방지
        if (collabRequestRepository.findByProjectIdAndRequesterId(projectId, requester.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 협업 요청한 프로젝트입니다.");
        }
        
        ProjectCollabRequest collabRequest = ProjectCollabRequestConverter.toEntity(request, project, requester);
        ProjectCollabRequest savedRequest = collabRequestRepository.save(collabRequest);
        
        return ProjectCollabRequestConverter.toCreateResponse(savedRequest);
    }

    @Transactional
    public ProjectCollabRequestResponse.AcceptResponse acceptCollabRequest(
            Long projectId, 
            Long requestId, 
            Long ownerId) {
        
        ProjectCollabRequest collabRequest = collabRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 협업 요청입니다."));
        
        // 프로젝트 ID 일치 확인
        if (!collabRequest.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("프로젝트와 요청이 일치하지 않습니다.");
        }
        
        // 프로젝트 소유자만 수락 가능
        if (!collabRequest.getProject().getOwner().getUser().getId().equals(ownerId)) {
            throw new IllegalArgumentException("프로젝트 소유자만 협업 요청을 수락할 수 있습니다.");
        }
        
        // 이미 처리된 요청인지 확인
        if (collabRequest.getStatus() != CollabStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 요청입니다.");
        }
        
        User ownerUser = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        
        ArtistProfile owner = artistProfileRepository.findByUser(ownerUser)
                .orElseThrow(() -> new IllegalArgumentException("아티스트 프로필이 존재하지 않습니다."));
        
        // 요청 수락 처리
        collabRequest.acceptRequest(owner);
        
        // 프로젝트 멤버 생성 (요청자가 이미 ArtistProfile)
        ProjectMember member = ProjectMember.builder()
                .project(collabRequest.getProject())
                .artist(collabRequest.getRequester())
                .joinedAt(LocalDateTime.now())
                .build();
        
        ProjectMember savedMember = projectMemberRepository.save(member);
        
        return ProjectCollabRequestConverter.toAcceptResponse(collabRequest, savedMember);
    }

    @Transactional
    public ProjectCollabRequestResponse.RejectResponse rejectCollabRequest(
            Long projectId, 
            Long requestId, 
            Long ownerId) {
        
        ProjectCollabRequest collabRequest = collabRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 협업 요청입니다."));
        
        // 프로젝트 ID 일치 확인
        if (!collabRequest.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("프로젝트와 요청이 일치하지 않습니다.");
        }
        
        // 프로젝트 소유자만 거절 가능
        if (!collabRequest.getProject().getOwner().getUser().getId().equals(ownerId)) {
            throw new IllegalArgumentException("프로젝트 소유자만 협업 요청을 거절할 수 있습니다.");
        }
        
        // 이미 처리된 요청인지 확인
        if (collabRequest.getStatus() != CollabStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 요청입니다.");
        }
        
        User ownerUser = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        
        ArtistProfile owner = artistProfileRepository.findByUser(ownerUser)
                .orElseThrow(() -> new IllegalArgumentException("아티스트 프로필이 존재하지 않습니다."));
        
        // 요청 거절 처리
        collabRequest.rejectRequest(owner);
        
        return ProjectCollabRequestConverter.toRejectResponse(collabRequest);
    }
}
