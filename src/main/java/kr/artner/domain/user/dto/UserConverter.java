package kr.artner.domain.user.dto;

import kr.artner.global.auth.oauth.enums.OAuthProvider;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.enums.UserRole;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.venue.repository.VenueAdminProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final ArtistProfileRepository artistProfileRepository;
    private final VenueAdminProfileRepository venueAdminProfileRepository;

    public User toEntity(UserRequest.JoinDTO request) {
        return User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .phone(request.getPhone())
                .profileImageUrl(request.getProfileImageUrl())
                .oauthProvider(request.getOauthProvider())
                .role(UserRole.USER)
                .build();
    }

    public UserResponse.JoinResponse toJoinResponse(User user) {
        return UserResponse.JoinResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .joinDate(user.getCreatedAt())
                .build();
    }

    public UserResponse.DetailInfoDTO toDetailInfo(User user) {
        Long artistId = artistProfileRepository.findByUser(user)
                .map(profile -> profile.getId())
                .orElse(null);

        Long venueAdminId = venueAdminProfileRepository.findByUser(user)
                .map(profile -> profile.getId())
                .orElse(null);

        return UserResponse.DetailInfoDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .profileImageUrl(user.getProfileImageUrl())
                .nickname(user.getNickname())
                .artistId(artistId)
                .venueAdminId(venueAdminId)
                .build();
    }

    public UserResponse.UpdateResponse toUpdateResponse(User user) {
        return UserResponse.UpdateResponse.builder()
                .username(user.getUsername())
                .phone(user.getPhone())
                .build();
    }

    public UserResponse.GetUserInfoResponse toGetUserInfoResponse(User user) {
        Long artistId = artistProfileRepository.findByUser(user)
                .map(profile -> profile.getId())
                .orElse(null);

        Long venueAdminId = venueAdminProfileRepository.findByUser(user)
                .map(profile -> profile.getId())
                .orElse(null);

        return UserResponse.GetUserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .artistId(artistId)
                .venueAdminId(venueAdminId)
                .build();
    }
}
