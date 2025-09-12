package kr.artner.domain.artist.entity;

import jakarta.persistence.*;
import kr.artner.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Entity
@Table(name = "artist_profile")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "artist_name", length = 100, nullable = false)
    private String artistName;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Column(length = 150, nullable = false)
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @ElementCollection
    @CollectionTable(name = "artist_profile_urls", joinColumns = @JoinColumn(name = "artist_profile_id"))
    @Column(name = "url")
    private List<String> urls;
}
