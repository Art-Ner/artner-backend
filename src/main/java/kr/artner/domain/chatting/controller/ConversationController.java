package kr.artner.domain.chatting.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    @PostMapping
    public ResponseEntity<?> createConversation() { return ResponseEntity.ok().build(); }

    @GetMapping
    public ResponseEntity<?> getMyConversations() { return ResponseEntity.ok().build(); }

    @GetMapping("/{conversationId}")
    public ResponseEntity<?> getConversationDetail(@PathVariable Long conversationId) { return ResponseEntity.ok().build(); }

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable Long conversationId) { return ResponseEntity.ok().build(); }

    @PostMapping("/{conversationId}/leave")
    public ResponseEntity<?> leaveConversation(@PathVariable Long conversationId) { return ResponseEntity.ok().build(); }
}
