package org.asan.domain.sensor.mongorepository.Gyroscope;

import java.util.List;

import org.asan.domain.sensor.entity.SensorGyroscope;

public interface SensorGyroscopeCustomRepository {
    void deleteAllGyroscopes(List<String> sensorGyroscopeIdList);
}
