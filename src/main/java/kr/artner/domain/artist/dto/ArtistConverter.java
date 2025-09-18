package kr.artner.domain.artist.dto;

import kr.artner.domain.artist.entity.ArtistProfile;

public class ArtistConverter {

    public static ArtistResponse.GetArtistProfileResponse toGetArtistProfileResponse(ArtistProfile artistProfile) {
        return ArtistResponse.GetArtistProfileResponse.builder()
                .id(artistProfile.getId())
                .artistName(artistProfile.getArtistName())
                .profileImageUrl(artistProfile.getProfileImageUrl())
                .headline(artistProfile.getHeadline())
                .bio(artistProfile.getBio())
                .urls(artistProfile.getUrls())
                .build();
    }
}
