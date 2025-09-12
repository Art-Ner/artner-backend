package kr.artner.domain.notification.dto;

import kr.artner.domain.notification.entity.Notification;

public class NotificationConverter {

    public static NotificationResponse.GetNotificationResponse toGetNotificationResponse(Notification notification) {
        return NotificationResponse.GetNotificationResponse.builder()
                .id(notification.getId())
                .kind(notification.getKind())
                .title(notification.getTitle())
                .body(notification.getBody())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
