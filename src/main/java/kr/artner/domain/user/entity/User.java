package kr.artner.domain.user.entity;

import jakarta.persistence.*;
import kr.artner.global.auth.oauth.enums.OAuthProvider;
import kr.artner.domain.common.BaseRDBEntity;
import kr.artner.domain.user.enums.UserRole;
import kr.artner.domain.artist.entity.ArtistGenre;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"oauth_provider", "email"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseRDBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 190, unique = true)
    private String email;

    @Column(length = 30)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", length = 30)
    private OAuthProvider oauthProvider;

    @Column(name = "profile_image_url", length = 512)
    private String profileImageUrl; // S3 경로

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 100, nullable = false) // Added nickname field
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtistGenre> artistGenres = new ArrayList<>();

    @Builder
    private User(String email, String username, String phone, String profileImageUrl, OAuthProvider oauthProvider, UserRole role, String nickname) { // Added nickname to constructor
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.profileImageUrl = profileImageUrl;
        this.oauthProvider = oauthProvider;
        this.role = role;
        this.nickname = nickname; // Assign nickname
    }

    public void updateProfile(String username, String phone, String nickname) { // Added nickname to updateProfile
        if (username != null) this.username = username;
        if (phone != null) this.phone = phone;
        if (nickname != null) this.nickname = nickname; // Update nickname
    }

    public void updateProfileImage(String profileImageUrl) {
        if (profileImageUrl != null) this.profileImageUrl = profileImageUrl;
    }

    public boolean isAdmin() {
        return this.role.isAdmin();
    }
}
