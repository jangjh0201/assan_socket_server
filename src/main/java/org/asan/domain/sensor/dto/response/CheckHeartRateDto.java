package org.asan.domain.sensor.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckHeartRateDto(
                Long watchId,
                String watchName,
                String patientRoomName,
                Integer heartRate,
                Long wardId,
                String sectorName,
                LocalDateTime currentTime

) {
        public static CheckHeartRateDto of(Long watchId, String watchName, String patientRoomName, Long wardId,
                        String sectorName, Integer heartRate) {
                return CheckHeartRateDto.builder()
                                .watchId(watchId)
                                .watchName(watchName)
                                .patientRoomName(patientRoomName)
                                .heartRate(heartRate)
                                .wardId(wardId)
                                .sectorName(sectorName)
                                .currentTime(LocalDateTime.now()).build();
        }
}
