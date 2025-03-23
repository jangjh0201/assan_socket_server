package org.asansocketserver.domain.watch.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.watch.entity.Watch;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonProperty;

@Builder(access = AccessLevel.PRIVATE)
public record WatchResponseDto(
        Long watchId,
        String device,
        @JsonProperty("name") String patientName,
        @JsonProperty("host") String patientWard) {
    public static WatchResponseDto of(Watch watch) {
        return WatchResponseDto.builder()
                .watchId(watch.getId())
                .device(watch.getDevice())
                .patientName(watch.getPatient() == null ? "지정되지 않음" : watch.getPatient().getName())
                .patientWard(watch.getPatient() == null ? "지정되지 않음" : watch.getPatient().getWard())
                .build();
    }

    public static List<WatchResponseDto> listOf(List<Watch> watchList) {
        return watchList.stream()
                .map(WatchResponseDto::of)
                .collect(Collectors.toList());
    }
}
