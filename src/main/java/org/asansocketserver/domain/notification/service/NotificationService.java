package org.asansocketserver.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.notification.dto.request.NotificationRequestDto;
import org.asansocketserver.domain.notification.dto.request.response.NotificationResponseDto;
import org.asansocketserver.domain.notification.entity.Notification;
import org.asansocketserver.domain.notification.mongorepository.NotificationMongoRepository;
import org.asansocketserver.domain.watch.entity.Watch;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMongoRepository notificationMongoRepository;

    public void createAndSaveNotification(Watch watch, Long imageId, String prediction, String alarmType) {

        LocalDateTime now = LocalDateTime.now();
        NotificationRequestDto requestDto = NotificationRequestDto.of(
                watch.getId(),
                imageId,
                watch.getName(),
                watch.getHost(),
                prediction,
                alarmType,
                now);

        Notification notification = Notification.createNotification(requestDto);
        notificationMongoRepository.save(notification);

        log.info("알림 저장: " + requestDto);

    }

    public List<NotificationResponseDto> getNotifications(int page, int size, String type, String watchName,
            String watchId, LocalDate startDate, LocalDate endDate, boolean sortAsc) {
        int skip = (page - 1) * size;

        // 지정된 날짜의 시작과 끝을 설정하거나, 날짜가 없으면 오늘 날짜를 사용
        LocalDateTime startOfDay;
        LocalDateTime endOfDay;

        if (startDate != null && endDate != null) {
            startOfDay = startDate.atStartOfDay();
            endOfDay = endDate.atTime(LocalTime.MAX);
        } else {
            LocalDate today = LocalDate.now();
            startOfDay = today.atStartOfDay();
            endOfDay = today.atTime(LocalTime.MAX);
        }

        List<Notification> filteredNotifications = notificationMongoRepository.findAll().stream()
                .filter(notification -> type == null || notification.getAlarmType().equals(type))
                .filter(notification -> watchName == null || notification.getWatchName().equals(watchName))
                .filter(notification -> watchId == null || notification.getWatchId().toString().equals(watchId))
                .filter(notification -> !notification.getTimestamp().isBefore(startOfDay)
                        && !notification.getTimestamp().isAfter(endOfDay))
                .sorted((n1, n2) -> {
                    int comparison = n1.getTimestamp().compareTo(n2.getTimestamp());
                    return sortAsc ? comparison : -comparison;
                })
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());

        return filteredNotifications.stream()
                .map(NotificationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}