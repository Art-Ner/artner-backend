package kr.artner.domain.user.service;

import kr.artner.domain.user.dto.UserConverter;
import kr.artner.domain.user.dto.UserRequest;
import kr.artner.domain.user.dto.UserResponse;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import kr.artner.global.auth.oauth.enums.OAuthProvider; // Import OAuthProvider
import kr.artner.domain.user.enums.UserRole; // Import UserRole

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Transactional
    public UserResponse.JoinResponse join(UserRequest.JoinDTO request) {
        boolean emailExists = userRepository.existsByEmail(request.getEmail());

        Optional.of(request.getEmail())
                .filter(email -> !emailExists)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_DUPLICATE_BY_EMAIL));
        
        boolean usernameExists = userRepository.existsByUsername(request.getUsername());

        if (usernameExists) {
            throw new GeneralException(ErrorStatus.MEMBER_DUPLICATE_BY_USERNAME);
        }
        User newUser = userConverter.toEntity(request);
        User savedUser = userRepository.save(newUser);
        return userConverter.toJoinResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse.DetailInfoDTO getMyInfo(Long userId) {
        User user = getUserOrThrow(userId);
        return userConverter.toDetailInfo(user);
    }

    @Transactional
    public UserResponse.UpdateResponse updateProfile(Long userId, UserRequest.UpdateProfile request) {
        User user = getUserOrThrow(userId);
        
        String newUsername = request.getUsername().trim();
        
        // 기존 사용자명과 동일한 경우 - 변경 불필요
        if (!user.getUsername().equals(newUsername)) {
            // 사용자명 중복 체크
            if (userRepository.existsByUsername(newUsername)) {
                throw new GeneralException(ErrorStatus.MEMBER_DUPLICATE_BY_USERNAME);
            }
        }
        
        user.updateProfile(newUsername, request.getPhone(), request.getNickname());
        return userConverter.toUpdateResponse(user);
    }

    @Transactional
    public UserResponse.DetailInfoDTO updateProfileImage(Long userId, String profileImageUrl) {
        User user = getUserOrThrow(userId);
        user.updateProfileImage(profileImageUrl);
        return userConverter.toDetailInfo(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserOrThrow(userId);
        userRepository.delete(user);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    @Transactional
    public User findOrCreateUser(String email, String username, OAuthProvider oauthProvider) {
        return userRepository.findByOauthProviderAndEmail(oauthProvider, email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .username(username)
                            .nickname(username) // Default nickname to username
                            .oauthProvider(oauthProvider)
                            .role(UserRole.USER) // Default role to USER
                            .build();
                    return userRepository.save(newUser);
                });
    }
}
