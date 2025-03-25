package org.assansocketserver.domain.sensor.mongorepository.heartrate;

import org.assansocketserver.domain.sensor.entity.SensorHeartRate;
import org.assansocketserver.domain.sensor.mongorepository.SensorCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface SensorHeartRateRepository extends MongoRepository<SensorHeartRate, String>, SensorHeartRateCustomRepository {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);

    List<SensorHeartRate> findAllByWatchIdAndDate(Long watchId, LocalDate date);

    List<SensorHeartRate> findAllByWatchIdAndDateBetween(int patientId, LocalDate localDate, LocalDate localDate1);

    SensorHeartRate findTopByWatchIdAndDateOrderByTimestampDesc(Long id, LocalDate now);
}
