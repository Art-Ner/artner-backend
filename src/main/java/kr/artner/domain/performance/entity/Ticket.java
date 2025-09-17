package kr.artner.domain.performance.entity;

import jakarta.persistence.*;
import kr.artner.domain.performance.entity.Performance;
import kr.artner.domain.ticket.enums.TicketStatus;
import kr.artner.domain.user.entity.User;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets", indexes = {
        @Index(name = "ix_tickets_buyer_paid", columnList = "buyer_id, purchased_at"),
        @Index(name = "ix_tickets_hold_expiry", columnList = "hold_expires_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TicketStatus status = TicketStatus.RESERVED;

    @Column(name = "hold_expires_at", nullable = false)
    @Builder.Default
    private LocalDateTime holdExpiresAt = LocalDateTime.now().plusMinutes(10);

    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "external_payment_id", length = 120)
    private String externalPaymentId;

    @Column(name = "idempotency_key")
    private UUID idempotencyKey;

    public void processPayment() {
        if (this.status == TicketStatus.PAID) {
            throw new IllegalStateException("이미 결제된 티켓입니다.");
        }
        if (this.status == TicketStatus.CANCELLED) {
            throw new IllegalStateException("취소된 티켓은 결제할 수 없습니다.");
        }
        if (this.status == TicketStatus.REFUNDED) {
            throw new IllegalStateException("환불된 티켓은 결제할 수 없습니다.");
        }
        
        this.status = TicketStatus.PAID;
        this.purchasedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == TicketStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 티켓입니다.");
        }
        if (this.status == TicketStatus.REFUNDED) {
            throw new IllegalStateException("환불된 티켓은 취소할 수 없습니다.");
        }
        
        this.status = TicketStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }
}
