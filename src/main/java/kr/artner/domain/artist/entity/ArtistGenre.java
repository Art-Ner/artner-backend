package kr.artner.domain.user.entity;

import jakarta.persistence.*;
import kr.artner.domain.common.enums.GenreCode;

@Entity
@Table(name = "artist_genre")
public class ArtistGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre_code", nullable = false)
    private GenreCode genreCode;

    // ...getter, setter, equals, hashCode, toString...
}
