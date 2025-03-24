package org.asan.domain.sensor.mongorepository.barometer;

import java.util.List;

public interface SensorBarometerCustomRepository {
    void deleteAllBarometers(List<String> sensorBarometerIdList);
}
