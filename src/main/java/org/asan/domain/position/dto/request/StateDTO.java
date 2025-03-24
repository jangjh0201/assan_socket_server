package org.asan.domain.position.dto.request;

public record StateDTO(
                String watchId,
                Long wardId,
                String sectorName,
                Long endTime) {
}
