package org.asansocketserver.domain.position.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.asansocketserver.domain.notification.service.NotificationService;
import org.asansocketserver.domain.position.dto.request.*;
import org.asansocketserver.domain.position.dto.response.PositionResponseDto;
import org.asansocketserver.domain.position.entity.BeaconData;
import org.asansocketserver.domain.position.entity.PositionData;
import org.asansocketserver.domain.position.entity.PositionState;
import org.asansocketserver.domain.position.mongorepository.PositionMongoRepository;
import org.asansocketserver.domain.position.repository.BeaconDataRepository;
import org.asansocketserver.domain.position.repository.PositionStateRepository;
import org.asansocketserver.domain.position.util.UniqueBSSIDMap;
import org.asansocketserver.domain.ward.repository.CoordinateRepository;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.asansocketserver.global.error.exception.EntityNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.asansocketserver.global.error.ErrorCode.WATCH_UUID_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PositionService {
    private final BeaconDataRepository beaconDataRepository;
    private final WatchRepository watchRepository;
    private final CoordinateRepository coordinateRepository;
    private final PositionStateRepository positionStateRepository;
    private final PositionMongoRepository positionMongoRepository;
    private final NotificationService notificationService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final RestTemplate restTemplate = new RestTemplate();
    private final SimpMessageSendingOperations sendingOperations;

    public static String UPLOAD_DIR = "C:\\Users\\Gachon\\Desktop\\BecaonCsvAsan\\";
    // public static String UPLOAD_DIR = "/Users/parkjaeseok/Desktop/csv/";
    // public static String UPLOAD_DIR = "/app/uploads/beaconCsv/";

    @Value("${flask.url}")
    private String flaskUrl;

    public List<BeaconCountsDTO> countBeacon() {
        return beaconDataRepository.findAllBeaconCount().stream()
                .map(result -> new BeaconCountsDTO((String) result[0], ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    public void insertState(StateDTO stateDTO) {

        Watch watch = findByWatchOrThrow(stateDTO.watchId());
        PositionState positionState = PositionState.createPositionState(watch.getId(), stateDTO.imageId(),
                stateDTO.position(), System.currentTimeMillis(), stateDTO.endTime());
        positionStateRepository.save(positionState);

        long delay = stateDTO.endTime() - System.currentTimeMillis();
        if (delay > 0) {
            scheduler.schedule(() -> {
                positionStateRepository.deleteById(watch.getId());
            }, delay, TimeUnit.MILLISECONDS);
        }
    }

    public void createCsv() throws JsonProcessingException {
        List<BeaconData> beaconDataList = beaconDataRepository.findAll();

        // 데이터를 저장할 Map
        Map<String, List<Map<String, String>>> data = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        // 모든 비콘 데이터를 파싱하여 Map에 저장
        for (BeaconData reading : beaconDataList) {
            List<Map<String, String>> beaconDataListToMap = objectMapper.readValue(
                    reading.getBeaconData(), new TypeReference<List<Map<String, String>>>() {
                    });
            String position = reading.getPosition();
            data.putIfAbsent(position, new ArrayList<>());

            for (Map<String, String> beaconData : beaconDataListToMap) {
                data.get(position).add(beaconData);
            }
        }

        // 유니크한 BSSID를 수집
        Set<String> uniqueBssids = new TreeSet<>(); // TreeSet을 사용하여 자동으로 정렬
        for (List<Map<String, String>> beaconDataMapList : data.values()) {
            for (Map<String, String> beaconData : beaconDataMapList) {
                uniqueBssids.add(beaconData.get("bssid"));
            }
        }

        UniqueBSSIDMap.getInstance().initializeBSSIDMap(uniqueBssids);

        // CSV 파일 생성
        try (FileWriter writer = new FileWriter(UPLOAD_DIR + "output.csv", StandardCharsets.UTF_8)) {
            // UTF-8 BOM 추가
            writer.write("\uFEFF");

            // 헤더 작성
            writer.append("Room");
            for (String bssid : uniqueBssids) {
                writer.append(",").append(bssid);
            }
            writer.append("\n");

            // 데이터 작성
            for (BeaconData reading : beaconDataList) {
                String position = reading.getPosition();
                List<Map<String, String>> beaconDataListToMap = objectMapper.readValue(
                        reading.getBeaconData(), new TypeReference<List<Map<String, String>>>() {
                        });

                // 한 행에 대한 데이터를 작성
                writer.append(position);
                Map<String, String> bssidToRssiMap = new HashMap<>();
                for (Map<String, String> beaconData : beaconDataListToMap) {
                    bssidToRssiMap.put(beaconData.get("bssid"), beaconData.get("rssi"));
                }
                for (String bssid : uniqueBssids) {
                    writer.append(",");
                    String rssi = bssidToRssiMap.get(bssid);
                    writer.append(rssi != null ? rssi : "NaN");
                }
                writer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteState(StateDTO stateDTO) {
        Watch watch = findByWatchOrThrow(stateDTO.watchId());
        positionStateRepository.deleteById(watch.getId());
    }

    public PositionState getCollectionState(Long androidId) {
        Watch watch = findByWatchOrThrow(String.valueOf(androidId));
        PositionState positionState = positionStateRepository.findById(watch.getId()).orElse(null);
        if (positionState == null)
            return PositionState.createPositionState(androidId, null, null, null, 0L);
        else
            return positionState;
    }

    @Transactional
    public PositionResponseDto receiveData(PosDataDTO posData, String destination) throws Exception {

        Watch watch = findByWatchOrThrow(posData.watchId());
        PositionState positionState = findByPositionStateOrNull(watch.getId());
        UniqueBSSIDMap baseMap = UniqueBSSIDMap.getInstance();
        UniqueBSSIDMap uniqueBSSIDMap = new UniqueBSSIDMap();

        System.out.println(posData.beaconData() + " " + posData.position());
        String prediction;
        Long imageId = null;

        synchronized (baseMap) {
            uniqueBSSIDMap.copyFrom(baseMap);

            try {

                System.out.println(posData.beaconData() + " " + posData.position());
                // positionState이 null이 아닌 상태는 "비콘 수집" 상태임
                if (!Objects.isNull(positionState)) {
                    System.out.println("positionState");
                    System.out.println("Check adding");
                    addPosData(posData, positionState.getImageId(), positionState.getPosition());

                } else {
                    for (BeaconDataDTO beaconData : posData.beaconData()) {
                        System.out.println(
                                "Updating beaconData bssid = " + beaconData.bssid() + ", rssi = " + beaconData.rssi());
                        uniqueBSSIDMap.updateBSSIDMap(beaconData.bssid(), String.valueOf(beaconData.rssi()));
                    }
                }
            } finally {
                baseMap.copyFrom(uniqueBSSIDMap);

                prediction = "null";
                if (!baseMap.getBSSIDMap().isEmpty()) {
                    prediction = sendUniqueBSSIDMapToFlask(uniqueBSSIDMap);
                }
                baseMap.resetBSSIDMapValues();

                // prediction이 나타나지 않을 경우 execption 처리 재확인
                // try {
                // imageId = coordinateRepository.findByPositionAndIsWebTrue(prediction)
                // .orElseThrow(() -> new NoSuchElementException("No coordinate found for the
                // given prediction"))
                // .getImage()
                // .getId();
                //
                // } catch (NoSuchElementException e) {
                // System.out.println("Image ID could not be retrieved: " + e.getMessage());
                // // 예외 발생 시 추가적인 로직을 여기에 작성
                // }
                System.out.println("imageId = " + imageId);
                System.out.println("After reset: " + baseMap.getBSSIDMap());
            }
        }

        if (posData.beaconData().isEmpty()) {
            prediction = "null";
        }
        watch.updateCurrentLocation(prediction);
        updatePositionData(watch.getId(), PositionData.of(prediction));

        String color = "null";

        return PositionResponseDto.of(watch.getId(), watch.getPatient().getName(), imageId, color, prediction);
    }

    private String sendUniqueBSSIDMapToFlask(UniqueBSSIDMap uniqueBSSIDMap) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> bssidMap = uniqueBSSIDMap.getBSSIDMap();
        HttpEntity<Map<String, String>> request = new HttpEntity<>(bssidMap, headers);

        ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, request, String.class);
        JSONObject jsonResponse = new JSONObject(response.getBody());
        return jsonResponse.getString("prediction");
    }

    private PositionState findByPositionStateOrNull(Long id) {
        return positionStateRepository.findById(id)
                .orElse(null);
    }

    public void deleteBeacon(String positionName) {
        List<BeaconData> beaconsByPosition = beaconDataRepository.findAllByPosition(positionName);
        beaconDataRepository.deleteAll(beaconsByPosition);
    }

    private String addPosData(PosDataDTO posData, Long imageId, String position) {

        if (posData.beaconData().isEmpty()) {

            return null;
        }

        for (BeaconDataDTO beaconData : posData.beaconData()) {
            System.out.println("scaning beaconData bssid = " + beaconData.bssid() + ", rssi = " + beaconData.rssi());
        }

        BeaconData beaconDataEntity = new BeaconData();
        beaconDataEntity.setImageId(imageId);
        beaconDataEntity.setPosition(position);
        String beaconDataJson = convertBeaconDataDtoToJson(posData.beaconData());
        beaconDataEntity.setBeaconData(beaconDataJson);
        beaconDataRepository.save(beaconDataEntity);
        return null;
    }

    // 받은 PosData에서 json({uuid, rssi})을 (DB)에 저장.
    private String convertBeaconDataDtoToJson(List beaconDataDTO) {
        // ObjectMapper를 사용하여 Beacon
        // DataDTO를 JSON 문자열로 변환
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(beaconDataDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Watch findByWatchOrThrow(String id) {
        return watchRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new EntityNotFoundException(WATCH_UUID_NOT_FOUND));
    }

    private void updatePositionData(Long watchId, PositionData position) {
        positionMongoRepository.updatePosition(watchId, position);
    }

}
