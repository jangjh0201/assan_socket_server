package org.assansocketserver.batch.cdc.step;

import lombok.RequiredArgsConstructor;

import org.assansocketserver.batch.cdc.entity.SensorRow;
import org.assansocketserver.batch.cdc.repository.SensorDataRepository;
import org.assansocketserver.domain.sensor.entity.SensorAccelerometer;
import org.assansocketserver.domain.sensor.entity.SensorGyroscope;
import org.assansocketserver.domain.sensor.mongorepository.Gyroscope.SensorGyroscopeRepository;
import org.assansocketserver.domain.watch.entity.Watch;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GyroscopeStep {
    private final SensorGyroscopeRepository sensorGyroscopeRepository;
    private final SensorDataRepository sensorDataRepository;

    public void gyroscopeStep(Watch watch) {
        List<SensorGyroscope> sensorGyroscopeList = gyroscopeReadTask(watch);
        List<SensorRow> sensorRowList = gyroscopeReadTask(sensorGyroscopeList);
        gyroscopeWriteTask(sensorRowList, watch);
    }

    private List<SensorGyroscope> gyroscopeReadTask(Watch watch) {
        return sensorGyroscopeRepository.findAllByWatchIdAndDate(watch.getId(), LocalDate.now());
    }

    private List<SensorRow> gyroscopeReadTask(List<SensorGyroscope> sensorGyroscopeList) {
        List<SensorRow> sensorRowList = createSensorRowList(sensorGyroscopeList);
        deleteAllGyroscopeData(sensorGyroscopeList);
        return sensorRowList;
    }

    private void gyroscopeWriteTask(List<SensorRow> sensorRowList, Watch watch) {
        sensorRowList.forEach(sensorRow ->
                sensorDataRepository.updateGyroscopeData(watch, sensorRow)
        );
    }

    private List<SensorRow> createSensorRowList(List<SensorGyroscope> sensorGyroscopeList) {
        return sensorGyroscopeList.stream()
                .map(SensorRow::gyroscopeOf)
                .collect(Collectors.toList());
    }

    private void deleteAllGyroscopeData(List<SensorGyroscope> sensorGyroscopeList) {
        List<String> sensorGyroscopeIds = sensorGyroscopeList.stream()
                .map(SensorGyroscope::getId)
                .collect(Collectors.toList());
        sensorGyroscopeRepository.deleteAllGyroscopes(sensorGyroscopeIds);
    }

    public SensorGyroscope getLatestGyroscopeData(Watch watch) {
        // 주어진 Watch ID와 현재 날짜에 해당하는 가장 최신의 Accelerometer 데이터를 가져옵니다.
        return sensorGyroscopeRepository
                .findTopByWatchIdAndDateOrderByTimestampDesc(watch.getId(), LocalDate.now());
    }
}
