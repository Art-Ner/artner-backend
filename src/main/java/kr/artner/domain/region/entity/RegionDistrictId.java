package kr.artner.domain.region.entity;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class RegionDistrictId implements Serializable {
    private String provinceCode;
    private String code;
}