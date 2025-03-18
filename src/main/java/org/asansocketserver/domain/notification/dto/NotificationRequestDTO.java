package org.asansocketserver.domain.notification.dto;

import java.time.LocalDateTime;

public record NotificationRequestDTO(
        Long watchId,
        Long imageId,
        String watchName,
        String watchHost,
        String position,
        String alarmType,
        LocalDateTime timeStamp) {
    public static NotificationRequestDTO of(Long watchId, Long imageId, String watchName, String watchHost,
            String position, String alarmType, LocalDateTime timeStamp) {
        return new NotificationRequestDTO(watchId, imageId, watchName, watchHost, position, alarmType, timeStamp);
    }
}
