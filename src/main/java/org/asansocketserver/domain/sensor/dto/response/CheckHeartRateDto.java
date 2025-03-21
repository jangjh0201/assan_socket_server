package org.asansocketserver.domain.sensor.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckHeartRateDto(
        Long watchId,
        String watchName,
        String patientRoomName,
        Integer heartRate,
        Long imageId,
        String position,
        String color,
        LocalDateTime currentTime

) {
    public static CheckHeartRateDto of(Long watchId, String watchName, String patientRoomName, Long imageId,
            String position, String color, Integer heartRate) {
        return CheckHeartRateDto.builder()
                .watchId(watchId)
                .watchName(watchName)
                .patientRoomName(patientRoomName)
                .heartRate(heartRate)
                .imageId(imageId)
                .position(position)
                .color(color)
                .currentTime(LocalDateTime.now()).build();
    }
}
