package kr.artner.domain.chatting.entity;

import jakarta.persistence.*;
import kr.artner.domain.user.entity.User;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_low_id", "user_high_id"})
}, indexes = {
        @Index(name = "ix_conv_users", columnList = "user_low_id, user_high_id"),
        @Index(name = "ix_conv_type_ct", columnList = "created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_low_id", nullable = false)
    private User userLow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_high_id", nullable = false)
    private User userHigh;

    @Column(name = "archived_by_low", nullable = false)
        @Builder.Default
        private boolean archivedByLow = false;

    @Column(name = "archived_by_high", nullable = false)
        @Builder.Default
        private boolean archivedByHigh = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
