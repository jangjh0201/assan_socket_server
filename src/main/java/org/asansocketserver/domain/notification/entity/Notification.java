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
    private Long watchId;
    private Long imageId;
    private String watchName;
    private String watchHost;
    private String position;
    private String alarmType;
    private LocalDateTime timestamp;

    public static Notification createNotification(NotificationRequestDTO notificationRequestDto) {
        return Notification.builder()
                .watchId(notificationRequestDto.watchId())
                .imageId(notificationRequestDto.imageId())
                .watchName(notificationRequestDto.watchName())
                .watchHost(notificationRequestDto.watchHost())
                .position(notificationRequestDto.position())
                .alarmType(notificationRequestDto.alarmType())
                .timestamp(notificationRequestDto.timeStamp())
                .build();
    }
}
