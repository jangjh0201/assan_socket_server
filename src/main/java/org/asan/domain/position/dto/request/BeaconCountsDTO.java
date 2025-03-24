package org.asan.domain.position.dto.request;

public record BeaconCountsDTO(
        String sectorName,
        int counts
) {
}