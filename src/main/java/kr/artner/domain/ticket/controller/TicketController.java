package kr.artner.domain.ticket.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @PostMapping("/{ticketId}/hold")
    public ResponseEntity<?> holdTicket(@PathVariable Long ticketId) { return ResponseEntity.ok().build(); }

    @PostMapping("/{ticketId}/pay")
    public ResponseEntity<?> payTicket(@PathVariable Long ticketId) { return ResponseEntity.ok().build(); }

    @PatchMapping("/{ticketId}")
    public ResponseEntity<?> cancelTicket(@PathVariable Long ticketId) { return ResponseEntity.ok().build(); }

    @GetMapping("/my")
    public ResponseEntity<?> getMyTickets() { return ResponseEntity.ok().build(); }
}
