package kr.artner.domain.region.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.artner.domain.region.entity.RegionDistrictId;

@Entity
@Table(name = "region_district")
@IdClass(RegionDistrictId.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RegionDistrict {
    @Id
    @Column(name = "province_code", length = 4)
    private String provinceCode;

    @Id
    @Column(length = 4)
    private String code;

    @Column(length = 50, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code", insertable = false, updatable = false)
    private RegionProvince province;
}