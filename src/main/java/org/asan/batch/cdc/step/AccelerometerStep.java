package org.asan.batch.cdc.step;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.asan.batch.cdc.entity.SensorRow;
import org.asan.batch.cdc.repository.SensorDataRepository;
import org.asan.domain.sensor.entity.SensorAccelerometer;
import org.asan.domain.sensor.entity.SensorBarometer;
import org.asan.domain.sensor.entity.SensorGyroscope;
import org.asan.domain.sensor.entity.SensorHeartRate;
import org.asan.domain.sensor.entity.SensorLight;
import org.asan.domain.sensor.mongorepository.Gyroscope.SensorGyroscopeRepository;
import org.asan.domain.sensor.mongorepository.accelerometer.SensorAccelerometerRepository;
import org.asan.domain.sensor.mongorepository.barometer.SensorBarometerRepository;
import org.asan.domain.sensor.mongorepository.heartrate.SensorHeartRateRepository;
import org.asan.domain.sensor.mongorepository.light.SensorLightRepository;
import org.asan.domain.watch.entity.Watch;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;



@Slf4j
@RequiredArgsConstructor
@Component
public class AccelerometerStep {

    private final SensorAccelerometerRepository sensorAccelerometerRepository;
    private final SensorGyroscopeRepository sensorGyroscopeRepository;
    private final SensorBarometerRepository sensorBarometerRepository;
    private final SensorHeartRateRepository sensorHeartRateRepository;
    private final SensorLightRepository sensorLightRepository;
    private final SensorDataRepository sensorDataRepository;

    @Transactional
    public void execute(Watch watch) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        SensorAccelerometer latestAccelerometerData = getLatestAccelerometerData(watch);
        SensorGyroscope latestGyroscopeData = getLatestGyroscopeData(watch);
        SensorBarometer latestBarometerData = getLatestBarometerData(watch);
        SensorHeartRate latestHeartRateData = getLatestHeartRateData(watch);
        SensorLight latestLightData = getLatestLightData(watch);

        Float accX = latestAccelerometerData != null ? latestAccelerometerData.getAccX() : null;
        Float accY = latestAccelerometerData != null ? latestAccelerometerData.getAccY() : null;
        Float accZ = latestAccelerometerData != null ? latestAccelerometerData.getAccZ() : null;

        Float gyroX = latestGyroscopeData != null ? latestGyroscopeData.getGyroX() : null;
        Float gyroY = latestGyroscopeData != null ? latestGyroscopeData.getGyroY() : null;
        Float gyroZ = latestGyroscopeData != null ? latestGyroscopeData.getGyroZ() : null;

        Float barometerValue = latestBarometerData != null ? latestBarometerData.getValue() : null;
        Integer heartRateValue = latestHeartRateData != null ? latestHeartRateData.getValue() : null;
        Integer lightValue = latestLightData != null ? latestLightData.getValue() : null;

        if (accX != null || accY != null || accZ != null ||
                gyroX != null || gyroY != null || gyroZ != null ||
                barometerValue != null || heartRateValue != null || lightValue != null) {

            SensorRow sensorRow = SensorRow.of(
                    now.toString(),
                    accX,
                    accY,
                    accZ,
                    barometerValue,
                    gyroX,
                    gyroY,
                    gyroZ,
                    heartRateValue,
                    lightValue
            );

            // SensorData 객체를 생성하여 SensorRow를 추가합니다.
            sensorDataRepository.updateAccelerometerData(watch, sensorRow);
        }

        if (latestHeartRateData != null) {
            sensorHeartRateRepository.delete(latestHeartRateData);
        }


        // 데이터를 처리한 후 각 컬렉션에서 삭제합니다.
        if (latestAccelerometerData != null) {
            sensorAccelerometerRepository.delete(latestAccelerometerData);
        }

        if (latestGyroscopeData != null) {
            sensorGyroscopeRepository.delete(latestGyroscopeData);
        }

        if (latestBarometerData != null) {
            sensorBarometerRepository.delete(latestBarometerData);
        }


        if (latestLightData != null) {
            sensorLightRepository.delete(latestLightData);
        }
    }

    private SensorAccelerometer getLatestAccelerometerData(Watch watch) {
        return sensorAccelerometerRepository
                .findTopByWatchIdAndDateOrderByTimestampDesc(watch.getId(), LocalDate.now());
    }

    private SensorGyroscope getLatestGyroscopeData(Watch watch) {
        return sensorGyroscopeRepository
                .findTopByWatchIdAndDateOrderByTimestampDesc(watch.getId(), LocalDate.now());
    }

    private SensorBarometer getLatestBarometerData(Watch watch) {
        return sensorBarometerRepository
                .findTopByWatchIdAndDateOrderByTimestampDesc(watch.getId(), LocalDate.now());
    }

    private SensorHeartRate getLatestHeartRateData(Watch watch) {
        return sensorHeartRateRepository
                .findTopByWatchIdAndDateOrderByTimestampDesc(watch.getId(), LocalDate.now());
    }

    private SensorLight getLatestLightData(Watch watch) {
        return sensorLightRepository
                .findTopByWatchIdAndDateOrderByTimestampDesc(watch.getId(), LocalDate.now());
    }
}
