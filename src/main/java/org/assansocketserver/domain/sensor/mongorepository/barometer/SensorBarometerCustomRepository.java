package org.assansocketserver.domain.sensor.mongorepository.barometer;

import java.util.List;

public interface SensorBarometerCustomRepository {
    void deleteAllBarometers(List<String> sensorBarometerIdList);
}
