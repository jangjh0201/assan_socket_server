package org.asansocketserver.domain.ward.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WardsDTO {
    private List<Long> imageIds;
    private List<String> imageNames;
}
