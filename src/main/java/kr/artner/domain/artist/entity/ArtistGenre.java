package kr.artner.domain.artist.entity;

import jakarta.persistence.*;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artist_genre")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistGenre {

    @EmbeddedId
    private ArtistGenreId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // Maps the userId from the embedded id
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // @Enumerated(EnumType.STRING)
    // @Column(name = "genre_code", nullable = false)
    // private GenreCode genreCode;

    // Getters and setters (Lombok will generate them with @Getter, @Setter if added)
    // For now, I'll just add them manually for clarity if needed, or rely on Lombok.
    public ArtistGenreId getId() {
        return id;
    }

    public void setId(ArtistGenreId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Transient
    public GenreCode getGenreCode() {
        return id != null ? id.getGenreCode() : null;
    }

    // public void setGenreCode(GenreCode genreCode) {
    //     this.genreCode = genreCode;
    // }
}
