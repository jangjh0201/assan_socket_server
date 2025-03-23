package org.asan.domain.watch.utils;

public class WatchMapper {
    public static org.asan.domain.watch.dto.WatchDTO toDTO(org.asan.domain.watch.entity.Watch entity) {
        return org.asan.domain.watch.dto.WatchDTO.builder()
            .id(entity.getId())
            .uuid(entity.getUuid())
            .build();
    }

    public static org.asan.domain.watch.entity.Watch toEntity(org.asan.domain.watch.dto.WatchDTO dto) {
        return org.asan.domain.watch.entity.Watch.builder()
            .id(dto.getId())
            .uuid(dto.getUuid())
            .build();
    }
}
