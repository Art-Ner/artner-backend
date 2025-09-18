package kr.artner.domain.venue.entity;

import jakarta.persistence.*;
import kr.artner.domain.user.entity.User;
import lombok.*;

@Entity
@Table(name = "venue_admin_profile")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    @Column(name = "business_name", length = 100, nullable = false)
    private String businessName;

    @Column(length = 15)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String description;
}
