package org.asansocketserver.domain.sensor.entity;


import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = "sensorsend")
public class SensorSendState {
    @Id
    private Long id;

    public static SensorSendState createSensorSendState(Long watchId) {
        return SensorSendState.builder()
                .id(watchId)
                .build();
    }
}
