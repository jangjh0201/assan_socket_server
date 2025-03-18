package org.asansocketserver.domain.notification.dto;

import org.asansocketserver.domain.notification.entity.Notification;

import java.time.LocalDateTime;

public record NotificationResponseDTO(
        Long watchId,
        Long imageId,
        String watchName,
        String watchHost,
        String position,
        String alarmType,
        LocalDateTime timestamp
) {
    public static NotificationResponseDTO fromEntity(Notification notification) {
        return new NotificationResponseDTO(
                notification.getWatchId(),
                notification.getImageId(),
                notification.getWatchName(),
                notification.getWatchHost(),
                notification.getPosition(),
                notification.getAlarmType(),
                notification.getTimestamp()
        );
    }
}
