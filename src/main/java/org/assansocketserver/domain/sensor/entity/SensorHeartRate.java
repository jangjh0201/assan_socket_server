package org.assansocketserver.domain.sensor.entity;

import jakarta.persistence.Id;
import lombok.*;

import org.assansocketserver.domain.sensor.dto.request.HeartRateRequestDto;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "sensor_heart_rate")
public class SensorHeartRate {
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

    public static SensorHeartRate createSensor(Long watchId, HeartRateRequestDto heartRateRequestDto) {
        return SensorHeartRate.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .value(heartRateRequestDto.value())
                .timestamp(heartRateRequestDto.timestamp())
                .build();
    }
}
