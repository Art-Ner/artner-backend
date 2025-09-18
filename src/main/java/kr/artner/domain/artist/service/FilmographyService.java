package kr.artner.domain.artist.service;

import kr.artner.domain.artist.dto.FilmographyRequest;
import kr.artner.domain.artist.dto.FilmographyResponse;
import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.entity.Filmography;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.artist.repository.FilmographyRepository;
import kr.artner.domain.user.entity.User;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import kr.artner.response.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmographyService {

    private final FilmographyRepository filmographyRepository;
    private final ArtistProfileRepository artistProfileRepository;

    @Transactional
    public FilmographyResponse.CreateFilmographyResponse createFilmography(User user, FilmographyRequest.CreateFilmography request) {
        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        Filmography filmography = Filmography.builder()
                .artistProfile(artistProfile)
                .title(request.getTitle())
                .description(request.getDescription())
                .releasedAt(request.getReleasedAt())
                .mediaUrl(request.getMediaUrl())
                .build();

        Filmography savedFilmography = filmographyRepository.save(filmography);

        return FilmographyResponse.CreateFilmographyResponse.builder()
                .id(savedFilmography.getId())
                .title(savedFilmography.getTitle())
                .description(savedFilmography.getDescription())
                .releasedAt(savedFilmography.getReleasedAt())
                .mediaUrl(savedFilmography.getMediaUrl())
                .message("필모그래피가 성공적으로 등록되었습니다.")
                .build();
    }

    @Transactional(readOnly = true)
    public FilmographyResponse.FilmographyListResponse getFilmographyList(Long artistProfileId, Pageable pageable) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        Page<Filmography> filmographyPage = filmographyRepository.findAllByArtistProfileOrderByReleasedAtDesc(artistProfile, pageable);

        List<FilmographyResponse.FilmographyItem> filmographies = filmographyPage.getContent().stream()
                .map(this::convertToFilmographyItem)
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.builder()
                .totalCount(filmographyPage.getTotalElements())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .hasMore(filmographyPage.hasNext())
                .build();

        return FilmographyResponse.FilmographyListResponse.builder()
                .filmographies(filmographies)
                .pageInfo(pageInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public FilmographyResponse.FilmographyItem getFilmographyDetail(Long artistProfileId, Long filmographyId) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        Filmography filmography = filmographyRepository.findByIdAndArtistProfile(filmographyId, artistProfile)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FILMOGRAPHY_NOT_FOUND));

        return convertToFilmographyItem(filmography);
    }

    @Transactional
    public FilmographyResponse.FilmographyItem updateFilmography(User user, Long filmographyId, FilmographyRequest.UpdateFilmography request) {
        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        Filmography filmography = filmographyRepository.findByIdAndArtistProfile(filmographyId, artistProfile)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FILMOGRAPHY_NOT_FOUND));

        filmography.setTitle(request.getTitle());
        filmography.setDescription(request.getDescription());
        filmography.setReleasedAt(request.getReleasedAt());
        filmography.setMediaUrl(request.getMediaUrl());

        Filmography savedFilmography = filmographyRepository.save(filmography);
        return convertToFilmographyItem(savedFilmography);
    }

    @Transactional
    public void deleteFilmography(User user, Long filmographyId) {
        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        Filmography filmography = filmographyRepository.findByIdAndArtistProfile(filmographyId, artistProfile)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FILMOGRAPHY_NOT_FOUND));

        filmographyRepository.delete(filmography);
    }

    private FilmographyResponse.FilmographyItem convertToFilmographyItem(Filmography filmography) {
        return FilmographyResponse.FilmographyItem.builder()
                .id(filmography.getId())
                .title(filmography.getTitle())
                .description(filmography.getDescription())
                .releasedAt(filmography.getReleasedAt())
                .mediaUrl(filmography.getMediaUrl())
                .createdAt(filmography.getCreatedAt())
                .updatedAt(filmography.getUpdatedAt())
                .build();
    }
}