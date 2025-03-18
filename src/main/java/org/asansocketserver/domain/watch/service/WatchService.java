package org.asansocketserver.domain.watch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.batch.cdc.entity.SensorData;
import org.asansocketserver.batch.cdc.repository.SensorDataRepository;
import org.asansocketserver.domain.image.dto.CoordinateIDAndPositionDTO;
import org.asansocketserver.domain.image.dto.ImageIDAndPositionAndCoordinateDTO;
import org.asansocketserver.domain.image.entity.Coordinate;
import org.asansocketserver.domain.image.repository.CoordinateRepository;
import org.asansocketserver.domain.watch.dto.request.WatchRequestDto;
import org.asansocketserver.domain.watch.dto.request.WatchUpdateRequestDto;
import org.asansocketserver.domain.watch.dto.response.WatchAllResponseDto;
import org.asansocketserver.domain.watch.dto.response.WatchResponseDto;
import org.asansocketserver.domain.watch.dto.web.request.WatchNoContactedRequestDto;
import org.asansocketserver.domain.watch.dto.web.request.WatchProhibitedCoordinatesUpdateRequestDto;
import org.asansocketserver.domain.watch.dto.web.request.WatchTransferDto;

import org.asansocketserver.domain.watch.dto.web.response.*;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.entity.WatchCoordinateProhibition;
import org.asansocketserver.domain.watch.entity.WatchLive;
import org.asansocketserver.domain.watch.entity.WatchNoContact;
import org.asansocketserver.domain.watch.repository.WatchLiveRepository;
import org.asansocketserver.domain.watch.repository.WatchNoContactRepository;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.asansocketserver.global.error.exception.ConflictException;
import org.asansocketserver.global.error.exception.EntityNotFoundException;
//import org.asansocketserver.socket.dto.MessageType;
//import org.asansocketserver.socket.dto.SocketBaseResponse;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.asansocketserver.socket.dto.MessageType;
import org.asansocketserver.socket.dto.SocketBaseResponse;
import org.bson.types.ObjectId;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.asansocketserver.global.error.ErrorCode.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class WatchService {
    private final WatchRepository watchRepository;
    private final WatchLiveRepository watchLiveRepository;
    private final SimpMessageSendingOperations sendingOperations;
    private final WatchNoContactRepository watchNoContactRepository;
    private final CoordinateRepository coordinateRepository;
    private final SensorDataRepository sensorDataRepository;
    private final MongoTemplate mongoTemplate;

    public WatchResponseDto updateWatchInfo(Long watchId, WatchUpdateRequestDto watchUpdateRequestDto) {
        Watch watch = findByWatchIdOrThrow(watchId);
        watch.updateWatch(watchUpdateRequestDto);

        boolean isDuplicateName = watchRepository.existsByNameAndIdNot(watchUpdateRequestDto.name(), watchId);
        if (isDuplicateName) {
            throw new IllegalArgumentException("중복 이름이 존재합니다.");
        }

        Optional<SensorData> sensorData = sensorDataRepository.findByWatchIdAndDate(watch.getId(), LocalDate.now());

        if (sensorData.isPresent()) {
            mongoTemplate.updateFirst(
                    query(where("watch_id").is(watchId).and("date").is(LocalDate.now())),
                    update("name", watchUpdateRequestDto.name()),
                    SensorData.class);
        }

        return WatchResponseDto.of(watch);
    }

    public WatchAllResponseDto findAllWatch() {
        List<Watch> watchList = findAllByWatch();
        List<WatchResponseDto> watchResponseDtoList = WatchResponseDto.listOf(watchList);
        return WatchAllResponseDto.of(watchResponseDtoList);
    }

    public Long deleteWatch(Long id) {

        Optional<Watch> watch = watchRepository.findById(id);
        Optional<WatchLive> watchLive = watchLiveRepository.findById(id);

        if (watch.isPresent()) {
            watchNoContactRepository.deleteAllByWatch(watch.get());
            watchNoContactRepository.deleteAllByNoContactWatch(watch.get());

            watchRepository.delete(watch.get());
        }

        watchLive.ifPresent(watchLiveRepository::delete);
        sendingOperations.convertAndSend("/queue/sensor/9999999", SocketBaseResponse.of(MessageType.DEL_WATCH, id));

        return id;
    }

    public WatchResponseDto findWatch(String uuid) {
        Watch watch = findByWatchOrThrow(uuid);
        return WatchResponseDto.of(watch);
    }


    public List<WatchIdAndNameDto> getWatchForNoContact() {
        List<Watch> watchList = watchRepository.findAll();
        List<WatchIdAndNameDto> watchIdAndNameDtos = new ArrayList<>();
        for (Watch watch : watchList) {
            WatchIdAndNameDto watchIdAndNameDto = new WatchIdAndNameDto(watch.getId(), watch.getName());
            watchIdAndNameDtos.add(watchIdAndNameDto);
        }
        return watchIdAndNameDtos;
    }

    public NoContactAndProhibitedIdWithNameDto getNoContactAndProhibitedIdWithName(Long watchId) {
        Watch watch = findByWatchIdOrThrow(watchId);

        List<WatchIdAndNameDto> watchIdAndNameDtos = getNoContactWatchDtos(watch);
        List<ImageIDAndPositionAndCoordinateDTO> coordinateIDAndPositionDtos = getProhibitedCoordinateDtos(watch);

        return NoContactAndProhibitedIdWithNameDto.of(watchIdAndNameDtos, coordinateIDAndPositionDtos);
    }

    private List<WatchIdAndNameDto> getNoContactWatchDtos(Watch watch) {
        return watch.getNoContactWatchList().stream()
                .map(watchNoContact -> WatchIdAndNameDto.of(
                        watchNoContact.getNoContactWatch().getId(),
                        watchNoContact.getNoContactWatch().getName()))
                .collect(Collectors.toList());
    }

    private List<ImageIDAndPositionAndCoordinateDTO> getProhibitedCoordinateDtos(Watch watch) {
        return watch.getProhibitedCoordinateList().stream()
                .map(watchCoordinateProhibition -> ImageIDAndPositionAndCoordinateDTO.of(
                        watchCoordinateProhibition.getCoordinate().getImage().getId(),
                        watchCoordinateProhibition.getCoordinate().getId(),
                        watchCoordinateProhibition.getCoordinate().getPosition()))
                .collect(Collectors.toList());
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

    @Transactional
    public WatchNoContactResponseDto updateNoContactWatchList(Long watchId, List<Long> noContactWatchIds) {
        Optional<Watch> watchOptional = watchRepository.findById(watchId);
        if (watchOptional.isEmpty()) {
            throw new IllegalArgumentException(watchId + "번 워치는 존재하지않습니다.");
        }

        Watch watch = watchOptional.get();
        watch.getNoContactWatchList().clear();

        for (Long noContactWatchId : noContactWatchIds) {
            Optional<Watch> noContactWatchOptional = watchRepository.findById(noContactWatchId);
            if (noContactWatchOptional.isEmpty()) {
                throw new IllegalArgumentException(watchId + "번 워치는 존재하지않아 접촉 금지 대상에 지정할 수 없습니다.");
            }
            watch.addNoContactWatch(noContactWatchOptional.get());
        }

        WatchNoContactResponseDto responseDto = new WatchNoContactResponseDto();
        responseDto.setWatchId(watch.getId());
        responseDto.setNoContactWatchIds(noContactWatchIds);

        return responseDto;
    }

    public WatchProhibitedCoordinatesUpdateResponseDto updateProhibitedCoordinateList(Long watchId,
            List<Long> prohibitedCoordinatesIds) {
        Optional<Watch> watchOptional = watchRepository.findById(watchId);
        if (watchOptional.isEmpty()) {
            throw new IllegalArgumentException(watchId + "번 워치는 존재하지않습니다.");
        }

        Watch watch = watchOptional.get();
        watch.getProhibitedCoordinateList().clear();

        for (Long prohibitedCoordinate : prohibitedCoordinatesIds) {
            Optional<Coordinate> prohibitedCoordinateOptional = coordinateRepository.findById(prohibitedCoordinate);
            if (prohibitedCoordinateOptional.isEmpty()) {
                throw new IllegalArgumentException(watchId + "번 위치(좌표)는 존재하지않습니다.");
            }
            watch.addProhibitedCoordinate(prohibitedCoordinateOptional.get());
        }

        return WatchProhibitedCoordinatesUpdateResponseDto.of(watch.getId(), prohibitedCoordinatesIds);

    }
}
