package org.asan.batch.cdc.step;

import lombok.RequiredArgsConstructor;

import org.asan.batch.cdc.entity.SensorRow;
import org.asan.batch.cdc.repository.SensorDataRepository;
import org.asan.domain.sensor.entity.SensorLight;
import org.asan.domain.sensor.mongorepository.light.SensorLightRepository;
import org.asan.domain.watch.entity.Watch;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class LightStep {
    private final SensorLightRepository sensorLightRepository;
    private final SensorDataRepository sensorDataRepository;

    public void lightStep(Watch watch) {
        List<SensorLight> sensorLightList = lightReadTask(watch);
        List<SensorRow> sensorRowList = lightReadTask(sensorLightList);
        lightWriteTask(sensorRowList, watch);
    }

    private List<SensorLight> lightReadTask(Watch watch) {
        return sensorLightRepository.findAllByWatchIdAndDate(watch.getId(), LocalDate.now());
    }

    private List<SensorRow> lightReadTask(List<SensorLight> sensorLightList) {
        List<SensorRow> sensorRowList = createSensorRowList(sensorLightList);
        deleteAllLightData(sensorLightList);
        return sensorRowList;
    }

    private void lightWriteTask(List<SensorRow> sensorRowList, Watch watch) {
        sensorRowList.forEach(sensorRow ->
                sensorDataRepository.updateLightData(watch, sensorRow)
        );
    }

    private List<SensorRow> createSensorRowList(List<SensorLight> sensorLightList) {
        return sensorLightList.stream()
                .map(SensorRow::lightOf)
                .collect(Collectors.toList());
    }

    private void deleteAllLightData(List<SensorLight> sensorLightList) {
        List<String> sensorLightIds = sensorLightList.stream()
                .map(SensorLight::getId)
                .collect(Collectors.toList());
        sensorLightRepository.deleteAllLights(sensorLightIds);
    }
}
