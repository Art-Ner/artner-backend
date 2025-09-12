package kr.artner.domain.chatting.dto;

import kr.artner.domain.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ChattingResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetConversationResponse {
        private Long id;
        private UserResponse.GetUserInfoResponse userLow;
        private UserResponse.GetUserInfoResponse userHigh;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMessageResponse {
        private Long id;
        private Long conversationId;
        private UserResponse.GetUserInfoResponse sender;
        private String body;
        private LocalDateTime createdAt;
    }
}
