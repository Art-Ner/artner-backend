package kr.artner.domain.performance.controller;

import jakarta.validation.Valid;
import kr.artner.domain.performance.service.PerformanceService;
import kr.artner.domain.ticket.dto.TicketRequest;
import kr.artner.domain.ticket.dto.TicketResponse;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @GetMapping("/{performanceId}")
    public ResponseEntity<?> getPerformanceDetail(@PathVariable Long performanceId) {
        // TODO: 공연 상세 조회
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getPerformances(@RequestParam(value = "keyword", required = false) String keyword) {
        // TODO: 공연 목록 조회
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{performanceId}")
    public ResponseEntity<?> deletePerformance(@PathVariable Long performanceId) {
        // TODO: 공연 삭제
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{performanceId}")
    public ResponseEntity<?> publishPerformance(@PathVariable Long performanceId) {
        // TODO: 공연 최종 게시
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{performanceId}")
    public ResponseEntity<?> updatePerformance(@PathVariable Long performanceId) {
        // TODO: 등록된 공연 수정
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new")
    public ResponseEntity<?> createPerformanceDraft() {
        // TODO: 공연 최초 임시 저장
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{performanceId}/tickets")
    public ApiResponse<Map<String, Object>> getPerformanceTickets(
            @PathVariable Long performanceId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        TicketResponse.PerformanceTicketsResponse response = performanceService.getPerformanceTickets(
                performanceId, status, page, size, sort
        );
        
        return ApiResponse.success(
                "예매 현황을 불러왔습니다.",
                Map.of("tickets", response.getTickets(),
                       "page_info", response.getPageInfo())
        );
    }

    @PostMapping("/{performanceId}/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse.CreateReservationResponse createTicketReservation(
            @PathVariable Long performanceId,
            @RequestBody @Valid TicketRequest.CreateReservationRequest request
    ) {
        return performanceService.createTicketReservation(performanceId, request);
    }
}
