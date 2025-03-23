package org.asan.domain.sensor.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "sensor_data")
public class SensorData {
    @MongoId
    @Field(name = "_id")
    private ObjectId id;

    @Field(name = "date")
    private LocalDate date;

    @Field(name = "watch_id")
    private Long watchId;

    @Field(name = "name")
    private String name;

    @Field(name = "sensorRowList")
    @Builder.Default
    List<SensorRow> sensorRowList = new ArrayList<>();
}
