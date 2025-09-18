package kr.artner.domain.region.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "region_province")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RegionProvince {
    @Id
    @Column(length = 4)
    private String code;

    @Column(length = 50, nullable = false)
    private String name;
}