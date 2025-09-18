package kr.artner.domain.venue.entity;

import jakarta.persistence.*;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.venue.enums.BookingStatus;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "ix_bookings_venue", columnList = "venue_id, start_dt, end_dt")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "start_dt", nullable = false)
    private LocalDateTime startDt;

    @Column(name = "end_dt", nullable = false)
    private LocalDateTime endDt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void approve() {
        if (this.status != BookingStatus.REQUESTED) {
            throw new IllegalStateException("REQUESTED 상태에서만 승인할 수 있습니다.");
        }
        this.status = BookingStatus.APPROVED;
        this.decidedAt = LocalDateTime.now();
    }

    public void reject() {
        if (this.status != BookingStatus.REQUESTED) {
            throw new IllegalStateException("REQUESTED 상태에서만 거절할 수 있습니다.");
        }
        this.status = BookingStatus.REJECTED;
        this.decidedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 요청입니다.");
        }
        if (this.status != BookingStatus.REQUESTED) {
            throw new IllegalStateException("REQUESTED 상태에서만 취소할 수 있습니다.");
        }
        this.status = BookingStatus.CANCELLED;
        this.decidedAt = LocalDateTime.now();
    }
}
