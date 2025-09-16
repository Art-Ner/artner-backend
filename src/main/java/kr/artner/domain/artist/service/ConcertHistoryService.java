package kr.artner.domain.artist.service;

import kr.artner.domain.artist.dto.ConcertHistoryRequest;
import kr.artner.domain.artist.dto.ConcertHistoryResponse;
import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.entity.ConcertHistory;
import kr.artner.domain.artist.enums.RoleCode;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.artist.repository.ConcertHistoryRepository;
import kr.artner.domain.user.entity.User;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import kr.artner.response.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertHistoryService {

    private final ConcertHistoryRepository concertHistoryRepository;
    private final ArtistProfileRepository artistProfileRepository;

    @Transactional
    public ConcertHistoryResponse.CreateConcertHistoryResponse createConcertHistory(User user, ConcertHistoryRequest.CreateConcertHistory request) {
        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        // 역할 코드 변환
        List<RoleCode> roleCodes = new ArrayList<>();
        if (request.getRoleCodes() != null && !request.getRoleCodes().isEmpty()) {
            for (String roleCodeName : request.getRoleCodes()) {
                try {
                    RoleCode roleCode = RoleCode.valueOf(roleCodeName.toUpperCase());
                    roleCodes.add(roleCode);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid role code: {}", roleCodeName);
                }
            }
        }

        ConcertHistory concertHistory = ConcertHistory.builder()
                .artistProfile(artistProfile)
                .workTitle(request.getWorkTitle())
                .roleCodes(roleCodes)
                .startedOn(request.getStartedOn())
                .endedOn(request.getEndedOn())
                .proofUrl(request.getProofUrl())
                .build();

        ConcertHistory savedConcertHistory = concertHistoryRepository.save(concertHistory);

        List<String> roleCodeNames = savedConcertHistory.getRoleCodes().stream()
                .map(RoleCode::name)
                .collect(Collectors.toList());

        return ConcertHistoryResponse.CreateConcertHistoryResponse.builder()
                .id(savedConcertHistory.getId())
                .workTitle(savedConcertHistory.getWorkTitle())
                .roleCodes(roleCodeNames)
                .startedOn(savedConcertHistory.getStartedOn())
                .endedOn(savedConcertHistory.getEndedOn())
                .proofUrl(savedConcertHistory.getProofUrl())
                .message("공연 이력이 성공적으로 등록되었습니다.")
                .build();
    }

    @Transactional(readOnly = true)
    public ConcertHistoryResponse.ConcertHistoryListResponse getConcertHistoryList(Long artistProfileId, Pageable pageable) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        Page<ConcertHistory> concertHistoryPage = concertHistoryRepository.findAllByArtistProfileOrderByStartedOnDesc(artistProfile, pageable);

        List<ConcertHistoryResponse.ConcertHistoryItem> concertHistories = concertHistoryPage.getContent().stream()
                .map(this::convertToConcertHistoryItem)
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.builder()
                .totalCount(concertHistoryPage.getTotalElements())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .hasMore(concertHistoryPage.hasNext())
                .build();

        return ConcertHistoryResponse.ConcertHistoryListResponse.builder()
                .concertHistories(concertHistories)
                .pageInfo(pageInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public ConcertHistoryResponse.ConcertHistoryItem getConcertHistoryDetail(Long artistProfileId, Long historyId) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        ConcertHistory concertHistory = concertHistoryRepository.findByIdAndArtistProfile(historyId, artistProfile)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONCERT_HISTORY_NOT_FOUND));

        return convertToConcertHistoryItem(concertHistory);
    }

    @Transactional
    public ConcertHistoryResponse.ConcertHistoryItem updateConcertHistory(User user, Long historyId, ConcertHistoryRequest.UpdateConcertHistory request) {
        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        ConcertHistory concertHistory = concertHistoryRepository.findByIdAndArtistProfile(historyId, artistProfile)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONCERT_HISTORY_NOT_FOUND));

        // 역할 코드 변환
        List<RoleCode> roleCodes = new ArrayList<>();
        if (request.getRoleCodes() != null && !request.getRoleCodes().isEmpty()) {
            for (String roleCodeName : request.getRoleCodes()) {
                try {
                    RoleCode roleCode = RoleCode.valueOf(roleCodeName.toUpperCase());
                    roleCodes.add(roleCode);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid role code: {}", roleCodeName);
                }
            }
        }

        ConcertHistory updatedConcertHistory = ConcertHistory.builder()
                .id(concertHistory.getId())
                .artistProfile(concertHistory.getArtistProfile())
                .workTitle(request.getWorkTitle())
                .roleCodes(roleCodes)
                .startedOn(request.getStartedOn())
                .endedOn(request.getEndedOn())
                .proofUrl(request.getProofUrl())
                .createdAt(concertHistory.getCreatedAt())
                .build();

        ConcertHistory savedConcertHistory = concertHistoryRepository.save(updatedConcertHistory);
        return convertToConcertHistoryItem(savedConcertHistory);
    }

    @Transactional
    public void deleteConcertHistory(User user, Long historyId) {
        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTIST_PROFILE_NOT_FOUND));

        ConcertHistory concertHistory = concertHistoryRepository.findByIdAndArtistProfile(historyId, artistProfile)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONCERT_HISTORY_NOT_FOUND));

        concertHistoryRepository.delete(concertHistory);
    }

    private ConcertHistoryResponse.ConcertHistoryItem convertToConcertHistoryItem(ConcertHistory concertHistory) {
        List<String> roleCodeNames = concertHistory.getRoleCodes().stream()
                .map(RoleCode::name)
                .collect(Collectors.toList());

        return ConcertHistoryResponse.ConcertHistoryItem.builder()
                .id(concertHistory.getId())
                .workTitle(concertHistory.getWorkTitle())
                .roleCodes(roleCodeNames)
                .startedOn(concertHistory.getStartedOn())
                .endedOn(concertHistory.getEndedOn())
                .proofUrl(concertHistory.getProofUrl())
                .createdAt(concertHistory.getCreatedAt())
                .updatedAt(concertHistory.getUpdatedAt())
                .build();
    }
}