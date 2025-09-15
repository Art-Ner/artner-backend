package kr.artner.domain.artist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.artner.domain.common.enums.GenreCode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ArtistGenreId implements Serializable {
    @Column(name = "artist_profile_id")
    private Long artistProfileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre_code")
    private GenreCode genreCode;
}
