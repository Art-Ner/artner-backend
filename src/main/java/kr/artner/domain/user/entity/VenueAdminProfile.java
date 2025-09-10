package kr.artner.domain.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "venue_admin_profile")
public class VenueAdminProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Column(name = "business_reg_number", length = 10, nullable = false)
    private String businessRegNumber;

    // ...getter, setter, equals, hashCode, toString...
}
