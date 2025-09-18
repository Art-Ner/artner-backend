package kr.artner.domain.chatting.controller;

import jakarta.validation.Valid;
import kr.artner.domain.chatting.dto.ChattingRequest;
import kr.artner.domain.chatting.dto.ChattingResponse;
import kr.artner.domain.chatting.service.ChattingService;
import kr.artner.domain.user.entity.User;
import kr.artner.global.auth.LoginMember;
import kr.artner.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ChattingService chattingService;

    @PostMapping
    public ApiResponse<ChattingResponse.CreateConversationResponse> createConversation(
            @LoginMember User user,
            @RequestBody @Valid ChattingRequest.CreateConversation request
    ) {
        ChattingResponse.CreateConversationResponse response = chattingService.createConversation(user, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    public ApiResponse<ChattingResponse.GetConversationsResponse> getMyConversations(
            @LoginMember User user,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        ChattingResponse.GetConversationsResponse response = chattingService.getMyConversations(user, page, size);
        return ApiResponse.success(response);
    }

    @GetMapping("/{conversationId}/messages")
    public ApiResponse<ChattingResponse.GetMessagesResponse> getMessages(
            @LoginMember User user,
            @PathVariable Long conversationId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size
    ) {
        ChattingResponse.GetMessagesResponse response = chattingService.getMessages(user, conversationId, page, size);
        return ApiResponse.success(response);
    }


    @PostMapping("/{conversationId}/leave")
    public ApiResponse<Void> leaveConversation(
            @LoginMember User user,
            @PathVariable Long conversationId
    ) {
        chattingService.leaveConversation(user, conversationId);
        return ApiResponse.success(null);
    }

    @MessageMapping("/conversation/{conversationId}/send")
    public void sendMessageViaWebSocket(
            @DestinationVariable Long conversationId,
            @Payload ChattingRequest.SendMessage request,
            java.security.Principal principal
    ) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
            if (auth.getPrincipal() instanceof User) {
                User user = (User) auth.getPrincipal();
                chattingService.sendMessage(user, conversationId, request);
            }
        }
    }
}
