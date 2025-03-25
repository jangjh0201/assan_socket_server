package org.assansocketserver.domain.sensor.mongorepository.light;

import lombok.RequiredArgsConstructor;

import org.assansocketserver.domain.sensor.entity.SensorHeartRate;
import org.assansocketserver.domain.sensor.entity.SensorLight;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class SensorLightCustomRepositoryImpl implements SensorLightCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteAllLights(List<String> sensorLightIdList) {
        Query query = new Query(Criteria.where("_id").in(sensorLightIdList));
        mongoTemplate.remove(query, SensorLight.class);
    }
}
