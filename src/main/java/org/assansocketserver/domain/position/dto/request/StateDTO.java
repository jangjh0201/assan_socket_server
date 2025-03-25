package org.assansocketserver.domain.position.dto.request;

public record StateDTO(
        String watchId,
        Long imageId,
        String position,
        Long endTime) {
}
