package org.assansocketserver.domain.ward.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class WardsDTO {
    @JsonProperty("imageIds")
    private List<Long> wardIds;
    @JsonProperty("imageNames")
    private List<String> wardNames;
}
