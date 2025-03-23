package org.asansocketserver.domain.ward.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import org.asansocketserver.domain.ward.enums.SectorType;

import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectorDTO {
    private Long wardId;
    private Long sectorId;
    private String sectorName;
    @Column(precision = 10, scale = 4)
    private BigDecimal startX;
    @Column(precision = 10, scale = 4)
    private BigDecimal startY;
    @Column(precision = 10, scale = 4)
    private BigDecimal endX;
    @Column(precision = 10, scale = 4)
    private BigDecimal endY;
    private SectorType sectorType;
}
