package kr.artner.domain.artist.service;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.artist.dto.ArtistResponse;
import kr.artner.domain.project.dto.ProjectConverter;
import kr.artner.domain.project.dto.ProjectResponse;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.project.repository.ProjectMemberRepository;
import kr.artner.domain.project.entity.ProjectMember;
import kr.artner.domain.user.entity.User;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import kr.artner.response.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    @Transactional(readOnly = true)
    public ArtistResponse.ArtistListResponse getArtists(String keyword, Integer limit, Integer offset, String genre, String role) {
        // 기본값 설정
        int pageSize = (limit != null && limit > 0) ? Math.min(limit, 100) : 10;
        int pageNumber = (offset != null && offset >= 0) ? offset / pageSize : 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<ArtistProfile> artistPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 키워드 검색: 아티스트명 또는 헤드라인에서 검색
            artistPage = artistProfileRepository.findByArtistNameContainingIgnoreCaseOrHeadlineContainingIgnoreCase(
                keyword.trim(), keyword.trim(), pageable);
        } else {
            // 전체 목록 조회
            artistPage = artistProfileRepository.findAll(pageable);
        }

        List<ArtistResponse.ArtistListItem> artists = artistPage.getContent().stream()
                .map(this::convertToArtistListItem)
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.builder()
                .totalCount(artistPage.getTotalElements())
                .limit(pageSize)
                .offset((long) pageNumber * pageSize)
                .hasMore(artistPage.hasNext())
                .build();

        return ArtistResponse.ArtistListResponse.builder()
                .artists(artists)
                .pageInfo(pageInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public ArtistResponse.GetArtistProfileResponse getArtistProfile(Long artistId) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        List<String> genres = artistProfile.getGenres().stream()
                .map(genre -> genre.getGenreCode().name())
                .collect(Collectors.toList());

        List<String> roles = artistProfile.getRoles().stream()
                .map(role -> role.getRoleCode().name())
                .collect(Collectors.toList());

        return ArtistResponse.GetArtistProfileResponse.builder()
                .id(artistProfile.getId())
                .artistName(artistProfile.getArtistName())
                .profileImageUrl(artistProfile.getProfileImageUrl())
                .headline(artistProfile.getHeadline())
                .bio(artistProfile.getBio())
                .urls(artistProfile.getUrls())
                .genres(genres)
                .roles(roles)
                .build();
    }

    private ArtistResponse.ArtistListItem convertToArtistListItem(ArtistProfile artistProfile) {
        List<String> genres = artistProfile.getGenres().stream()
                .map(genre -> genre.getGenreCode().name())
                .collect(Collectors.toList());

        List<String> roles = artistProfile.getRoles().stream()
                .map(role -> role.getRoleCode().name())
                .collect(Collectors.toList());

        return ArtistResponse.ArtistListItem.builder()
                .id(artistProfile.getId())
                .artistName(artistProfile.getArtistName())
                .profileImageUrl(artistProfile.getProfileImageUrl())
                .headline(artistProfile.getHeadline())
                .bio(artistProfile.getBio())
                .urls(artistProfile.getUrls())
                .genres(genres)
                .roles(roles)
                .build();
    }
}
