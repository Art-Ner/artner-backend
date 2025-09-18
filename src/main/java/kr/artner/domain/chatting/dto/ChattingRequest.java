package kr.artner.domain.chatting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChattingRequest {

    @Getter
    @NoArgsConstructor
    public static class CreateConversation {
        @NotNull(message = "상대방 ID는 필수입니다.")
        private Long targetUserId;
    }

    @Getter
    @NoArgsConstructor
    public static class SendMessage {
        @NotBlank(message = "메시지 내용은 필수입니다.")
        private String body;
    }
}
