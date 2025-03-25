package org.assansocketserver.batch.cdc.step;

import lombok.RequiredArgsConstructor;

import org.assansocketserver.batch.cdc.entity.SensorRow;
import org.assansocketserver.batch.cdc.repository.SensorDataRepository;
import org.assansocketserver.domain.sensor.entity.SensorBarometer;
import org.assansocketserver.domain.sensor.mongorepository.barometer.SensorBarometerRepository;
import org.assansocketserver.domain.watch.entity.Watch;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BarometerStep {
    private final SensorBarometerRepository sensorBarometerRepository;
    private final SensorDataRepository sensorDataRepository;

    public void barometerStep(Watch watch) {
        List<SensorBarometer> sensorBarometerList = barometerReadTask(watch);
        List<SensorRow> sensorRowList = barometerProcessorTask(sensorBarometerList);
        barometerWriteTask(sensorRowList, watch);
    }

    private List<SensorBarometer> barometerReadTask(Watch watch) {
        return sensorBarometerRepository.findAllByWatchIdAndDate(watch.getId(), LocalDate.now());
    }

    private List<SensorRow> barometerProcessorTask(List<SensorBarometer> sensorBarometerList) {
        List<SensorRow> sensorRowList = createSensorRowList(sensorBarometerList);
        deleteAllAccelerometerData(sensorBarometerList);
        return sensorRowList;
    }

    private void barometerWriteTask(List<SensorRow> sensorRowList, Watch watch) {
        sensorRowList.forEach(sensorRow ->
                sensorDataRepository.updateBarometerData(watch, sensorRow)
        );
    }

    private List<SensorRow> createSensorRowList(List<SensorBarometer> sensorBarometerList) {
        return sensorBarometerList.stream()
                .map(SensorRow::barometerOf)
                .collect(Collectors.toList());
    }

    private void deleteAllAccelerometerData(List<SensorBarometer> sensorBarometerList) {
        List<String> sensorBarometerIds = sensorBarometerList.stream()
                .map(SensorBarometer::getId)
                .collect(Collectors.toList());
        sensorBarometerRepository.deleteAllBarometers(sensorBarometerIds);
    }
}
