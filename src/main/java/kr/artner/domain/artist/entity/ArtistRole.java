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
}
