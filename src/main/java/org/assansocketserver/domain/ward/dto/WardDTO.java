package org.assansocketserver.domain.ward.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WardDTO {

    @JsonProperty("ward_id")
    private Long id;

    @JsonProperty("ward_name")
    private String name;
    private String image;
}
