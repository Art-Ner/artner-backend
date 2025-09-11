package kr.artner.domain.project.entity;

import jakarta.persistence.*;
import kr.artner.domain.project.enums.CollabStatus;
import kr.artner.domain.user.entity.User;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_collab_requests", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "requester_id"})
}, indexes = {
        @Index(name = "ix_pcr_project_status", columnList = "project_id, status, created_at"),
        @Index(name = "ix_pcr_requester", columnList = "requester_id, created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectCollabRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollabStatus status;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decided_by")
    private User decidedBy;
}
