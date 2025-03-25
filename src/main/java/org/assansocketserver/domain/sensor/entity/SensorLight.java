package org.assansocketserver.domain.sensor.entity;

import jakarta.persistence.Id;
import lombok.*;

import org.assansocketserver.domain.sensor.dto.request.LightRequestDto;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "sensor_light")
public class SensorLight {
    @Id
    private String id;
    @Field(name = "date")
    private LocalDate date;
    @Field(name = "watch_id")
    private Long watchId;
    @Field(name = "value")
    private Integer value;
    @Field(name = "timestamp")
    private Long timestamp;

    public static SensorLight createSensor(Long watchId, LightRequestDto lightRequestDto) {
        return SensorLight.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .value(lightRequestDto.value())
                .timestamp(lightRequestDto.timestamp())
                .build();
    }
}
