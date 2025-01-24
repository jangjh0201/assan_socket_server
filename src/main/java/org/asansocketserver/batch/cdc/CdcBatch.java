package org.asansocketserver.batch.cdc;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.batch.cdc.step.*;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
@Component
public class CdcBatch {
    private final WatchRepository watchRepository;
    private final AccelerometerStep accelerometerStep;
    private final BarometerStep barometerStep;
    private final GyroscopeStep gyroscopeStep;
    private final HeartRateStep heartRateStep;
    private final LightStep lightStep;

    @Transactional
    @Scheduled(fixedRate = 33)
    public void createSensorDateBatch() {
        List<Watch> watchList = findAllByWatch();
        watchList.forEach(watch -> {
            accelerometerStep.execute(watch);
            // barometerStep.barometerStep(watch);
            // gyroscopeStep.gyroscopeStep(watch);
            // heartRateStep.heartRateStep(watch);
            // lightStep.lightStep(watch);
        });
    }

    private List<Watch> findAllByWatch() {
        return watchRepository.findAll();
    }
}
