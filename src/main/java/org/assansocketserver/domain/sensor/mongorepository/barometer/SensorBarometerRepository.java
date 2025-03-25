package org.assansocketserver.domain.sensor.mongorepository.barometer;

import org.assansocketserver.domain.sensor.entity.SensorBarometer;
import org.assansocketserver.domain.sensor.mongorepository.SensorCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface SensorBarometerRepository extends MongoRepository<SensorBarometer, String>, SensorBarometerCustomRepository {
    List<SensorBarometer> findAllByWatchIdAndDate(Long watchId, LocalDate date);

    List<SensorBarometer> findAllByWatchIdAndDateBetween(int patientId, LocalDate localDate, LocalDate localDate1);

    SensorBarometer findTopByWatchIdAndDateOrderByTimestampDesc(Long id, LocalDate now);
}
