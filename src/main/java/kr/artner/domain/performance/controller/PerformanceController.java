package kr.artner.domain.performance.controller;

import jakarta.validation.Valid;
import kr.artner.domain.performance.dto.PerformanceRequest;
import kr.artner.domain.performance.dto.PerformanceResponse;
import kr.artner.domain.performance.service.PerformanceService;
import kr.artner.domain.user.entity.User;
import kr.artner.global.auth.CustomUserDetails;
import kr.artner.global.auth.LoginMember;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, Object>> createPerformance(
            @LoginMember CustomUserDetails userDetails,
            @RequestBody @Valid PerformanceRequest.CreatePerformanceRequest request
    ) {
        User user = userDetails.getUser();
        PerformanceResponse.CreatePerformanceResponse response = performanceService.createPerformance(request, user.getId());
        
        return ApiResponse.success(
                "공연이 생성되었습니다.",
                Map.of("performance", response)
        );
    }

    @GetMapping("/{performanceId}")
    public ApiResponse<Map<String, Object>> getPerformanceDetail(@PathVariable Long performanceId) {
        PerformanceResponse.PerformanceDetailResponse response = performanceService.getPerformanceDetail(performanceId);
        
        return ApiResponse.success(
                "공연 상세를 불러왔습니다.",
                Map.of("performance", response)
        );
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> getPerformances(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "genreCode", required = false) String genreCode,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "venueId", required = false) Long venueId,
            @RequestParam(value = "startDtFrom", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDtFrom,
            @RequestParam(value = "startDtTo", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDtTo,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        PerformanceResponse.PerformanceListResponse response = performanceService.getPerformances(
                keyword, genreCode, region, projectId, venueId, 
                startDtFrom, startDtTo, page, size, sort
        );
        
        return ApiResponse.success(
                "공연 목록을 조회했습니다.",
                Map.of("performances", response.getPerformances(),
                       "page_info", response.getPageInfo())
        );
    }

    @DeleteMapping("/{performanceId}")
    public ApiResponse<Void> deletePerformance(
            @PathVariable Long performanceId,
            @LoginMember CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        performanceService.deletePerformance(performanceId, user.getId());
        
        return ApiResponse.success("공연이 삭제되었습니다.", null);
    }

    @PatchMapping("/{performanceId}/publish")
    public ApiResponse<Map<String, Object>> publishPerformance(
            @PathVariable Long performanceId,
            @LoginMember CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        PerformanceResponse.PublishPerformanceResponse response = performanceService.publishPerformance(performanceId, user.getId());
        
        return ApiResponse.success(
                "공연이 게시되었습니다.",
                Map.of("performance", response)
        );
    }

    @PatchMapping("/{performanceId}")
    public ApiResponse<Map<String, Object>> updatePerformance(
            @PathVariable Long performanceId,
            @LoginMember CustomUserDetails userDetails,
            @RequestBody @Valid PerformanceRequest.UpdatePerformanceRequest request
    ) {
        User user = userDetails.getUser();
        PerformanceResponse.UpdatePerformanceResponse response = performanceService.updatePerformance(performanceId, request, user.getId());
        
        return ApiResponse.success(
                "공연이 수정되었습니다.",
                Map.of("performance", response)
        );
    }

    @PostMapping("/new")
    public ResponseEntity<?> createPerformanceDraft() {
        // TODO: 공연 최초 임시 저장
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{performanceId}/tickets")
    public ResponseEntity<?> getPerformanceTickets(@PathVariable Long performanceId) {
        // TODO: 예매 현황 조회
        return ResponseEntity.ok().build();
    }
}
