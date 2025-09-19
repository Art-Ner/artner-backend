package kr.artner.domain.performance.entity;

import jakarta.persistence.*;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.performance.enums.PerformanceStatus;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.venue.entity.Venue;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "performances")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Performance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private ArtistProfile owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre_code", nullable = false)
    private GenreCode genreCode;

    @Column(name = "running_time")
    private Integer runningTime;

    @Column(name = "poster_url", length = 255)
    private String posterUrl;

    @Column(name = "start_dt", nullable = false)
    private LocalDateTime startDt;

    @Column(name = "end_dt", nullable = false)
    private LocalDateTime endDt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private PerformanceStatus status = PerformanceStatus.DRAFT;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updatePerformance(Project project, Venue venue, String title, String description, 
                                GenreCode genreCode, Integer runningTime, String posterUrl, 
                                LocalDateTime startDt, LocalDateTime endDt) {
        this.project = project;
        this.venue = venue;
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (genreCode != null) this.genreCode = genreCode;
        if (runningTime != null) this.runningTime = runningTime;
        if (posterUrl != null) this.posterUrl = posterUrl;
        if (startDt != null) this.startDt = startDt;
        if (endDt != null) this.endDt = endDt;
        this.updatedAt = LocalDateTime.now();
    }

    public void publish() {
        if (this.status == PerformanceStatus.PUBLISHED) {
            throw new IllegalStateException("이미 게시된 공연입니다.");
        }
        this.status = PerformanceStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void unpublish() {
        if (this.status == PerformanceStatus.DRAFT) {
            throw new IllegalStateException("이미 미게시 상태인 공연입니다.");
        }
        this.status = PerformanceStatus.DRAFT;
        this.publishedAt = null;
        this.updatedAt = LocalDateTime.now();
    }
}
