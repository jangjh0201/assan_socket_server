package org.asansocketserver.domain.watch.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.watch.entity.Watch;
import java.util.List;
import java.util.stream.Collectors;

@Builder(access = AccessLevel.PRIVATE)
public record WatchResponseDto(
        Long watchId,
        String device,
        String name,
        String host
) {
    public static WatchResponseDto of(Watch watch) {
        return WatchResponseDto.builder()
                .watchId(watch.getId())
                .device(watch.getDevice())
                .name(watch.getName())
                .host(watch.getHost())
                .build();
    }

    public static List<WatchResponseDto> listOf(List<Watch> watchList) {
        return watchList.stream()
                .map(WatchResponseDto::of)
                .collect(Collectors.toList());
    }
}
