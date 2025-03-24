package org.asan.domain.sensor.mongorepository.accelerometer;

import java.util.List;

public interface SensorAccelerometerCustomRepository {
    void deleteAllAccelerometers(List<String> sensorAccelerometerIdList);
}
