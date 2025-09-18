package kr.artner.domain.venue.dto;

import kr.artner.domain.project.entity.Project;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.venue.entity.Booking;
import kr.artner.domain.venue.entity.Venue;
import kr.artner.domain.venue.enums.BookingStatus;

import java.time.LocalDateTime;

public class BookingConverter {

    public static Booking toEntity(BookingRequest.CreateBookingRequest request, User requestedBy, Venue venue, Project project) {
        return Booking.builder()
                .requestedBy(requestedBy)
                .venue(venue)
                .project(project)
                .startDt(request.getStartDt())
                .endDt(request.getEndDt())
                .status(BookingStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BookingResponse.CreateBookingResponse toCreateBookingResponse(Booking booking) {
        return BookingResponse.CreateBookingResponse.builder()
                .id(booking.getId())
                .requestedBy(booking.getRequestedBy().getId())
                .venueId(booking.getVenue().getId())
                .projectId(booking.getProject().getId())
                .startDt(booking.getStartDt())
                .endDt(booking.getEndDt())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    public static BookingResponse.BookingItem toBookingItem(Booking booking) {
        return BookingResponse.BookingItem.builder()
                .id(booking.getId())
                .venueId(booking.getVenue().getId())
                .projectId(booking.getProject().getId())
                .requestedBy(booking.getRequestedBy().getId())
                .status(booking.getStatus().name())
                .startDt(booking.getStartDt())
                .endDt(booking.getEndDt())
                .decidedAt(booking.getDecidedAt())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    public static BookingResponse.BookingDetailResponse toBookingDetailResponse(Booking booking) {
        return BookingResponse.BookingDetailResponse.builder()
                .id(booking.getId())
                .venueId(booking.getVenue().getId())
                .projectId(booking.getProject().getId())
                .requestedBy(booking.getRequestedBy().getId())
                .status(booking.getStatus().name())
                .startDt(booking.getStartDt())
                .endDt(booking.getEndDt())
                .decidedAt(booking.getDecidedAt())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    public static BookingResponse.BookingApprovalResponse toBookingApprovalResponse(Booking booking) {
        return BookingResponse.BookingApprovalResponse.builder()
                .id(booking.getId())
                .venueId(booking.getVenue().getId())
                .projectId(booking.getProject().getId())
                .requestedBy(booking.getRequestedBy().getId())
                .status(booking.getStatus().name())
                .startDt(booking.getStartDt())
                .endDt(booking.getEndDt())
                .decidedAt(booking.getDecidedAt())
                .build();
    }

    public static BookingResponse.BookingRejectionResponse toBookingRejectionResponse(Booking booking) {
        return BookingResponse.BookingRejectionResponse.builder()
                .id(booking.getId())
                .venueId(booking.getVenue().getId())
                .projectId(booking.getProject().getId())
                .requestedBy(booking.getRequestedBy().getId())
                .status(booking.getStatus().name())
                .startDt(booking.getStartDt())
                .endDt(booking.getEndDt())
                .decidedAt(booking.getDecidedAt())
                .build();
    }

    public static BookingResponse.BookingCancellationResponse toBookingCancellationResponse(Booking booking) {
        return BookingResponse.BookingCancellationResponse.builder()
                .id(booking.getId())
                .venueId(booking.getVenue().getId())
                .projectId(booking.getProject().getId())
                .requestedBy(booking.getRequestedBy().getId())
                .status(booking.getStatus().name())
                .startDt(booking.getStartDt())
                .endDt(booking.getEndDt())
                .decidedAt(booking.getDecidedAt())
                .build();
    }
}