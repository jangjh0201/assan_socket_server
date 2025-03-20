package org.asansocketserver.domain.sensor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.image.entity.Coordinate;
import org.asansocketserver.domain.image.repository.CoordinateRepository;
import org.asansocketserver.domain.notification.service.NotificationService;
import org.asansocketserver.domain.sensor.dto.request.*;
import org.asansocketserver.domain.sensor.dto.response.*;
import org.asansocketserver.domain.sensor.entity.*;
import org.asansocketserver.domain.sensor.entity.sensorType.*;
import org.asansocketserver.domain.sensor.mongorepository.Gyroscope.SensorGyroscopeRepository;
import org.asansocketserver.domain.sensor.mongorepository.accelerometer.SensorAccelerometerRepository;
import org.asansocketserver.domain.sensor.mongorepository.barometer.SensorBarometerRepository;
import org.asansocketserver.domain.sensor.mongorepository.heartrate.SensorHeartRateRepository;
import org.asansocketserver.domain.sensor.mongorepository.light.SensorLightRepository;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.asansocketserver.global.error.exception.EntityNotFoundException;
import org.asansocketserver.socket.dto.MessageType;
import org.asansocketserver.socket.dto.SocketBaseResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.asansocketserver.domain.sensor.entity.sensorType.Accelerometer.createAccelerometer;
import static org.asansocketserver.domain.sensor.entity.sensorType.HeartRate.createHeartRate;
import static org.asansocketserver.global.error.ErrorCode.WATCH_UUID_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class SensorService {
    private static final String SESSION_DATA = "watchId";
    private final SensorAccelerometerRepository sensorAccelerometerRepository;
    private final SensorGyroscopeRepository sensorGyroscopeRepository;
    private final SensorBarometerRepository sensorBarometerRepository;
    private final SensorHeartRateRepository sensorHeartRateRepository;
    private final SensorLightRepository sensorLightRepository;
    private final WatchRepository watchRepository;
    private final SimpMessageSendingOperations sendingOperations;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CoordinateRepository coordinateRepository;
    private final NotificationService notificationService;

    private Watch findByWatchOrThrow(Long id) {
        return watchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(WATCH_UUID_NOT_FOUND));
    }

    public void sensorSendState(StateRequestDto stateDTO) {
        Watch watch = findByWatchOrThrow(stateDTO.watchId());
        redisTemplate.opsForValue().set("sensorSendState:" + watch.getId(), watch.getId());
        redisTemplate.expire("sensorSendState:" + watch.getId(), 3, TimeUnit.MINUTES);
    }

    public void sendAccelerometer(Map<String, Object> simpSessionAttributes,
            AccelerometerRequestDto accelerometerRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        Accelerometer createdAccelerometer = createAccelerometer(accelerometerRequestDto);
        createAccelerometerAndSave(watchId, accelerometerRequestDto);

        String destination = "/queue/sensor/" + simpSessionAttributes.get("watchId");

        Object sensorSendState = redisTemplate.opsForValue().get("sensorSendState:" + watchId);

        if (!Objects.isNull(sensorSendState)) {
            sendingOperations.convertAndSend(destination, SocketBaseResponse.of(MessageType.ACCELEROMETER,
                    AccelerometerResponseDto.of(createdAccelerometer)));
        }
    }

    public void sendBarometer(Map<String, Object> simpSessionAttributes,
            BarometerRequestDto barometerRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        createBarometerAndSave(watchId, barometerRequestDto);
    }

    public void sendGyroscope(Map<String, Object> simpSessionAttributes,
            GyroscopeRequestDto gyroscopeRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        createGyroscopeAndSave(watchId, gyroscopeRequestDto);
    }

    public void sendHeartRate(Map<String, Object> simpSessionAttributes,
            HeartRateRequestDto heartRateRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        Optional<Watch> watch = watchRepository.findById(watchId);
        HeartRate heartRate = createHeartRate(heartRateRequestDto);
        String position = watch.get().getCurrentLocation();
        Optional<Coordinate> coordinate = Optional.empty();
        Long imageId = null;

        if (position != null && !position.isEmpty()) {
            coordinate = coordinateRepository.findByPositionAndIsWebTrue(position);
            if (coordinate.isPresent()) {
                imageId = coordinate.get().getImage().getId();
            }
        }
        createHeartRateAndSave(watchId, heartRateRequestDto);

        String destination = "/queue/sensor/" + simpSessionAttributes.get("watchId");

        if (watch.get().getMaxHeartRate() < heartRate.getValue()) {

            sendingOperations.convertAndSend(destination,
                    SocketBaseResponse.of(MessageType.HIGH_HEART_RATE,
                            CheckHeartRateDto.of(watchId, watch.get().getName(), watch.get().getHost(), imageId,
                                    watch.get().getCurrentLocation(), "blue", heartRate.getValue())));
            notificationService.createAndSaveNotification(watch.get(), imageId, position, "HIGH-HEART-RATE");
        } else if (watch.get().getMinHeartRate() > heartRate.getValue()) {
            sendingOperations.convertAndSend(destination,
                    SocketBaseResponse.of(MessageType.LOW_HEART_RATE,
                            CheckHeartRateDto.of(watchId, watch.get().getName(), watch.get().getHost(), imageId,
                                    watch.get().getCurrentLocation(), "red", heartRate.getValue())));
            notificationService.createAndSaveNotification(watch.get(), imageId, position, "LOW-HEART-RATE");
        }

        Object sensorSendState = redisTemplate.opsForValue().get("sensorSendState:" + watchId);

        if (!Objects.isNull(sensorSendState)) {
            sendingOperations.convertAndSend(destination,
                    SocketBaseResponse.of(MessageType.HEART_RATE, HeartRateResponseDto.of(heartRate)));
        }

    }

    public void sendLight(Map<String, Object> simpSessionAttributes,
            LightRequestDto lightRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        createLightAndSave(watchId, lightRequestDto);
    }

    private Long getWatchIdFromSession(Map<String, Object> simpSessionAttributes) {
        return (Long) simpSessionAttributes.get(SESSION_DATA);
    }

    private void createAccelerometerAndSave(Long watchId, AccelerometerRequestDto accelerometer) {
        SensorAccelerometer sensorAccelerometer = SensorAccelerometer.createSensor(watchId, accelerometer);

        sensorAccelerometerRepository.save(sensorAccelerometer);
    }

    private void createBarometerAndSave(Long watchId, BarometerRequestDto barometerRequestDto) {
        SensorBarometer sensorBarometer = SensorBarometer.createSensor(watchId, barometerRequestDto);
        sensorBarometerRepository.save(sensorBarometer);
    }

    private void createGyroscopeAndSave(Long watchId, GyroscopeRequestDto gyroscopeRequestDto) {
        SensorGyroscope sensorGyroscope = SensorGyroscope.createSensor(watchId, gyroscopeRequestDto);
        sensorGyroscopeRepository.save(sensorGyroscope);
    }

    private void createHeartRateAndSave(Long watchId, HeartRateRequestDto heartRateRequestDto) {
        SensorHeartRate sensorHeartRate = SensorHeartRate.createSensor(watchId, heartRateRequestDto);
        sensorHeartRateRepository.save(sensorHeartRate);
    }

    private void createLightAndSave(Long watchId, LightRequestDto lightRequestDto) {
        SensorLight sensorLight = SensorLight.createSensor(watchId, lightRequestDto);
        sensorLightRepository.save(sensorLight);
    }
}
