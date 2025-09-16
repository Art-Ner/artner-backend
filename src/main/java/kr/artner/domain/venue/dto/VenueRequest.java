package kr.artner.domain.venue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VenueRequest {

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
