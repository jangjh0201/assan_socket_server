package org.asansocketserver.domain.ward.dto;

import lombok.AccessLevel;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;

@Builder(access = AccessLevel.PRIVATE)
public record WardResponseDto(
        @JsonProperty("imageId") Long id,
        @JsonProperty("imageName") String name,
        @JsonProperty("imageUrl") String image) {
    public static WardResponseDto of(Long id, String name, String image) {
        return WardResponseDto.builder()
                .id(id)
                .name(name)
                .image(image)
                .build();
    }

}
