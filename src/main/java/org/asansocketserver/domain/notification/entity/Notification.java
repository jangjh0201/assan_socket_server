package org.asansocketserver.domain.notification.entity;

import lombok.*;

import org.asansocketserver.domain.notification.dto.NotificationRequestDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "notification")
public class Notification {

    @Id
    private String id;
    private String riskname;
    private Long patientId;
    private String patientName;
    private String patientRoom;
    private String currentSectorName;
    private LocalDateTime timestamp;

    public static Notification createNotification(NotificationRequestDTO notificationRequestDto) {
        return Notification.builder()
                .riskname(notificationRequestDto.riskName())
                .patientId(notificationRequestDto.patientId())
                .patientName(notificationRequestDto.patientName())
                .patientRoom(notificationRequestDto.patientRoomName())
                .currentSectorName(notificationRequestDto.currentSectorName())
                .timestamp(notificationRequestDto.timeStamp())
                .build();
    }
}
