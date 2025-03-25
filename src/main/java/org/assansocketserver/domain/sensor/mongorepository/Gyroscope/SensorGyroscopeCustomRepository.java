package org.assansocketserver.domain.sensor.mongorepository.Gyroscope;

import java.util.List;

import org.assansocketserver.domain.sensor.entity.SensorGyroscope;

public interface SensorGyroscopeCustomRepository {
    void deleteAllGyroscopes(List<String> sensorGyroscopeIdList);
}
