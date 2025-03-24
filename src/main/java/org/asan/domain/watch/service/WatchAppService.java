package org.asan.domain.watch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.asan.domain.watch.dto.response.WatchAllResponseDto;
import org.asan.domain.watch.dto.response.WatchResponseDto;
import org.asan.domain.watch.entity.Watch;
import org.asan.domain.watch.entity.WatchLive;
import org.asan.domain.watch.repository.WatchLiveRepository;
import org.asan.global.error.exception.ConflictException;
import org.asan.global.error.exception.EntityNotFoundException;
import org.asan.socket.dto.MessageType;
import org.asan.socket.dto.SocketBaseResponse;
import org.asan.domain.watch.dto.request.WatchRequestDto;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.asan.global.error.ErrorCode.*;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class WatchAppService {
    private final org.asan.domain.watch.repository.WatchRepository watchRepository;
    private final WatchLiveRepository watchLiveRepository;
    private final SimpMessageSendingOperations sendingOperations;


    public WatchAllResponseDto findAllWatch() {
        List<Watch> watchList = findAllByWatch();
        List<WatchResponseDto> watchResponseDtoList = WatchResponseDto.listOf(watchList);
        return WatchAllResponseDto.of(watchResponseDtoList);
    }

    public Long deleteWatch(Long id) {

        Optional<Watch> watch = watchRepository.findById(id);
        Optional<WatchLive> watchLive = watchLiveRepository.findById(id);

        watchRepository.delete(watch.get());
        
        watchLive.ifPresent(watchLiveRepository::delete);
        sendingOperations.convertAndSend("/queue/sensor/9999999", SocketBaseResponse.of(MessageType.DEL_WATCH, id));

        return id;
    }

    public WatchResponseDto findWatch(String uuid) {
        Watch watch = findByWatchOrThrow(uuid);
        return WatchResponseDto.of(watch);
    }

    public WatchResponseDto createWatch(WatchRequestDto watchRequestDto) {
        validateDuplicateWatch(watchRequestDto);
        Watch createdWatch = createWatchAndSave(watchRequestDto);
        Long newWatchId = watchRepository.findByUuid(watchRequestDto.uuid()).get().getId();
        sendingOperations.convertAndSend("/queue/sensor/9999999",
                SocketBaseResponse.of(MessageType.NEW_WATCH, newWatchId));
        return WatchResponseDto.of(createdWatch);
    }

    private Watch createWatchAndSave(WatchRequestDto watchRequestDto) {
        Watch createdWatch = Watch.createWatch(watchRequestDto.uuid(), watchRequestDto.device());
        return watchRepository.save(createdWatch);
    }

    private void validateDuplicateWatch(WatchRequestDto watchRequestDto) {
        if (watchRepository.existsByUuid(watchRequestDto.uuid()))
            throw new ConflictException(DUPLICATE_WATCH_UUID);
    }

    private Watch findByWatchIdOrThrow(Long id) {
        return watchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(WATCH_NOT_FOUND));
    }

    private Watch findByWatchOrThrow(String uuid) {
        return watchRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(WATCH_UUID_NOT_FOUND));
    }

    private List<Watch> findAllByWatch() {
        return watchRepository.findAll();
    }

}
