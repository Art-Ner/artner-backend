package kr.artner.domain.project.entity;

import kr.artner.domain.artist.entity.ArtistProfile;
import jakarta.persistence.*;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.project.enums.ProjectStatus;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects", indexes = {
        @Index(name = "ix_projects_status", columnList = "status, created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private ArtistProfile owner;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String concept;

    @Column(name = "target_region", length = 100)
    private String targetRegion;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_genre", nullable = false)
    private GenreCode targetGenre;

    @Column(name = "expected_scale", length = 50)
    private String expectedScale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateProject(String title, String concept, String targetRegion, 
                              GenreCode targetGenre, String expectedScale) {
        if (title != null) this.title = title;
        if (concept != null) this.concept = concept;
        if (targetRegion != null) this.targetRegion = targetRegion;
        if (targetGenre != null) this.targetGenre = targetGenre;
        if (expectedScale != null) this.expectedScale = expectedScale;
        this.updatedAt = LocalDateTime.now();
    }
}
