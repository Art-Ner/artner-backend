package kr.artner.domain.performance.service;

import kr.artner.domain.performance.entity.Performance;
import kr.artner.domain.performance.entity.Ticket;
import kr.artner.domain.performance.repository.PerformanceRepository;
import kr.artner.domain.ticket.dto.TicketConverter;
import kr.artner.domain.ticket.dto.TicketRequest;
import kr.artner.domain.ticket.dto.TicketResponse;
import kr.artner.domain.ticket.enums.TicketStatus;
import kr.artner.domain.ticket.repository.TicketRepository;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceService {
    
    private final PerformanceRepository performanceRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketResponse.PerformanceTicketsResponse getPerformanceTickets(
            Long performanceId,
            String status,
            Integer page,
            Integer size,
            String sort
    ) {
        // 공연 존재 확인
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        // 기본값 설정
        page = page != null ? page : 0;
        size = size != null ? size : 20;
        
        // 정렬 설정
        Sort sortOption = Sort.by(Sort.Direction.DESC, "purchasedAt");
        if (sort != null) {
            String[] sortParts = sort.split(",");
            if (sortParts.length == 2) {
                Sort.Direction direction = "asc".equalsIgnoreCase(sortParts[1]) ? 
                    Sort.Direction.ASC : Sort.Direction.DESC;
                sortOption = Sort.by(direction, sortParts[0]);
            }
        }
        
        Pageable pageable = PageRequest.of(page, size, sortOption);
        
        // 상태 필터 적용
        Page<Ticket> ticketPage;
        if (status != null) {
            try {
                TicketStatus ticketStatus = TicketStatus.valueOf(status.toUpperCase());
                ticketPage = ticketRepository.findByPerformanceAndStatusOrderByPurchasedAtDesc(
                        performance, ticketStatus, pageable);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 티켓 상태입니다: " + status);
            }
        } else {
            ticketPage = ticketRepository.findByPerformanceOrderByPurchasedAtDesc(performance, pageable);
        }
        
        // DTO 변환
        List<TicketResponse.TicketItem> tickets = ticketPage.getContent()
                .stream()
                .map(TicketConverter::toTicketItem)
                .collect(Collectors.toList());
        
        // 페이지 정보 구성
        TicketResponse.PageInfo pageInfo = TicketResponse.PageInfo.builder()
                .totalCount(ticketPage.getTotalElements())
                .page(ticketPage.getNumber())
                .size(ticketPage.getSize())
                .hasMore(!ticketPage.isLast())
                .build();
        
        return TicketResponse.PerformanceTicketsResponse.builder()
                .tickets(tickets)
                .pageInfo(pageInfo)
                .build();
    }

    @Transactional
    public TicketResponse.CreateReservationResponse createTicketReservation(
            Long performanceId, TicketRequest.CreateReservationRequest request) {
        
        // 공연 존재 확인
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        // 구매자 존재 확인
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 가격 검증
        if (request.getPrice() < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }

        // 티켓 생성
        Ticket ticket = Ticket.builder()
                .performance(performance)
                .buyer(buyer)
                .price(request.getPrice())
                .status(TicketStatus.RESERVED)
                .purchasedAt(LocalDateTime.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketConverter.toCreateReservationResponse(savedTicket);
    }
}
