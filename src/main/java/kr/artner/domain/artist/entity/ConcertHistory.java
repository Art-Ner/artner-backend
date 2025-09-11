package kr.artner.domain.artist.entity;

import jakarta.persistence.*;
import kr.artner.domain.artist.enums.RoleCode;
import kr.artner.domain.user.entity.User;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "concert_history", indexes = {
        @Index(name = "ix_credits_user", columnList = "user_id, started_on")
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
