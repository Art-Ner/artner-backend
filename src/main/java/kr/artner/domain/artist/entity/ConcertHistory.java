package kr.artner.domain.artist.entity;

import jakarta.persistence.*;
import kr.artner.domain.artist.enums.RoleCode;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "concert_history", indexes = {
        @Index(name = "ix_concert_history_artist_profile", columnList = "artist_profile_id, started_on")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ConcertHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_profile_id", nullable = false)
    private ArtistProfile artistProfile;

    @Column(name = "work_title", length = 200, nullable = false)
    private String workTitle;

    @ElementCollection(targetClass = RoleCode.class)
    @CollectionTable(name = "concert_history_roles", joinColumns = @JoinColumn(name = "concert_history_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role_code", nullable = false)
    private List<RoleCode> roleCodes;

    @Column(name = "started_on")
    private LocalDate startedOn;

    @Column(name = "ended_on")
    private LocalDate endedOn;

    @Column(name = "proof_url", length = 255)
    private String proofUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @org.hibernate.annotations.UpdateTimestamp
    private LocalDateTime updatedAt;
}
