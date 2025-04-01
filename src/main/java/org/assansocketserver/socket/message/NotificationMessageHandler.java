package org.assansocketserver.socket.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.notification.dto.NotificationDTO;
import org.assansocketserver.domain.notification.service.NotificationService;
import org.assansocketserver.domain.patient.service.PatientSocketService;
import org.assansocketserver.domain.ward.entity.Ward;
import org.assansocketserver.global.common.WebSocketMessage;
import org.assansocketserver.socket.utils.SessionWardMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationMessageHandler implements MessageHandler {

    private final PatientSocketService patientSocketService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    private final SessionWardMapper sessionWardMapper;

    private static final ConcurrentHashMap<String, SessionSender> SESSION_SENDERS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> SESSION_INITIALIZED = new ConcurrentHashMap<>();

    @Override
    public void handleMessage(WebSocketSession session, String cmd, String data) {
        SESSION_SENDERS.putIfAbsent(session.getId(), new SessionSender(session, objectMapper));

        try {
            log.info("cmd: {}", cmd);
            log.info("data: {}", data);
            Map<String, String> fields = objectMapper.readValue(data, new TypeReference<Map<String, String>>() {
            });

            // 메시지에서 받은 토큰을 이용하여 sessionWardMapper에 ward 매핑
            boolean mapped = sessionWardMapper.mapSessionWithWard(session, fields.get("token"));
            if (!mapped) {
                sendErrorMessage(session, "토큰 매핑 실패");
                return;
            }

            Ward ward = sessionWardMapper.getWardBySession(session);

            switch (cmd) {
                case "ALL":
                    sendAllNotifications(session, ward);
                    break;
                case "READ":
                    markNotificationAsRead(session, fields.get("notification_id"));
                    sendAllNotifications(session, ward);
                    break;
                default:
                    log.warn("알 수 없는 Notification cmd: {}", cmd);
            }
        } catch (Exception e) {
            log.error("Notification 메시지 처리 중 에러 발생", e);
            sendErrorMessage(session, "Notification 처리 중 에러 발생");
        }
    }

    public void sendNewNotification(NotificationDTO notificationDTO, Ward ward) {
        // 알림 저장을 한 번만 수행 (저장된 알림 데이터를 재사용)
        NotificationDTO savedNotification = notificationService.addNewNotification(notificationDTO, ward);
        WebSocketMessage<NotificationDTO> newNotification = WebSocketMessage.of("NOTIFICATION_NEW", savedNotification);

        // 해당 ward에 매핑된 모든 세션 가져오기
        Collection<WebSocketSession> sessions = sessionWardMapper.getSessionsByWard(ward);
        if (sessions.isEmpty()) {
            log.info("해당 ward에 매핑된 세션이 없습니다. 알림 전송 생략");
            return;
        }

        for (WebSocketSession session : sessions) {
            String sessionId = session.getId();
            if (!Boolean.TRUE.equals(SESSION_INITIALIZED.get(sessionId))) {
                log.info("세션 {} 은 아직 초기화되지 않았습니다. 알림 전송 생략", sessionId);
                continue;
            }
            // 신규 알림 전송
            sendMessage(session, newNotification);
            // 전체 알림 목록 갱신 전송
            sendAllNotifications(session, ward);
        }
    }

    public void broadcastNewNotification(NotificationDTO notificationDTO) {
        // 전체 세션을 ward 기준으로 그룹화
        Map<Ward, List<WebSocketSession>> wardSessionsMap = sessionWardMapper.getAllSessions().stream()
                .collect(Collectors.groupingBy(session -> sessionWardMapper.getWardBySession(session)));

        // 각 ward 그룹에 대해 처리
        wardSessionsMap.forEach((ward, sessions) -> {
            // ward가 null인 경우는 무시
            if (ward == null) {
                return;
            }
            // 한 번만 저장
            NotificationDTO savedNotification = notificationService.addNewNotification(notificationDTO, ward);
            WebSocketMessage<NotificationDTO> newNotification = WebSocketMessage.of("NOTIFICATION_NEW",
                    savedNotification);

            // 해당 ward에 속한 모든 세션에 메시지 전송
            sessions.forEach(session -> {
                String sessionId = session.getId();
                if (!Boolean.TRUE.equals(SESSION_INITIALIZED.get(sessionId))) {
                    log.info("세션 {} 은 아직 초기화되지 않았습니다. 알림 전송 생략", sessionId);
                    return;
                }
                sendMessage(session, newNotification);
                sendAllNotifications(session, ward);
            });
        });
    }

    private void sendAllNotifications(WebSocketSession session, Ward ward) {
        List<NotificationDTO> unreadNotifications = notificationService.getAllUnreadNotifications(ward);


        System.out.println("::::::::::::::::::::::::Send 받은 시각 (KST): " + Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Seoul")));

        sendMessage(session, patientSocketService.getPatientList(ward));
        sendMessage(session, WebSocketMessage.of("NOTIFICATION_ALL", unreadNotifications));
        SESSION_INITIALIZED.put(session.getId(), true); // 최초 초기화 완료 표시
        
    }

    private void markNotificationAsRead(WebSocketSession session, String notificationId) {
        if (notificationId == null) {
            sendErrorMessage(session, "notification_id가 필요합니다.");
            return;
        }
        boolean success = notificationService.markNotificationAsRead(notificationId);
        if (!success) {
            sendErrorMessage(session, "알림 읽음 처리 실패: " + notificationId);
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        SessionSender sender = SESSION_SENDERS.get(session.getId());
        if (sender != null) {
            sender.send(message);
        }
    }

    private void sendErrorMessage(WebSocketSession session, String errorMessage) {
        WebSocketMessage<String> errorResponse = WebSocketMessage.of("ERROR", errorMessage);
        sendMessage(session, errorResponse);
    }

}
