package org.asansocketserver.domain.image.dto;


import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LabelDataDTO {

    private Long wardId;
    private String sectorName;
    private BigDecimal startX;
    private BigDecimal startY;
    private BigDecimal endX;
    private BigDecimal endY;


}

