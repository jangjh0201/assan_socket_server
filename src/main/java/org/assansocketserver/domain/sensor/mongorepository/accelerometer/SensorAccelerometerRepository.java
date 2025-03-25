package org.assansocketserver.domain.sensor.mongorepository.accelerometer;

import org.assansocketserver.domain.sensor.entity.SensorAccelerometer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface SensorAccelerometerRepository extends MongoRepository<SensorAccelerometer, String>, SensorAccelerometerCustomRepository {
    List<SensorAccelerometer> findAllByWatchIdAndDate(Long watchId, LocalDate date);


    List<SensorAccelerometer> findAllByWatchIdAndDateBetween(int patientId, LocalDate localDate, LocalDate localDate1);

    SensorAccelerometer findTopByWatchIdAndDateOrderByTimestampDesc(Long id, LocalDate now);
}
