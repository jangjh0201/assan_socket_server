package org.asan.domain.sensor.repository;

import org.asan.domain.sensor.entity.SensorRow;
import org.asan.domain.watch.entity.Watch;

public interface SensorDataRepositoryCustom {
    void updateAccelerometerData(Watch watch, SensorRow sensorRow);

    void updateBarometerData(Watch watch, SensorRow sensorRow);

    void updateGyroscopeData(Watch watch, SensorRow sensorRow);

    void updateHeartRateData(Watch watch, SensorRow sensorRow);

    void updateLightData(Watch watch, SensorRow sensorRow);
}
