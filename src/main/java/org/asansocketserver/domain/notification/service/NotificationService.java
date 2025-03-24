package org.asansocketserver.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.asan.domain.watch.entity.Watch;
import org.asansocketserver.domain.notification.dto.NotificationRequestDTO;
import org.asansocketserver.domain.notification.entity.Notification;
import org.asansocketserver.domain.notification.mongorepository.NotificationMongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMongoRepository notificationMongoRepository;

    public void createAndSaveNotification(Watch watch, String prediction, String riskName) {

        LocalDateTime now = LocalDateTime.now();
        NotificationRequestDTO requestDto = NotificationRequestDTO.of(
                riskName,
                watch.getPatient().getId(),
                watch.getPatient().getName(),
                watch.getPatient().getWard().getName(),
                prediction,
                now);

        Notification notification = Notification.createNotification(requestDto);
        notificationMongoRepository.save(notification);

        log.info("알림 저장: " + requestDto);

    }

}