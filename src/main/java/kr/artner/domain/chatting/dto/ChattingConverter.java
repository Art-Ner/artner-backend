package kr.artner.domain.chatting.dto;

import kr.artner.domain.chatting.entity.Conversation;
import kr.artner.domain.chatting.entity.Message;
import kr.artner.domain.user.dto.UserConverter;

public class ChattingConverter {

    public static ChattingResponse.GetConversationResponse toGetConversationResponse(Conversation conversation) {
        return ChattingResponse.GetConversationResponse.builder()
                .id(conversation.getId())
                .userLow(UserConverter.toGetUserInfoResponse(conversation.getUserLow()))
                .userHigh(UserConverter.toGetUserInfoResponse(conversation.getUserHigh()))
                .createdAt(conversation.getCreatedAt())
                .build();
    }

    public static ChattingResponse.GetMessageResponse toGetMessageResponse(Message message) {
        return ChattingResponse.GetMessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .sender(UserConverter.toGetUserInfoResponse(message.getSender()))
                .body(message.getBody())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
