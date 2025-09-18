package kr.artner.domain.venue.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VenueRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateVenueRequest {
        @NotBlank(message = "공연장명은 필수입니다.")
        @Size(max = 150, message = "공연장명은 150자 이하여야 합니다.")
        private String name;

        @NotBlank(message = "지역은 필수입니다.")
        @Size(max = 100, message = "지역은 100자 이하여야 합니다.")
        private String region;

        @NotBlank(message = "주소는 필수입니다.")
        @Size(max = 255, message = "주소는 255자 이하여야 합니다.")
        private String address;

        @NotBlank(message = "이미지 URL은 필수입니다.")
        private String imageUrl;

        @NotNull(message = "수용인원은 필수입니다.")
        @Min(value = 1, message = "수용인원은 1명 이상이어야 합니다.")
        private Integer seatCapacity;

        @NotNull(message = "기본 대관비는 필수입니다.")
        @Min(value = 0, message = "기본 대관비는 0원 이상이어야 합니다.")
        private Integer baseRentalFee;

        @NotBlank(message = "설명은 필수입니다.")
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateVenueRequest {
        @Size(max = 150, message = "공연장명은 150자 이하여야 합니다.")
        private String name;

        @Size(max = 100, message = "지역은 100자 이하여야 합니다.")
        private String region;

        @Size(max = 255, message = "주소는 255자 이하여야 합니다.")
        private String address;

        private String imageUrl;

        @Min(value = 1, message = "수용인원은 1명 이상이어야 합니다.")
        private Integer seatCapacity;

        @Min(value = 0, message = "기본 대관비는 0원 이상이어야 합니다.")
        private Integer baseRentalFee;

        private String description;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateVenueAdminProfile {
        @NotBlank(message = "사업자등록번호는 필수입니다.")
        @Pattern(regexp = "^\\d{10}$", message = "사업자등록번호는 10자리 숫자여야 합니다.")
        private String businessRegNumber;

        @NotBlank(message = "사업체명은 필수입니다.")
        @Size(max = 100, message = "사업체명은 100자 이하여야 합니다.")
        private String businessName;

        @Size(max = 15, message = "전화번호는 15자 이하여야 합니다.")
        private String phone;

        @Size(max = 1000, message = "설명은 1000자 이하여야 합니다.")
        private String description;

        @Size(max = 255, message = "프로필 이미지 URL은 255자 이하여야 합니다.")
        private String profileImageUrl;
    }
}
