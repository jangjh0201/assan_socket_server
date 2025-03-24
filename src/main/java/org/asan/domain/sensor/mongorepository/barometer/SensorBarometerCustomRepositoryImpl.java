package org.asan.domain.sensor.mongorepository.barometer;

import lombok.RequiredArgsConstructor;

import org.asan.domain.sensor.entity.SensorBarometer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class SensorBarometerCustomRepositoryImpl implements SensorBarometerCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteAllBarometers(List<String> sensorBarometerIdList) {
        Query query = new Query(Criteria.where("_id").in(sensorBarometerIdList));
        mongoTemplate.remove(query, SensorBarometer.class);
    }
}
