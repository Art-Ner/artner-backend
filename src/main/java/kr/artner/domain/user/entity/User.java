package kr.artner.domain.user.entity;

import jakarta.persistence.*;
import kr.artner.domain.common.BaseRDBEntity;
import kr.artner.domain.common.enums.OAuthProvider;
import kr.artner.domain.common.enums.UserRole;
import lombok.*;

@Entity
@Table(name = "users")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Builder
    private User(String email, String username, String phone, String profileImageUrl, OAuthProvider oauthProvider, UserRole role) {
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.profileImageUrl = profileImageUrl;
        this.oauthProvider = oauthProvider;
        this.role = role;
    }

    public void updateProfile(String username, String phone) {
        if (username != null) this.username = username;
        if (phone != null) this.phone = phone;
    }

    public void updateProfileImage(String profileImageUrl) {
        if (profileImageUrl != null) this.profileImageUrl = profileImageUrl;
    }

    public boolean isAdmin() {
        return this.role.isAdmin();
    }
}
