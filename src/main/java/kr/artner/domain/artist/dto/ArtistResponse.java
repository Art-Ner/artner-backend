package kr.artner.domain.artist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ArtistResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetArtistProfileResponse {
        private Long id;
        private String artistName;
        private String profileImageUrl;
        private String headline;
        private String bio;
        private List<String> urls;
        private List<String> genres;
        private List<String> roles;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateArtistProfileResponse {
        private Long id;
        private String artistName;
        private String headline;
        private String bio;
        private List<String> urls;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistListItem {
        private Long id;
        private String artistName;
        private String profileImageUrl;
        private String headline;
        private String bio;
        private List<String> urls;
        private List<String> genres;
        private List<String> roles;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistListResponse {
        private List<ArtistListItem> artists;
        private kr.artner.response.PageInfo pageInfo;
    }
}
