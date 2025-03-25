package org.assansocketserver.mock;

import org.assansocketserver.domain.notification.dto.NotificationDTO;
import org.assansocketserver.domain.patient.repository.PatientRepository;
import org.assansocketserver.domain.risk.repository.RiskRepository;
import org.assansocketserver.domain.risk.repository.RiskTypeRepository;
import org.assansocketserver.domain.sector.repository.SectorRepository;
import org.assansocketserver.socket.message.NotificationMessageHandler;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class NotificationScheduler {

    private final PatientRepository patientRepository;
    private final RiskRepository riskRepository;
    private final SectorRepository sectorRepository;

    private final NotificationMessageHandler notificationMessageHandler;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void startScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // 현재 시간의 ISO-8601 문자열 생성 (UTC)
                String timestampStr = Instant.now().toString();
                // Instant로 파싱 후 시스템 기본 시간대로 LocalDateTime 변환
                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.parse(timestampStr),
                        ZoneId.systemDefault());

                // 알림 데이터 생성 (LocalDateTime을 문자열로 변환하여 사용)
                NotificationDTO notificationDTO = NotificationDTO.builder()
                        .category("risk")
                        .data(
                                Map.of(
                                        "risk_id", riskRepository.findAll().get(0).getId(),
                                        "risk_name", riskRepository.findAll().get(0).getRiskType().getName(),
                                        "severity", riskRepository.findAll().get(0).getSeverity(),
                                        "patient_id", patientRepository.findAll().get(0).getId(),
                                        "patient_name", patientRepository.findAll().get(0).getName(),
                                        "sector_id", sectorRepository.findAll().get(0).getId(),
                                        "sector_name", sectorRepository.findAll().get(0).getName(),
                                        "position", sectorRepository.findAll().get(2).getName(),
                                        "message", String.format("%s(%s)님 %s 발생",
                                                patientRepository.findAll().get(0).getName(),
                                                patientRepository.findAll().get(0).getSector().getName(),
                                                riskRepository.findAll().get(0).getRiskType().getName()),
                                        "timestamp", localDateTime.toString()))
                        .build();

                // sendNewNotification 메서드를 이용하여 알림 전송
                notificationMessageHandler.sendNewNotification(notificationDTO);
            } catch (Exception e) {
                // 스케줄러 내에서 예외 발생시 로깅 또는 처리
                e.printStackTrace();
            }
        }, 0, 7, TimeUnit.SECONDS);
    }

}
