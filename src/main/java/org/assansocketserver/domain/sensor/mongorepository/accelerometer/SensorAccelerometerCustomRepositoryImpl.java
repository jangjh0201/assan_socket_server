package org.assansocketserver.domain.sensor.mongorepository.accelerometer;

import lombok.RequiredArgsConstructor;

import org.assansocketserver.domain.sensor.entity.SensorAccelerometer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class SensorAccelerometerCustomRepositoryImpl implements SensorAccelerometerCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteAllAccelerometers(List<String> sensorAccelerometerIdList) {
        Query query = new Query(Criteria.where("_id").in(sensorAccelerometerIdList));
        mongoTemplate.remove(query, SensorAccelerometer.class);
    }
}