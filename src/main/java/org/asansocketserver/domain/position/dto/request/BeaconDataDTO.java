package org.asansocketserver.domain.position.dto.request;

public record BeaconDataDTO(
                String bssid,
                int rssi) {
}
