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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateConversationResponse {
        private Long id;
        private UserResponse.GetUserInfoResponse otherUser;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendMessageResponse {
        private Long id;
        private Long conversationId;
        private UserResponse.GetUserInfoResponse sender;
        private String body;
        private LocalDateTime createdAt;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetConversationsResponse {
        private java.util.List<ConversationItem> conversations;
        private kr.artner.response.PageInfo pageInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationItem {
        private Long id;
        private UserResponse.GetUserInfoResponse otherUser;
        private GetMessageResponse lastMessage;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMessagesResponse {
        private java.util.List<GetMessageResponse> messages;
        private kr.artner.response.PageInfo pageInfo;
    }
}
