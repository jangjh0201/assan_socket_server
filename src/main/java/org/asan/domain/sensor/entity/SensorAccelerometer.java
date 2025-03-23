package org.asan.domain.sensor.entity;

import jakarta.persistence.Id;
import lombok.*;

import org.asan.domain.sensor.dto.AccelerometerDTO;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "sensor_accelerometer")
public class SensorAccelerometer {
    @Id
    private String id;
    @Field(name = "date")
    private LocalDate date;
    @Field(name = "watch_id")
    private Long watchId;
    @Field(name = "timestamp")
    private Long timestamp;
    @Field(name = "accX")
    private Float accX;
    @Field(name = "accY")
    private Float accY;
    @Field(name = "accZ")
    private Float accZ;

    public static SensorAccelerometer createSensor(Long watchId, AccelerometerDTO request) {
        return SensorAccelerometer.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .timestamp(request.timestamp())
                .accX(request.accX())
                .accY(request.accY())
                .accZ(request.accZ())
                .build();
    }
}
