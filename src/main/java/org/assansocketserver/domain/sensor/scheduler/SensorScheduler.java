package org.assansocketserver.domain.sensor.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.watch.dto.response.WatchLiveResponseDto;
import org.assansocketserver.domain.watch.entity.WatchLive;
import org.assansocketserver.domain.watch.repository.WatchLiveRepository;
import org.assansocketserver.socket.dto.MessageType;
import org.assansocketserver.socket.dto.SocketBaseResponse;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
// @Component
public class SensorScheduler {
    private final WatchLiveRepository watchLiveRepository;
    private final SimpMessageSendingOperations sendingOperations;

    @Transactional
    @Scheduled(cron = "30 * * * * *")
    public void broadcastWatchList() {
        List<WatchLiveResponseDto> responseDto = findAllWatch();
        sendingOperations.convertAndSend("/queue/sensor/9999999",
                SocketBaseResponse.of(MessageType.WATCH_LIST, responseDto));
    }

    public void sendDisconnectWatch(Long watchId) {
        WatchLiveResponseDto responseDto = WatchLiveResponseDto.of(watchId);
        sendingOperations.convertAndSend("/queue/sensor/9999999",
                SocketBaseResponse.of(MessageType.DIS_WATCH, responseDto));
    }

    private List<WatchLiveResponseDto> findAllWatch() {
        List<WatchLive> watchLiveList = findAllWatchInRedis();
        return WatchLiveResponseDto.liveListOf(watchLiveList);
    }

    private List<WatchLive> findAllWatchInRedis() {
        return watchLiveRepository.findAllByLive(true);
    }
}