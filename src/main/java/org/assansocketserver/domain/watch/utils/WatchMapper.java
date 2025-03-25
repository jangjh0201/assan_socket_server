package org.assansocketserver.domain.watch.utils;

public class WatchMapper {
    public static org.assansocketserver.domain.watch.dto.WatchDTO toDTO(org.assansocketserver.domain.watch.entity.Watch entity) {
        return org.assansocketserver.domain.watch.dto.WatchDTO.builder()
            .id(entity.getId())
            .uuid(entity.getUuid())
            .build();
    }

    public static org.assansocketserver.domain.watch.entity.Watch toEntity(org.assansocketserver.domain.watch.dto.WatchDTO dto) {
        return org.assansocketserver.domain.watch.entity.Watch.builder()
            .id(dto.getId())
            .uuid(dto.getUuid())
            .build();
    }
}
