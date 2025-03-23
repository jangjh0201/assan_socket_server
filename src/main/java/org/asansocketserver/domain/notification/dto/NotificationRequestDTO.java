package org.asansocketserver.domain.notification.dto;

import java.time.LocalDateTime;

public record NotificationRequestDTO(
        String riskName,
        Long patientId,
        String patientName,
        String patientRoomName,
        String currentSectorName,
        LocalDateTime timeStamp) {
    public static NotificationRequestDTO of(String riskName, Long patientId, String patientName, String patientRoomName,
            String currentSectorName, LocalDateTime timeStamp) {
        return new NotificationRequestDTO(riskName, patientId, patientName, patientRoomName, currentSectorName,
                timeStamp);
    }
}
