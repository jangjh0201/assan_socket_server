package org.asan.domain.position.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PositionData {
    private String sectorName;
    private String timeStamp;

    public static PositionData of(String sectorName) {
        return PositionData.builder()
                .sectorName(sectorName)
                .timeStamp(String.valueOf(System.currentTimeMillis() / 1000))
                .build();
    }
}
