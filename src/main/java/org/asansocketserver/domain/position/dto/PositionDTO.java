package org.asansocketserver.domain.position.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionDTO {
    private Long wardId;
    private Long sectorId;
    private String sectorName;
}
