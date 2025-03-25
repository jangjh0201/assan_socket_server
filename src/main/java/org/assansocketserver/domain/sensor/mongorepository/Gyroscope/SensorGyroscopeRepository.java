package org.assansocketserver.domain.sensor.mongorepository.Gyroscope;

import org.assansocketserver.domain.sensor.entity.SensorAccelerometer;
import org.assansocketserver.domain.sensor.entity.SensorGyroscope;
import org.assansocketserver.domain.sensor.mongorepository.SensorCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface SensorGyroscopeRepository extends MongoRepository<SensorGyroscope, String>, SensorGyroscopeCustomRepository {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);

    List<SensorGyroscope> findAllByWatchIdAndDate(Long watchId, LocalDate date);

    List<SensorGyroscope> findAllByWatchIdAndDateBetween(int patientId, LocalDate localDate, LocalDate localDate1);

    SensorGyroscope findTopByWatchIdAndDateOrderByTimestampDesc(Long id, LocalDate now);
}
