package kr.artner.domain.artist.entity;

import jakarta.persistence.*;
import kr.artner.domain.artist.enums.RoleCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "artist_role")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ArtistRole {

    @EmbeddedId
    private ArtistRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistProfileId") // Maps the artistProfileId from the embedded id
    @JoinColumn(name = "artist_profile_id", nullable = false)
    private ArtistProfile artistProfile;

    @Transient
    public RoleCode getRoleCode() {
        return id != null ? id.getRoleCode() : null;
    }

    // @Enumerated(EnumType.STRING)
    // @Column(name = "role_code", nullable = false)
    // private RoleCode roleCode;
}
