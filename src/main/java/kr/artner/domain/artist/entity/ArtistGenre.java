package kr.artner.domain.artist.entity;

import jakarta.persistence.*;
import kr.artner.domain.common.enums.GenreCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "artist_genre")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ArtistGenre {

    @EmbeddedId
    private ArtistGenreId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistProfileId") // Maps the artistProfileId from the embedded id
    @JoinColumn(name = "artist_profile_id", nullable = false)
    private ArtistProfile artistProfile;

    // @Enumerated(EnumType.STRING)
    // @Column(name = "genre_code", nullable = false)
    // private GenreCode genreCode;

    @Transient
    public GenreCode getGenreCode() {
        return id != null ? id.getGenreCode() : null;
    }

    // public void setGenreCode(GenreCode genreCode) {
    //     this.genreCode = genreCode;
    // }
}
