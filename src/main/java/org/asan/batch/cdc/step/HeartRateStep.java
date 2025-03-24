package org.asan.batch.cdc.step;

import lombok.RequiredArgsConstructor;

import org.asan.batch.cdc.entity.SensorRow;
import org.asan.batch.cdc.repository.SensorDataRepository;
import org.asan.domain.sensor.entity.SensorHeartRate;
import org.asan.domain.sensor.mongorepository.heartrate.SensorHeartRateRepository;
import org.asan.domain.watch.entity.Watch;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class HeartRateStep {
    private final SensorHeartRateRepository sensorHeartRateRepository;
    private final SensorDataRepository sensorDataRepository;

    public void heartRateStep(Watch watch) {
        List<SensorHeartRate> sensorHeartRateList = heartRateReadTask(watch);
        List<SensorRow> sensorRowList = heartRateReadTask(sensorHeartRateList);
        heartRateWriteTask(sensorRowList, watch);
    }

    private List<SensorHeartRate> heartRateReadTask(Watch watch) {
        return sensorHeartRateRepository.findAllByWatchIdAndDate(watch.getId(), LocalDate.now());
    }

    private List<SensorRow> heartRateReadTask(List<SensorHeartRate> sensorHeartRateList) {
        List<SensorRow> sensorRowList = createSensorRowList(sensorHeartRateList);
        deleteAllHeartRateData(sensorHeartRateList);
        return sensorRowList;
    }

    private void heartRateWriteTask(List<SensorRow> sensorRowList, Watch watch) {
        sensorRowList.forEach(sensorRow ->
                sensorDataRepository.updateHeartRateData(watch, sensorRow)
        );
    }

    private List<SensorRow> createSensorRowList(List<SensorHeartRate> sensorHeartRateList) {
        return sensorHeartRateList.stream()
                .map(SensorRow::heartRateOf)
                .collect(Collectors.toList());
    }

    private void deleteAllHeartRateData(List<SensorHeartRate> sensorHeartRateList) {
        List<String> sensorGyroscopeIds = sensorHeartRateList.stream()
                .map(SensorHeartRate::getId)
                .collect(Collectors.toList());
        sensorHeartRateRepository.deleteAllHeartRates(sensorGyroscopeIds);
    }
}
