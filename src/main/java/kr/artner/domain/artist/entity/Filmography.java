package kr.artner.domain.artist.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "filmography", indexes = {
        @Index(name = "ix_filmography_artist_profile", columnList = "artist_profile_id, created_at")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Filmography {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_profile_id", nullable = false)
    private ArtistProfile artistProfile;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "released_at", nullable = false)
    private LocalDate releasedAt;

    @Column(name = "media_url", length = 255)
    private String mediaUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @org.hibernate.annotations.UpdateTimestamp
    private LocalDateTime updatedAt;
}
