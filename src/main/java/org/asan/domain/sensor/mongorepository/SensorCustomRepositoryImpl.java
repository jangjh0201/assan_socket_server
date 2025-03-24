package org.asan.domain.sensor.mongorepository;

import lombok.RequiredArgsConstructor;

import org.asan.domain.sensor.entity.*;
import org.asan.domain.sensor.entity.sensorType.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDate;

@RequiredArgsConstructor
public class SensorCustomRepositoryImpl implements SensorCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updateAccelerometer(final Long watchId, final Accelerometer accelerometer) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("date").is(LocalDate.now())
                .and("watchId").is(watchId));
        update.addToSet("accelerometerList", accelerometer);
        mongoTemplate.updateFirst(query, update, SensorAccelerometer.class);
    }

    @Override
    public void updateBarometer(final Long watchId, final Barometer barometer) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("date").is(LocalDate.now())
                .and("watchId").is(watchId));
        update.addToSet("barometerList", barometer);
        mongoTemplate.updateFirst(query, update, SensorBarometer.class);
    }

    @Override
    public void updateGyroscope(final Long watchId, final Gyroscope gyroscope) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("date").is(LocalDate.now())
                .and("watchId").is(watchId));
        update.addToSet("gyroscopeList", gyroscope);
        mongoTemplate.updateFirst(query, update, SensorGyroscope.class);
    }

    @Override
    public void updateHeartRate(final Long watchId, final HeartRate heartRate) {
        System.out.println(heartRate.getTimestamp());
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("date").is(LocalDate.now())
                .and("watchId").is(watchId));
        update.addToSet("heartRateList", heartRate);
        mongoTemplate.updateFirst(query, update, SensorHeartRate.class);
    }

    @Override
    public void updateLight(final Long watchId, final Light light) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("date").is(LocalDate.now())
                .and("watchId").is(watchId));
        update.addToSet("lightList", light);
        mongoTemplate.updateFirst(query, update, SensorLight.class);
    }
}