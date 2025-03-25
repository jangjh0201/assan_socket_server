package org.assansocketserver.batch.cdc.repository;

import org.assansocketserver.batch.cdc.entity.SensorRow;
import org.assansocketserver.domain.watch.entity.Watch;

public interface SensorDataRepositoryCustom {
    void updateAccelerometerData(Watch watch, SensorRow sensorRow);
    void updateBarometerData(Watch watch, SensorRow sensorRow);
    void updateGyroscopeData(Watch watch, SensorRow sensorRow);
    void updateHeartRateData(Watch watch, SensorRow sensorRow);
    void updateLightData(Watch watch, SensorRow sensorRow);
}
