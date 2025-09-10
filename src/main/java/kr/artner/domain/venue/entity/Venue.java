package kr.artner.domain.venue.entity;

import jakarta.persistence.*;
import kr.artner.domain.user.entity.User;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "venues", indexes = {
        @Index(name = "ix_venues_region", columnList = "region"),
        @Index(name = "ix_venues_kopis", columnList = "kopis_venue_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id")
    private User adminUser;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String region;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(name = "image_url", columnDefinition = "TEXT", nullable = false)
    private String imageUrl;

    @Column(name = "seat_capacity", nullable = false)
    private Integer seatCapacity;

    @Column(name = "base_rental_fee", nullable = false)
    private Integer baseRentalFee;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "kopis_venue_id", length = 64)
    private String kopisVenueId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
