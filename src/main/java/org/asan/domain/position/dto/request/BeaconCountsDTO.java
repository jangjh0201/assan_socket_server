package org.asan.domain.position.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BeaconCountsDTO(
                @JsonProperty("position") String sectorName,
                int counts) {
}