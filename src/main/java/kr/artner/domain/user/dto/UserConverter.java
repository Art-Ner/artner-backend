package kr.artner.domain.user.dto;

import kr.artner.domain.user.Role;
import kr.artner.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    
    public User toEntity(UserRequest.JoinDTO request) {
        return User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .phone(request.getPhone())
                .profileImageUrl(request.getProfileImageUrl())
                .oauthProvider(request.getOauthProvider())
                .role(Role.USER)
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
        return UserResponse.DetailInfoDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    public UserResponse.UpdateResponse toUpdateResponse(User user) {
        return UserResponse.UpdateResponse.builder()
                .username(user.getUsername())
                .phone(user.getPhone())
                .build();
    }
}
