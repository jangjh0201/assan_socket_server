package org.assansocketserver.domain.watch.utils;

import org.assansocketserver.domain.watch.dto.WatchDTO;
import org.assansocketserver.domain.watch.entity.Watch;

public class WatchMapper {
    public static WatchDTO toDTO(Watch entity) {
        return org.assansocketserver.domain.watch.dto.WatchDTO.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .build();
    }

    public static Watch toEntity(WatchDTO dto) {
        return org.assansocketserver.domain.watch.entity.Watch.builder()
                .id(dto.getId())
                .uuid(dto.getUuid())
                .build();
    }
}
