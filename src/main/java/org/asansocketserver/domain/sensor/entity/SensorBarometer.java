package org.asansocketserver.domain.sensor.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.asansocketserver.domain.sensor.dto.request.BarometerRequestDto;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "sensor_barometer")
public class SensorBarometer {
    @Id
    private String id;
    @Field(name = "date")
    private LocalDate date;
    @Field(name = "watch_id")
    private Long watchId;
    @Field(name = "value")
    private Float value;
    @Field(name = "timestamp")
    private Long timestamp;

    public static SensorBarometer createSensor(Long watchId, BarometerRequestDto barometerRequestDto) {
        return SensorBarometer.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .value(barometerRequestDto.value())
                .timestamp(barometerRequestDto.timestamp())
                .build();
    }
}
