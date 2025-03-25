package org.assansocketserver.mock;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.patient.repository.PatientRepository;
import org.assansocketserver.global.common.WebSocketMessage;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PatientScheduleService {

    private final PatientRepository patientRepository;

    public WebSocketMessage<List<Map<String, Object>>> getPatientList() {
        List<Patient> patients = patientRepository.findAll();
        List<Map<String, Object>> scheduleList = IntStream.range(0, patients.size())
                .mapToObj(i -> {
                    Patient patient = patients.get(i);
                    if (i == 0) {
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 2, // 예시: 다른 값 적용
                                "watch_battery", 88, // 예시: 다른 값 적용
                                "watch_charging", true, // 예시: 다른 값 적용
                                "riskgroup", true, // 예시: 다른 값 적용
                                "active_status", 2 // 예시: 다른 값 적용
                        );
                    } else if (i == 1) {
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 0, // 예시: 다른 값 적용
                                "watch_battery", 0, // 예시: 다른 값 적용
                                "watch_charging", false, // 예시: 다른 값 적용
                                "riskgroup", true, // 예시: 다른 값 적용
                                "active_status", 0 // 예시: 다른 값 적용
                        );
                    } else if (i == 2) {
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 2, // 예시: 다른 값 적용
                                "watch_battery", 29, // 예시: 다른 값 적용
                                "watch_charging", false, // 예시: 다른 값 적용
                                "riskgroup", true, // 예시: 다른 값 적용
                                "active_status", 1 // 예시: 다른 값 적용
                        );
                    } else if (i == 5) {
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 1, // 예시: 다른 값 적용
                                "watch_battery", 69, // 예시: 다른 값 적용
                                "watch_charging", false, // 예시: 다른 값 적용
                                "riskgroup", true, // 예시: 다른 값 적용
                                "active_status", 0 // 예시: 다른 값 적용
                        );
                    } else {
                        // 나머지 patient에 대한 기존 처리
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 2,
                                "watch_battery", 47,
                                "watch_charging", true,
                                "riskgroup", true,
                                "active_status", 0);
                    }
                })
                .collect(Collectors.toList());

        WebSocketMessage<List<Map<String, Object>>> message = WebSocketMessage.of("PATIENT_ALL", scheduleList);
        return message;
    }

    public WebSocketMessage<List<Map<String, Object>>> getPatientListFirst() {
        List<Patient> patients = patientRepository.findAll();
        List<Map<String, Object>> scheduleList = IntStream.range(0, patients.size())
                .mapToObj(i -> {
                    Patient patient = patients.get(i);
                    if (i == 0) {
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 2,
                                "watch_battery", 90,
                                "watch_charging", true,
                                "riskgroup", true,
                                "active_status", 0);
                    } else if (i == 1) {
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 0, // 예시: 다른 값 적용
                                "watch_battery", 0, // 예시: 다른 값 적용
                                "watch_charging", false, // 예시: 다른 값 적용
                                "riskgroup", true, // 예시: 다른 값 적용
                                "active_status", 0 // 예시: 다른 값 적용
                        );
                    } else if (i == 2) {
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 2, // 예시: 다른 값 적용
                                "watch_battery", 30, // 예시: 다른 값 적용
                                "watch_charging", false, // 예시: 다른 값 적용
                                "riskgroup", true, // 예시: 다른 값 적용
                                "active_status", 1 // 예시: 다른 값 적용
                        );
                    } else if (i == 5) {
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 1, // 예시: 다른 값 적용
                                "watch_battery", 70, // 예시: 다른 값 적용
                                "watch_charging", false, // 예시: 다른 값 적용
                                "riskgroup", true, // 예시: 다른 값 적용
                                "active_status", 0 // 예시: 다른 값 적용
                        );
                    } else {
                        // 나머지 patient에 대한 기존 처리
                        return Map.<String, Object>of(
                                "patient_id", patient.getId(),
                                "patient_name", patient.getName(),
                                "patient_number", patient.getNumber(),
                                "patient_room_name", patient.getSector().getName(),
                                "current_sector_id", patient.getSector().getId(),
                                "watch_status", 2,
                                "watch_battery", 50,
                                "watch_charging", true,
                                "riskgroup", true,
                                "active_status", 0);
                    }
                })
                .collect(Collectors.toList());

        WebSocketMessage<List<Map<String, Object>>> message = WebSocketMessage.of("PATIENT_ALL", scheduleList);
        return message;
    }

}
