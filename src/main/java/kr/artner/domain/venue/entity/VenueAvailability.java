package kr.artner.domain.venue.entity;

import jakarta.persistence.*;
import kr.artner.domain.common.enums.AvailabilityKind;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityKind kind;

    @Column(name = "start_dt", nullable = false)
    private LocalDateTime startDt;

    @Column(name = "end_dt", nullable = false)
    private LocalDateTime endDt;

    @Column(length = 255)
    private String note;
}
