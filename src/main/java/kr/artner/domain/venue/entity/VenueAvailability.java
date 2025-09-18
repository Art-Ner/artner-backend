package kr.artner.domain.venue.entity;

import jakarta.persistence.*;
import kr.artner.domain.venue.enums.AvailabilityKind;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "venue_availability", indexes = {
        @Index(name = "ix_va_venue_time", columnList = "venue_id, start_dt, end_dt")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VenueAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(name = "is_blocked", nullable = false)
    @Builder.Default
    private Boolean isBlocked = false;

    @Column(name = "start_dt", nullable = false)
    private LocalDateTime startDt;

    @Column(name = "end_dt", nullable = false)
    private LocalDateTime endDt;

    @Column(length = 255)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateAvailability(LocalDateTime startDt, LocalDateTime endDt, String note) {
        if (startDt != null) {
            this.startDt = startDt;
        }
        if (endDt != null) {
            this.endDt = endDt;
        }
        if (note != null) {
            this.note = note;
        }
        this.updatedAt = LocalDateTime.now();
    }
}
