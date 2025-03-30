package org.assansocketserver.domain.sensor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.notification.dto.NotificationDTO;
import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.risk.entity.Risk;
import org.assansocketserver.domain.risk.entity.RiskType;
import org.assansocketserver.domain.sensor.dto.request.*;
import org.assansocketserver.domain.sensor.dto.response.*;
import org.assansocketserver.domain.sensor.entity.*;
import org.assansocketserver.domain.sensor.entity.sensorType.*;
import org.assansocketserver.domain.sensor.mongorepository.Gyroscope.SensorGyroscopeRepository;
import org.assansocketserver.domain.sensor.mongorepository.accelerometer.SensorAccelerometerRepository;
import org.assansocketserver.domain.sensor.mongorepository.barometer.SensorBarometerRepository;
import org.assansocketserver.domain.sensor.mongorepository.heartrate.SensorHeartRateRepository;
import org.assansocketserver.domain.sensor.mongorepository.light.SensorLightRepository;
import org.assansocketserver.domain.watch.entity.Watch;
import org.assansocketserver.domain.watch.repository.WatchRepository;
import org.assansocketserver.global.error.exception.EntityNotFoundException;
import org.assansocketserver.socket.dto.MessageType;
import org.assansocketserver.socket.dto.SocketBaseResponse;
import org.assansocketserver.socket.message.NotificationMessageHandler;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import static org.assansocketserver.domain.sensor.entity.sensorType.Accelerometer.createAccelerometer;
import static org.assansocketserver.domain.sensor.entity.sensorType.HeartRate.createHeartRate;
import static org.assansocketserver.global.error.ErrorCode.WATCH_UUID_NOT_FOUND;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private final NotificationMessageHandler notificationMessageHandler;

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
        Optional<Watch> watchOptional = watchRepository.findWithPatientWardAndRisksById(watchId);

        if (watchOptional.isEmpty()) {
            throw new EntityNotFoundException(WATCH_UUID_NOT_FOUND);
        }

        Watch watch = watchOptional.get();
        HeartRate heartRate = createHeartRate(heartRateRequestDto);
        String currentLocation = watch.getCurrentLocation();

        createHeartRateAndSave(watchId, heartRateRequestDto);

        String destination = "/queue/sensor/" + simpSessionAttributes.get("watchId");

        Patient patient = watch.getPatient();

        // 위급상황 구분
        final String riskType;
        if (heartRate.getValue() == 0)
            riskType = "워치 탈착";
        else if (heartRate.getValue() > patient.getMaxHeartRate())
            riskType = "고심박";
        else if (heartRate.getValue() < patient.getMinHeartRate())
            riskType = "저심박";
        else
            riskType = null;

        Risk risk = patient.getWard().getRisks().stream()
                .filter(r -> r.getRiskType().getName().equals(riskType) && r.getAvailability() != false)
                .findFirst()
                .orElse(null);

        if (risk != null) {
            String message;
            if ("워치 탈착".equals(risk.getRiskType().getName())) {
                // 워치 탈착일 경우 patient 정보 없이 메시지 작성
                message = String.format("%s(%s)님 워치 탈착 발생",
                        patient.getName(),
                        patient.getSector().getName());
            } else {
                // 그 외의 경우 기존 메시지 포맷 사용
                message = String.format("%s(%s)님 %s(%s) 발생",
                        patient.getName(),
                        patient.getSector().getName(),
                        risk.getRiskType().getName(),
                        heartRate.getValue());
            }
            NotificationDTO notificationDTO = NotificationDTO.builder()
                    .category("risk")
                    .data(Map.of(
                            "risk_id", risk.getRiskType().getId(),
                            "risk_name", risk.getRiskType().getName(),
                            "severity", risk.getSeverity(),
                            "patient_id", patient.getId(),
                            "patient_name", patient.getName(),
                            "sector_id", patient.getSector().getId(),
                            "sector_name", patient.getSector().getName(),
                            "message", message,
                            "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                    .build();
            notificationMessageHandler.sendNewNotification(notificationDTO);
        }

        Object sensorSendState = redisTemplate.opsForValue().get("sensorSendState:" + watchId);
        if (sensorSendState != null) {
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
