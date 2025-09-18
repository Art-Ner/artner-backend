package kr.artner.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NotificationResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetNotificationResponse {
        private Long id;
        private String kind;
        private String title;
        private String body;
        private boolean isRead;
        private LocalDateTime createdAt;
    }
}
