package org.assansocketserver.domain.sleep.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assansocketserver.domain.sensor.entity.SensorAccelerometer;
import org.assansocketserver.domain.sensor.mongorepository.accelerometer.SensorAccelerometerRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SleepService {

    private static final String SLEEP_PREDICT_URL = "http://localhost:5001/sleep_predict";

    private final SensorAccelerometerRepository sensorAccelerometerRepository;
    private final RestClient restClient = RestClient.create();

    public List<Map<String, Object>> getSleepPredictions() {
        try {
            // 1. 가장 최신 데이터 조회
            SensorAccelerometer latest = sensorAccelerometerRepository
                    .findTopByOrderByTimestampDesc();

            if (latest == null) {
                throw new RuntimeException("가속도 데이터가 존재하지 않습니다.");
            }

            // 2. 최신 timestamp 기준 30초 전 시점 계산
            long thirtySecondsAgo = latest.getTimestamp() - 30_000;

            // 3. 30초 내 데이터 조회
            List<SensorAccelerometer> sensors = sensorAccelerometerRepository
                    .findAllByTimestampGreaterThanEqual(thirtySecondsAgo);

            // 4. 필요한 필드만 Map으로 변환 (id, date 제외)
            List<Map<String, Object>> dataList = sensors.stream()
                    .map(sensor -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("watch_id", sensor.getWatchId());
                        map.put("timestamp", sensor.getTimestamp());
                        map.put("accX", sensor.getAccX());
                        map.put("accY", sensor.getAccY());
                        map.put("accZ", sensor.getAccZ());
                        return map;
                    })
                    .collect(Collectors.toList());

            log.info("수면 측정 센서 데이터 크기: {}", dataList.size());

            // 5. Flask 서버로 POST 요청
            return restClient.post()
                    .uri(SLEEP_PREDICT_URL)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Map.of("data", dataList))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (Exception e) {
            throw new RuntimeException("Sleep prediction 처리 중 오류 발생", e);
        }
    }

    public List<Map<String, Object>> getMultiWatchSleepPredictions(List<Long> watchIds) {
        try {
            // 1. 모든 watchId에 대해 최신 30건 가져오기
            List<SensorAccelerometer> allData = watchIds.stream()
                    .flatMap(id -> sensorAccelerometerRepository
                            .findTop30ByWatchIdOrderByTimestampDesc(id)
                            .stream())
                    .collect(Collectors.toList());

            // 2. 필요한 필드만 추출
            List<Map<String, Object>> dataList = allData.stream()
                    .map(sensor -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("watch_id", sensor.getWatchId());
                        map.put("timestamp", sensor.getTimestamp());
                        map.put("accX", sensor.getAccX());
                        map.put("accY", sensor.getAccY());
                        map.put("accZ", sensor.getAccZ());
                        return map;
                    })
                    .collect(Collectors.toList());

            log.info("수면 측정 watch_id 개수: {}, 전송할 전체 데이터 수: {}", watchIds.size(), dataList.size());

            // 3. Flask 서버에 POST
            return restClient.post()
                    .uri(SLEEP_PREDICT_URL)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Map.of("data", dataList))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (Exception e) {
            throw new RuntimeException("Sleep prediction 처리 중 오류 발생", e);
        }
    }

}
