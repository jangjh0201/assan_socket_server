package org.asansocketserver.domain.position.dto.request;

public record BeaconCountsDTO(
        String sectorName,
        int counts
) {
}