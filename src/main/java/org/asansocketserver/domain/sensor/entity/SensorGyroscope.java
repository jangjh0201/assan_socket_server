package org.asansocketserver.domain.sensor.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.asansocketserver.domain.sensor.dto.request.GyroscopeRequestDto;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "sensor_gyroscope")
public class SensorGyroscope {
    @Id
    private String id;
    @Field(name = "date")
    private LocalDate date;
    @Field(name = "watch_id")
    private Long watchId;
    @Field(name = "gyroX")
    private Float gyroX;
    @Field(name = "gyroY")
    private Float gyroY;
    @Field(name = "gyroZ")
    private Float gyroZ;
    @Field(name = "timestamp")
    private Long timestamp;

    public static SensorGyroscope createSensor(Long watchId, GyroscopeRequestDto gyroscopeRequestDto) {
        return SensorGyroscope.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .gyroX(gyroscopeRequestDto.gyroX())
                .gyroY(gyroscopeRequestDto.gyroY())
                .gyroZ(gyroscopeRequestDto.gyroZ())
                .timestamp(gyroscopeRequestDto.timestamp())
                .build();
    }
}
