package org.assansocketserver.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.notification.dto.NotificationDTO;
import org.assansocketserver.domain.notification.entity.Notification;
import org.assansocketserver.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 읽지 않은 모든 알림 조회 (Entity → DTO 변환 후 반환)
     */
    public List<NotificationDTO> getAllUnreadNotifications() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return notificationRepository.findAll().stream()
                .filter(notification -> !notification.isRead()) // 읽지 않은 알림만 필터링
                .map(this::convertToDTO)
                .sorted(Comparator.comparing((NotificationDTO dto) -> {
                    Map<String, Object> data = dto.getData();
                    String timestampStr = (String) data.get("timestamp");
                    return LocalDateTime.parse(timestampStr, formatter);
                }).reversed()) // 내림차순 정렬: 최신 알림이 위로 오도록
                .collect(Collectors.toList());
    }

    /**
     * 특정 알림 읽음 처리
     */
    public boolean markNotificationAsRead(String notificationId) {
        return notificationRepository.findById(notificationId).map(notification -> {
            notification.markAsRead();
            notificationRepository.save(notification);
            return true;
        }).orElse(false);
    }

    /**
     * 새로운 알림 추가
     */
    public NotificationDTO addNewNotification(NotificationDTO notificationDTO) {
        Notification notification = convertToEntity(notificationDTO);
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    /**
     * Entity → DTO 변환 메서드
     */
    private NotificationDTO convertToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .category(notification.getCategory())
                .data(notification.getData())
                .isRead(notification.isRead())
                .build();
    }

    /**
     * DTO → Entity 변환 메서드
     */
    private Notification convertToEntity(NotificationDTO dto) {
        return Notification.builder()
                .id(dto.getId())
                .category(dto.getCategory())
                .data(dto.getData())
                .isRead(dto.isRead())
                .build();
    }
}
