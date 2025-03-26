package org.assansocketserver.socket.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.notification.dto.NotificationDTO;
import org.assansocketserver.domain.notification.service.NotificationService;
import org.assansocketserver.domain.patient.service.PatientSocketService;
import org.assansocketserver.global.common.WebSocketMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationMessageHandler implements MessageHandler {

    private final PatientSocketService patientScheduleService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENT_SESSIONS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, SessionSender> SESSION_SENDERS = new ConcurrentHashMap<>();

    @Override
    public void handleMessage(WebSocketSession session, String cmd, String data) {
        CLIENT_SESSIONS.putIfAbsent(session.getId(), session);
        SESSION_SENDERS.putIfAbsent(session.getId(), new SessionSender(session, objectMapper));

        try {
            log.info("cmd: {}", cmd);
            log.info("data: {}", data);

            switch (cmd) {
                case "ALL":
                    sendAllUnreadNotifications(session);
                    break;
                case "READ":
                    NotificationDTO notificationDTO = objectMapper.readValue(data, NotificationDTO.class);
                    markNotificationAsRead(session, notificationDTO);
                    sendAllUnreadNotifications(session);
                    break;
                default:
                    log.warn("알 수 없는 Notification cmd: {}", cmd);
            }
        } catch (Exception e) {
            log.error("Notification 메시지 처리 중 에러 발생", e);
            sendErrorMessage(session, "Notification 처리 중 에러 발생");
        }
    }

    public void sendNewNotification(NotificationDTO notificationDTO) {
        if (CLIENT_SESSIONS.isEmpty())
            return;

        WebSocketMessage<NotificationDTO> newNotification = WebSocketMessage.of(
                "NOTIFICATION_NEW", notificationService.addNewNotification(notificationDTO));

        CLIENT_SESSIONS.forEach((id, session) -> {
            sendMessage(session, newNotification);
            sendAllUnreadNotifications(session);
            sendMessage(session, patientScheduleService.getPatientList());
        });
    }

    private void sendAllUnreadNotifications(WebSocketSession session) {
        List<NotificationDTO> unreadNotifications = notificationService.getAllUnreadNotifications();
        sendMessage(session, WebSocketMessage.of("NOTIFICATION_ALL", unreadNotifications));
    }

    private void markNotificationAsRead(WebSocketSession session, NotificationDTO notificationDTO) {
        if (notificationDTO.getId() == null) {
            sendErrorMessage(session, "notification_id가 필요합니다.");
            return;
        }
        boolean success = notificationService.markNotificationAsRead(notificationDTO.getId());
        if (!success) {
            sendErrorMessage(session, "알림 읽음 처리 실패: " + notificationDTO.getId());
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

    public void removeSession(WebSocketSession session) {
        CLIENT_SESSIONS.remove(session.getId());
        SESSION_SENDERS.remove(session.getId());
    }
}
