package kr.artner.domain.performance.entity;

import jakarta.persistence.*;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.venue.entity.Venue;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "performances", indexes = {
        @Index(name = "ix_performances_time", columnList = "start_dt, end_dt")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
