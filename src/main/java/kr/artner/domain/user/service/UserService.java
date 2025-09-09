package kr.artner.domain.user.service;

import kr.artner.domain.user.User;
import kr.artner.domain.user.dto.UserConverter;
import kr.artner.domain.user.dto.UserRequest;
import kr.artner.domain.user.dto.UserResponse;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Transactional
    public UserResponse.JoinResponse join(UserRequest.JoinDTO request) {
        log.info("회원가입 시작: email={}, username={}", request.getEmail(), request.getUsername());
        
        boolean emailExists = userRepository.existsByEmail(request.getEmail());
        log.info("이메일 존재 여부 체크: email={}, exists={}", request.getEmail(), emailExists);
        
        Optional.of(request.getEmail())
                .filter(email -> !emailExists)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_DUPLICATE_BY_EMAIL));
        
        boolean usernameExists = userRepository.existsByUsername(request.getUsername());
        log.info("사용자명 존재 여부 체크: username={}, exists={}", request.getUsername(), usernameExists);
        
        if (usernameExists) {
            throw new GeneralException(ErrorStatus.MEMBER_DUPLICATE_BY_USERNAME);
        }

        User newUser = userConverter.toEntity(request);
        log.info("Entity 생성 완료: {}", newUser);
        
        User savedUser = userRepository.save(newUser);
        log.info("DB 저장 완료: id={}, email={}", savedUser.getId(), savedUser.getEmail());
        
        UserResponse.JoinResponse response = userConverter.toJoinResponse(savedUser);
        log.info("응답 생성 완료: {}", response);
        
        return response;
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
        
        user.updateProfile(newUsername, request.getPhone());
        return userConverter.toUpdateResponse(user);
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
}
