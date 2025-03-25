package org.assansocketserver.domain.sensor.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "combined_sensor_data")
@Builder
public class CombinedSensorData {

    @Id
    private String id; // MongoDB 문서의 고유 ID

    private Long timestamp;
    private Float accX;
    private Float accY;
    private Float accZ;
    private Float barometerValue;
    private Float gyroX;
    private Float gyroY;
    private Float gyroZ;
    private Integer heartRateValue;
    private Integer lightValue;

}
