package org.assansocketserver.batch.cdc.service;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.assansocketserver.batch.cdc.entity.SensorData;
import org.assansocketserver.batch.cdc.entity.SensorRow;
import org.assansocketserver.batch.cdc.repository.SensorDataRepository;
import org.assansocketserver.domain.sensor.entity.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorDataService {

    private final MongoTemplate mongoTemplate;
    private final SensorDataRepository sensorDataRepository;
    private final long TIMESTAMP_TOLERANCE_MS = 50;

    public SensorDataService(MongoTemplate mongoTemplate, SensorDataRepository sensorDataRepository) {
        this.mongoTemplate = mongoTemplate;
        this.sensorDataRepository = sensorDataRepository;
    }

    public SensorData processSensorDataForWatch(SensorData selectedSensorData) {
        LocalDate today = LocalDate.now();
        // System.out.println("today = " + today);
        List<SensorRow> sensorRowList = new ArrayList<>();

        // Acceleration Data 기준으로 SensorRow 생성
        List<SensorAccelerometer> accelerometerDataList = findAllAccelerometerDataForWatch(selectedSensorData, today);
        // System.out.println("accelerometerDataList = " + accelerometerDataList);
        for (SensorAccelerometer accelerometerData : accelerometerDataList) {
            // System.out.println("accelerometerData.getId() = " +
            // accelerometerData.getId());
            long timestamp = accelerometerData.getTimestamp();

            SensorBarometer barometer = getSensorEntityWithinTolerance(SensorBarometer.class, timestamp);
            SensorGyroscope gyroscope = getSensorEntityWithinTolerance(SensorGyroscope.class, timestamp);
            SensorHeartRate heartRate = getSensorEntityWithinTolerance(SensorHeartRate.class, timestamp);
            SensorLight light = getSensorEntityWithinTolerance(SensorLight.class, timestamp);

            Float barometerValue = barometer != null ? barometer.getValue() : null;
            Float gyroX = gyroscope != null ? gyroscope.getGyroX() : null;
            Float gyroY = gyroscope != null ? gyroscope.getGyroY() : null;
            Float gyroZ = gyroscope != null ? gyroscope.getGyroZ() : null;
            Integer heartRateValue = heartRate != null ? heartRate.getValue() : null;
            Integer lightValue = light != null ? light.getValue() : null;

            SensorRow sensorRow = SensorRow.of(
                    String.valueOf(timestamp),
                    accelerometerData.getAccX(),
                    accelerometerData.getAccY(),
                    accelerometerData.getAccZ(),
                    barometerValue,
                    gyroX,
                    gyroY,
                    gyroZ,
                    heartRateValue,
                    lightValue);

            sensorRowList.add(sensorRow);

            // 각 센서 데이터 엔티티 삭제
            deleteSensorData(SensorAccelerometer.class, accelerometerData.getId());
            if (barometer != null)
                deleteSensorData(SensorBarometer.class, barometer.getId());
            if (gyroscope != null)
                deleteSensorData(SensorGyroscope.class, gyroscope.getId());
            if (heartRate != null)
                deleteSensorData(SensorHeartRate.class, heartRate.getId());
            if (light != null)
                deleteSensorData(SensorLight.class, light.getId());
        }

        SensorData sensorData = sensorDataRepository.findByWatchIdAndDate(selectedSensorData.getWatchId(), today)
                .orElse(null);

        if (sensorData != null) {
            sensorData.updateDate(today);
            sensorData.getSensorRowList().addAll(sensorRowList);
        }
        return sensorData;
    }

    private <T> void deleteSensorData(Class<T> entityClass, Object id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, entityClass);
    }

    private List<SensorAccelerometer> findAllAccelerometerDataForWatch(SensorData sensorData, LocalDate date) {
        return mongoTemplate.find(
                Query.query(Criteria.where("watchId").is(sensorData.getWatchId()).and("date").is(date)),
                SensorAccelerometer.class);
    }

    private <T> T getSensorEntityWithinTolerance(Class<T> entityClass, long timestamp) {
        long startTime = timestamp - TIMESTAMP_TOLERANCE_MS;
        long endTime = timestamp + TIMESTAMP_TOLERANCE_MS;

        Query query = new Query(
                Criteria.where("timestamp").gte(startTime).lte(endTime));

        return mongoTemplate.findOne(query, entityClass);
    }

    public void deleteExpiredSensorData() {
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = today.minusDays(3);

        Query query = new Query(Criteria.where("date").lt(expiryDate));
        mongoTemplate.remove(query, SensorData.class);

    }
}
