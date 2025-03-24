package org.asan.domain.sensor.mongorepository.heartrate;

import lombok.RequiredArgsConstructor;

import org.asan.domain.sensor.entity.SensorHeartRate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class SensorHeartRateCustomRepositoryImpl implements SensorHeartRateCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteAllHeartRates(List<String> sensorHeatRateIdList) {
        Query query = new Query(Criteria.where("_id").in(sensorHeatRateIdList));
        mongoTemplate.remove(query, SensorHeartRate.class);
    }
}
