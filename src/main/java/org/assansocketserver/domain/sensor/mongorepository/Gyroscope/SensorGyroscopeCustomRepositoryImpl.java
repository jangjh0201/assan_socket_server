package org.assansocketserver.domain.sensor.mongorepository.Gyroscope;

import lombok.RequiredArgsConstructor;

import org.assansocketserver.domain.sensor.entity.SensorBarometer;
import org.assansocketserver.domain.sensor.entity.SensorGyroscope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class SensorGyroscopeCustomRepositoryImpl implements SensorGyroscopeCustomRepository{
    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteAllGyroscopes(List<String> sensorGyroscopeIdList) {
        Query query = new Query(Criteria.where("_id").in(sensorGyroscopeIdList));
        mongoTemplate.remove(query, SensorGyroscope.class);
    }
}
