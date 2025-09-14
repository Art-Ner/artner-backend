package kr.artner.domain.artist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.artner.domain.artist.enums.RoleCode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ArtistRoleId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_code")
    private RoleCode roleCode; // Stored as String in DB for ENUM
}
