package kr.artner.domain.venue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponse {
    private Long id;
    private String name;
    private String region;
    private String address;
    private String imageUrl;
    private Integer seatCapacity;
    private Integer baseRentalFee;
    private String description;
}
