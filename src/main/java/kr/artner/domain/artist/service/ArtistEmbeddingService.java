package kr.artner.domain.artist.service;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.entity.ArtistGenre;
import kr.artner.domain.artist.entity.ArtistRole;
import kr.artner.domain.artist.repository.ArtistGenreRepository;
import kr.artner.domain.artist.repository.ArtistRoleRepository;
import kr.artner.global.service.OpenAIEmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistEmbeddingService {

    private final OpenAIEmbeddingService openAIEmbeddingService;
    private final ArtistGenreRepository artistGenreRepository;
    private final ArtistRoleRepository artistRoleRepository;

    public String generateArtistEmbedding(ArtistProfile artistProfile) {
        try {
            // 아티스트의 장르와 역할 정보 조회
            List<String> genres = artistGenreRepository.findByArtistProfile(artistProfile)
                    .stream()
                    .map(genre -> genre.getId().getGenreCode().name())
                    .collect(Collectors.toList());

            List<String> roles = artistRoleRepository.findByArtistProfile(artistProfile)
                    .stream()
                    .map(role -> role.getId().getRoleCode().name())
                    .collect(Collectors.toList());

            return openAIEmbeddingService.generateArtistEmbedding(
                    artistProfile.getHeadline(),
                    artistProfile.getBio(),
                    genres,
                    roles
            );
        } catch (Exception e) {
            log.error("Error generating artist embedding for profile ID: {}", artistProfile.getId(), e);
            return null;
        }
    }

    public String generateUserPreferenceEmbedding(ArtistProfile artistProfile) {
        String embedding = generateArtistEmbedding(artistProfile);
        if (embedding != null) {
            log.info("Generated user preference embedding for artist profile ID: {}", artistProfile.getId());
        } else {
            log.warn("Failed to generate user preference embedding for artist profile ID: {}", artistProfile.getId());
        }
        return embedding;
    }
}