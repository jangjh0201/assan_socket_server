package org.asan.domain.sleep.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SleepService {

    private static final String SLEEP_PREDICT_URL = "http://localhost:5001/sleep_predict";
    private static final String SENSOR_DATA_FILE_PATH = "C:/Users/Junho/Downloads/flask-sleep/sensor_test_data.json"; // ✅ JSON 파일 경로

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> getSleepPredictions() {
        try {
            // JSON 파일 존재 여부 확인
            File jsonFile = new File(SENSOR_DATA_FILE_PATH);
            if (!jsonFile.exists()) {
                throw new FileNotFoundException("Sensor data file not found at: " + SENSOR_DATA_FILE_PATH);
            }

            // JSON을 Map<String, Object>로 변환
            Map<String, Object> requestBody = objectMapper.readValue(jsonFile, new TypeReference<>() {});

            // RestClient로 Flask 서버에 POST 요청 보내기
            return restClient.post()
                    .uri(SLEEP_PREDICT_URL)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Sensor data JSON file is missing: " + SENSOR_DATA_FILE_PATH, e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while processing sensor data JSON", e);
        }
    }
}