package org.asansocketserver.domain.ward.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ImageResponseDto(
        Long imageId,
        String imageName,
        String  imageUrl
) {
    public static ImageResponseDto of(Long imageId, String imageName ,String  imageUrl) {
        return ImageResponseDto.builder()
                .imageId(imageId)
                .imageName(imageName)
                .imageUrl(imageUrl)
                .build();
    }

}
