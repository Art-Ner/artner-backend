package kr.artner.domain.venue.controller;

import jakarta.validation.Valid;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.venue.dto.BookingRequest;
import kr.artner.domain.venue.dto.BookingResponse;
import kr.artner.domain.venue.service.BookingService;
import kr.artner.global.auth.CustomUserDetails;
import kr.artner.global.auth.LoginMember;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, Object>> createBooking(
            @LoginMember CustomUserDetails userDetails,
            @RequestBody @Valid BookingRequest.CreateBookingRequest request
    ) {
        BookingResponse.CreateBookingResponse response = bookingService.createBooking(request, userDetails.getUser().getId());
        
        return ApiResponse.success(
                "대관 요청이 접수되었습니다.",
                Map.of("booking", response)
        );
    }

    @GetMapping
    public ApiResponse<BookingResponse.BookingListResponse> getBookings(
            @RequestParam(value = "venueId", required = false) Long venueId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        BookingResponse.BookingListResponse response = bookingService.getBookings(venueId, status, limit, offset);
        
        return ApiResponse.success("대관 요청 목록을 불러왔습니다.", response);
    }

    @GetMapping("/{bookingId}")
    public ApiResponse<Map<String, Object>> getBookingDetail(@PathVariable Long bookingId) {
        BookingResponse.BookingDetailResponse response = bookingService.getBookingDetail(bookingId);
        
        return ApiResponse.success(
                "대관 요청 상세를 불러왔습니다.",
                Map.of("booking", response)
        );
    }

    @PatchMapping("/{bookingId}/approve")
    public ApiResponse<Map<String, Object>> approveBooking(
            @PathVariable Long bookingId,
            @LoginMember CustomUserDetails userDetails
    ) {
        BookingResponse.BookingApprovalResponse response = bookingService.approveBooking(bookingId, userDetails.getUser().getId());
        
        return ApiResponse.success(
                "대관 요청이 승인되었습니다.",
                Map.of("booking", response)
        );
    }

    @PatchMapping("/{bookingId}/reject")
    public ApiResponse<Map<String, Object>> rejectBooking(
            @PathVariable Long bookingId,
            @LoginMember CustomUserDetails userDetails
    ) {
        BookingResponse.BookingRejectionResponse response = bookingService.rejectBooking(bookingId, userDetails.getUser().getId());
        
        return ApiResponse.success(
                "대관 요청이 거절되었습니다.",
                Map.of("booking", response)
        );
    }

    @PatchMapping("/{bookingId}/cancel")
    public ApiResponse<Map<String, Object>> cancelBooking(
            @PathVariable Long bookingId,
            @LoginMember CustomUserDetails userDetails
    ) {
        BookingResponse.BookingCancellationResponse response = bookingService.cancelBooking(bookingId, userDetails.getUser().getId());
        
        return ApiResponse.success(
                "대관이 취소되었습니다.",
                Map.of("booking", response)
        );
    }
}
