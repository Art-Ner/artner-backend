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

import kr.artner.global.auth.oauth.enums.OAuthProvider;
import kr.artner.domain.user.enums.UserRole;
import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.entity.ArtistGenre;
import kr.artner.domain.artist.entity.ArtistGenreId;
import kr.artner.domain.artist.entity.ArtistRole;
import kr.artner.domain.artist.entity.ArtistRoleId;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.artist.repository.ArtistGenreRepository;
import kr.artner.domain.artist.repository.ArtistRoleRepository;
import kr.artner.domain.artist.dto.ArtistRequest;
import kr.artner.domain.artist.dto.ArtistResponse;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.artist.enums.RoleCode;
import kr.artner.domain.venue.dto.VenueRequest;
import kr.artner.domain.venue.dto.VenueResponse;
import kr.artner.domain.venue.service.VenueAdminService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final ArtistProfileRepository artistProfileRepository;
    private final ArtistGenreRepository artistGenreRepository;
    private final ArtistRoleRepository artistRoleRepository;
    private final VenueAdminService venueAdminService;
    private final kr.artner.domain.venue.repository.VenueAdminProfileRepository venueAdminProfileRepository;

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

        Long artistId = artistProfileRepository.findByUser(user)
                .map(profile -> profile.getId())
                .orElse(null);

        Long venueAdminId = venueAdminProfileRepository.findByUser(user)
                .map(profile -> profile.getId())
                .orElse(null);

        return userConverter.toDetailInfo(user, artistId, venueAdminId);
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

        Long artistId = artistProfileRepository.findByUser(user)
                .map(profile -> profile.getId())
                .orElse(null);

        Long venueAdminId = venueAdminProfileRepository.findByUser(user)
                .map(profile -> profile.getId())
                .orElse(null);

        return userConverter.toDetailInfo(user, artistId, venueAdminId);
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

    @Transactional
    public User findOrCreateUser(String email, String username, String nickname, OAuthProvider oauthProvider) {
        return userRepository.findByOauthProviderAndEmail(oauthProvider, email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .username(username)
                            .nickname(nickname)
                            .oauthProvider(oauthProvider)
                            .role(UserRole.USER)
                            .build();
                    return userRepository.save(newUser);
                });
    }

    @Transactional
    public ArtistResponse.CreateArtistProfileResponse createArtistProfile(Long userId, ArtistRequest.CreateArtistProfile request) {
        User user = getUserOrThrow(userId);

        // 중복 생성 허용 - 기존 검증 로직 제거

        // 아티스트 프로필 생성
        ArtistProfile artistProfile = ArtistProfile.builder()
                .user(user)
                .artistName(request.getArtistName())
                .headline(request.getHeadline())
                .bio(request.getBio())
                .urls(request.getUrls())
                .build();

        ArtistProfile savedProfile = artistProfileRepository.save(artistProfile);

        // 장르 저장
        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            List<ArtistGenre> artistGenres = new ArrayList<>();
            for (String genreName : request.getGenres()) {
                try {
                    GenreCode genreCode = GenreCode.valueOf(genreName.toUpperCase());
                    ArtistGenreId genreId = new ArtistGenreId(savedProfile.getId(), genreCode);
                    ArtistGenre artistGenre = ArtistGenre.builder()
                            .id(genreId)
                            .artistProfile(savedProfile)
                            .build();
                    artistGenres.add(artistGenre);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid genre code: {}", genreName);
                }
            }
            artistGenreRepository.saveAll(artistGenres);
        }

        // 역할 저장
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            List<ArtistRole> artistRoles = new ArrayList<>();
            for (String roleName : request.getRoles()) {
                try {
                    RoleCode roleCode = RoleCode.valueOf(roleName.toUpperCase());
                    ArtistRoleId roleId = new ArtistRoleId(savedProfile.getId(), roleCode);
                    ArtistRole artistRole = ArtistRole.builder()
                            .id(roleId)
                            .artistProfile(savedProfile)
                            .build();
                    artistRoles.add(artistRole);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid role code: {}", roleName);
                }
            }
            artistRoleRepository.saveAll(artistRoles);
        }

        // 저장된 장르와 역할 정보 조회
        List<String> savedGenres = artistGenreRepository.findByArtistProfile(savedProfile)
                .stream()
                .map(genre -> genre.getId().getGenreCode().name())
                .toList();

        List<String> savedRoles = artistRoleRepository.findByArtistProfile(savedProfile)
                .stream()
                .map(role -> role.getId().getRoleCode().name())
                .toList();

        ArtistResponse.ArtistProfileDetail artistProfileDetail = ArtistResponse.ArtistProfileDetail.builder()
                .id(savedProfile.getId())
                .user_id(savedProfile.getUser().getId())
                .artistName(savedProfile.getArtistName())
                .headline(savedProfile.getHeadline())
                .bio(savedProfile.getBio())
                .urls(savedProfile.getUrls())
                .genres(savedGenres)
                .roles(savedRoles)
                .build();

        return ArtistResponse.CreateArtistProfileResponse.builder()
                .artistProfile(artistProfileDetail)
                .build();
    }

    @Transactional
    public VenueResponse.CreateVenueAdminProfileResponse createVenueAdminProfile(Long userId, VenueRequest.CreateVenueAdminProfile request) {
        User user = getUserOrThrow(userId);
        return venueAdminService.createVenueAdminProfile(user, request);
    }
}
