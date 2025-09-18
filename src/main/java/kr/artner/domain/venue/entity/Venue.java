package kr.artner.domain.venue.entity;

import jakarta.persistence.*;
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
    @JoinColumn(name = "admin_profile_id")
    private VenueAdminProfile adminProfile;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String region;

    @Column(name = "province_code", length = 4)
    private String provinceCode;

    @Column(name = "district_code", length = 4)
    private String districtCode;

    @Column(length = 255)
    private String address;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "seat_capacity")
    private Integer seatCapacity;

    @Column(name = "base_rental_fee")
    private Integer baseRentalFee;

    @Column(name = "facility_type", length = 100)
    private String facilityType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "kopis_venue_id", length = 64)
    private String kopisVenueId;

    @Column(length = 20, nullable = false)
    @Builder.Default
    private String source = "INTERNAL";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
