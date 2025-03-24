package org.asan.domain.sensor.mongorepository.light;

import org.asan.domain.sensor.entity.SensorLight;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface SensorLightRepository extends MongoRepository<SensorLight, String>, SensorLightCustomRepository {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);

    List<SensorLight> findAllByWatchIdAndDate(Long watchId, LocalDate date);

    List<SensorLight> findAllByWatchIdAndDateBetween(int patientId, LocalDate localDate, LocalDate localDate1);

    SensorLight findTopByWatchIdAndDateOrderByTimestampDesc(Long id, LocalDate now);
}