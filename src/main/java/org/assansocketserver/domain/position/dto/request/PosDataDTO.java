package org.assansocketserver.domain.position.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PosDataDTO(
                String watchId,
                @JsonProperty("imageId") Long wardId,
                @JsonProperty("position") String sectorName,
                @JsonProperty("beacon_data") List<BeaconDataDTO> beaconData) {
}
