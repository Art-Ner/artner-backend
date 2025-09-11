package kr.artner.domain.artist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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

    @Column(name = "role_code")
    private String roleCode; // Stored as String in DB for ENUM
}
