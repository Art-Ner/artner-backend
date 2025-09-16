package kr.artner.domain.artist.controller;

import kr.artner.domain.project.dto.ProjectResponse;
import kr.artner.domain.artist.dto.ArtistProjectsResponse;
import kr.artner.domain.artist.service.ArtistService;
import kr.artner.domain.artist.service.FilmographyService;
import kr.artner.domain.artist.service.ConcertHistoryService;
import kr.artner.domain.artist.dto.FilmographyRequest;
import kr.artner.domain.artist.dto.FilmographyResponse;
import kr.artner.domain.artist.dto.ConcertHistoryRequest;
import kr.artner.domain.artist.dto.ConcertHistoryResponse;
import jakarta.validation.Valid;
import kr.artner.domain.user.entity.User;
import kr.artner.global.auth.LoginMember;
import kr.artner.response.ApiResponse;
import kr.artner.response.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final FilmographyService filmographyService;
    private final ConcertHistoryService concertHistoryService;

    @GetMapping("/me/projects")
    public ApiResponse<ArtistProjectsResponse> getMyProjects(
            @LoginMember User user,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "isOwner", defaultValue = "true") boolean isOwner
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<ProjectResponse.GetProjectResponse> projectPage = artistService.getMyProjects(user, isOwner, pageable);

        PageInfo pageInfo = PageInfo.builder()
                .totalCount(projectPage.getTotalElements())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .hasMore(projectPage.hasNext())
                .build();

        ArtistProjectsResponse.ProjectsResult projectsResult = ArtistProjectsResponse.ProjectsResult.builder()
                .projects(projectPage.getContent())
                .build();

        ArtistProjectsResponse response = ArtistProjectsResponse.builder()
                .result(projectsResult)
                .pageInfo(pageInfo)
                .build();

        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<kr.artner.domain.artist.dto.ArtistResponse.ArtistListResponse> getArtists(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "role", required = false) String role
    ) {
        kr.artner.domain.artist.dto.ArtistResponse.ArtistListResponse response =
                artistService.getArtists(keyword, limit, offset, genre, role);
        return ApiResponse.success(response);
    }

    @GetMapping("/{artistId}/profile")
    public ApiResponse<kr.artner.domain.artist.dto.ArtistResponse.GetArtistProfileResponse> getArtistProfile(@PathVariable Long artistId) {
        kr.artner.domain.artist.dto.ArtistResponse.GetArtistProfileResponse response = artistService.getArtistProfile(artistId);
        return ApiResponse.success(response);
    }

    @PutMapping("/me/profile")
    public ResponseEntity<?> updateMyArtistProfile() {
        // TODO: 아티스트 프로필 수정
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/filmography")
    public ApiResponse<FilmographyResponse.CreateFilmographyResponse> uploadFilmography(
            @LoginMember User user,
            @Valid @RequestBody FilmographyRequest.CreateFilmography request
    ) {
        FilmographyResponse.CreateFilmographyResponse response = filmographyService.createFilmography(user, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/{artistId}/filmography")
    public ApiResponse<FilmographyResponse.FilmographyListResponse> getFilmographyList(
            @PathVariable Long artistId,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset
    ) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        FilmographyResponse.FilmographyListResponse response = filmographyService.getFilmographyList(artistId, pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/{artistId}/filmography/{filmographyId}")
    public ApiResponse<FilmographyResponse.FilmographyItem> getFilmographyDetail(
            @PathVariable Long artistId,
            @PathVariable Long filmographyId
    ) {
        FilmographyResponse.FilmographyItem response = filmographyService.getFilmographyDetail(artistId, filmographyId);
        return ApiResponse.success(response);
    }

    @PutMapping("/me/filmography/{filmographyId}")
    public ApiResponse<FilmographyResponse.FilmographyItem> updateFilmography(
            @LoginMember User user,
            @PathVariable Long filmographyId,
            @Valid @RequestBody FilmographyRequest.UpdateFilmography request
    ) {
        FilmographyResponse.FilmographyItem response = filmographyService.updateFilmography(user, filmographyId, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/me/filmography/{filmographyId}")
    public ApiResponse<String> deleteFilmography(
            @LoginMember User user,
            @PathVariable Long filmographyId
    ) {
        filmographyService.deleteFilmography(user, filmographyId);
        return ApiResponse.success("필모그래피가 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/me/concert-history")
    public ApiResponse<ConcertHistoryResponse.CreateConcertHistoryResponse> uploadConcertHistory(
            @LoginMember User user,
            @Valid @RequestBody ConcertHistoryRequest.CreateConcertHistory request
    ) {
        ConcertHistoryResponse.CreateConcertHistoryResponse response = concertHistoryService.createConcertHistory(user, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/{artistId}/concert-history")
    public ApiResponse<ConcertHistoryResponse.ConcertHistoryListResponse> getConcertHistoryList(
            @PathVariable Long artistId,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset
    ) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        ConcertHistoryResponse.ConcertHistoryListResponse response = concertHistoryService.getConcertHistoryList(artistId, pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/{artistId}/concert-history/{historyId}")
    public ApiResponse<ConcertHistoryResponse.ConcertHistoryItem> getConcertHistoryDetail(
            @PathVariable Long artistId,
            @PathVariable Long historyId
    ) {
        ConcertHistoryResponse.ConcertHistoryItem response = concertHistoryService.getConcertHistoryDetail(artistId, historyId);
        return ApiResponse.success(response);
    }

    @PutMapping("/me/concert-history/{historyId}")
    public ApiResponse<ConcertHistoryResponse.ConcertHistoryItem> updateConcertHistory(
            @LoginMember User user,
            @PathVariable Long historyId,
            @Valid @RequestBody ConcertHistoryRequest.UpdateConcertHistory request
    ) {
        ConcertHistoryResponse.ConcertHistoryItem response = concertHistoryService.updateConcertHistory(user, historyId, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/me/concert-history/{historyId}")
    public ApiResponse<String> deleteConcertHistory(
            @LoginMember User user,
            @PathVariable Long historyId
    ) {
        concertHistoryService.deleteConcertHistory(user, historyId);
        return ApiResponse.success("공연 이력이 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/{artistProfileId}/reviews")
    public ResponseEntity<?> getArtistReviews(
            @PathVariable Long artistProfileId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        // TODO: 아티스트 프로필의 유저간 리뷰 목록 조회
        return ResponseEntity.ok().build();
    }
}
