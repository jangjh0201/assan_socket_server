package org.assansocketserver.global.config;

import org.assansocketserver.domain.sensor.scheduler.SensorScheduler;
import org.assansocketserver.domain.watch.repository.WatchLiveRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Configuration
public class SchedulerConfig {

    @Bean
    public SensorScheduler sensorScheduler(WatchLiveRepository watchLiveRepository, @Lazy SimpMessageSendingOperations sendingOperations) {
        return new SensorScheduler(watchLiveRepository, sendingOperations);
    }
}
