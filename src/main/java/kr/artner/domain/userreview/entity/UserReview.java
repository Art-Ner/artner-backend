package kr.artner.domain.userreview.entity;

import jakarta.persistence.*;
import kr.artner.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "target_user_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "privateBuilder")
public class UserReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;

    @Column(length = 500, nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateContent(String content) {
        this.content = content;
    }

    public static UserReviewBuilder builder() {
        return new UserReviewBuilder();
    }

    public static class UserReviewBuilder {
        private User user;
        private User targetUser;
        private String content;

        public UserReviewBuilder user(User user) {
            this.user = user;
            return this;
        }

        public UserReviewBuilder targetUser(User targetUser) {
            this.targetUser = targetUser;
            return this;
        }

        public UserReviewBuilder content(String content) {
            this.content = content;
            return this;
        }

        public UserReview build() {
            if (user != null && targetUser != null && user.getId().equals(targetUser.getId())) {
                throw new IllegalArgumentException("자기 자신에게는 리뷰를 작성할 수 없습니다.");
            }
            return UserReview.privateBuilder()
                    .user(user)
                    .targetUser(targetUser)
                    .content(content)
                    .build();
        }
    }
}
