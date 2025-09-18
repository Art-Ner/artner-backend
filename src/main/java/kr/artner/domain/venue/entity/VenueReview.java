package kr.artner.domain.venue.entity;

import jakarta.persistence.*;
import kr.artner.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venue_reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "venue_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "privateBuilder")
public class VenueReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rate;

    @Column(length = 500, nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateReview(BigDecimal rate, String content) {
        validateRate(rate);
        this.rate = rate;
        this.content = content;
    }

    private void validateRate(BigDecimal rate) {
        if (rate == null || rate.compareTo(new BigDecimal("1.0")) < 0 || rate.compareTo(new BigDecimal("5.0")) > 0) {
            throw new IllegalArgumentException("평점은 1.0에서 5.0 사이여야 합니다.");
        }
    }

    public static VenueReviewBuilder builder() {
        return new VenueReviewBuilder();
    }

    public static class VenueReviewBuilder {
        private User user;
        private Venue venue;
        private BigDecimal rate;
        private String content;

        public VenueReviewBuilder user(User user) {
            this.user = user;
            return this;
        }

        public VenueReviewBuilder venue(Venue venue) {
            this.venue = venue;
            return this;
        }

        public VenueReviewBuilder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public VenueReviewBuilder content(String content) {
            this.content = content;
            return this;
        }

        public VenueReview build() {
            if (rate == null || rate.compareTo(new BigDecimal("1.0")) < 0 || rate.compareTo(new BigDecimal("5.0")) > 0) {
                throw new IllegalArgumentException("평점은 1.0에서 5.0 사이여야 합니다.");
            }
            return VenueReview.privateBuilder()
                    .user(user)
                    .venue(venue)
                    .rate(rate)
                    .content(content)
                    .build();
        }
    }
}
