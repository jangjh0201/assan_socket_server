package org.assansocketserver.domain.sensor.mongorepository;

import org.assansocketserver.domain.sensor.entity.sensorType.*;

public interface SensorCustomRepository {
    void updateAccelerometer(final Long watchId, final Accelerometer accelerometer);

    void updateBarometer(final Long watchId, final Barometer barometer);

    void updateGyroscope(final Long watchId, final Gyroscope gyroscope);

    void updateHeartRate(final Long watchId, final HeartRate heartRate);

    void updateLight(final Long watchId, final Light light);
}
