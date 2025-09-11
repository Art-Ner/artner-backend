package kr.artner.domain.artist.entity;

import jakarta.persistence.*;
import kr.artner.domain.artist.enums.RoleCode;
import kr.artner.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artist_role")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistRole {

    @EmbeddedId
    private ArtistRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // Maps the userId from the embedded id
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @MapsId("roleCode") // Maps the roleCode from the embedded id
    @Column(name = "role_code", nullable = false)
    private RoleCode roleCode;

    // Getters and setters (Lombok will generate them with @Getter, @Setter if added)
    // For now, I'll just add them manually for clarity if needed, or rely on Lombok.
    public ArtistRoleId getId() {
        return id;
    }

    public void setId(ArtistRoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RoleCode getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(RoleCode roleCode) {
        this.roleCode = roleCode;
    }
}
