package kr.artner.domain.user.entity;

import jakarta.persistence.*;
import kr.artner.domain.common.enums.RoleCode;

@Entity
@Table(name = "artist_role")
public class ArtistRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_code", nullable = false)
    private RoleCode roleCode;

    // ...getter, setter, equals, hashCode, toString...
}
