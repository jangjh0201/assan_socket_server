package org.asan.domain.watch.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record WatchAllResponseDto(
        List<WatchResponseDto> watchList
) {
    public static WatchAllResponseDto of(List<WatchResponseDto> responseDtoList) {
        return WatchAllResponseDto.builder()
                .watchList(responseDtoList)
                .build();
    }
}
