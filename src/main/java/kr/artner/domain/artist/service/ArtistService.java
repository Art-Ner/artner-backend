package kr.artner.domain.artist.service;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.project.dto.ProjectConverter;
import kr.artner.domain.project.dto.ProjectResponse;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.project.repository.ProjectMemberRepository;
import kr.artner.domain.project.entity.ProjectMember;
import kr.artner.domain.user.entity.User;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ArtistProfileRepository artistProfileRepository;

    @Transactional(readOnly = true)
    public Page<ProjectResponse.GetProjectResponse> getMyProjects(User user, boolean isOwner, Pageable pageable) {
        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        if (isOwner) {
            return projectRepository.findAllByOwner(artistProfile, pageable)
                    .map(ProjectConverter::toGetProjectResponse);
        } else {
            Page<ProjectMember> projectMembersPage = projectMemberRepository.findAllByArtist(artistProfile, pageable);
            List<ProjectResponse.GetProjectResponse> projects = projectMembersPage.stream()
                    .filter(projectMember -> !projectMember.getProject().getOwner().equals(artistProfile))
                    .map(projectMember -> ProjectConverter.toGetProjectResponse(projectMember.getProject()))
                    .collect(Collectors.toList());
            return new PageImpl<>(projects, pageable, projectMembersPage.getTotalElements());
        }
    }
}
