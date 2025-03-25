package org.assansocketserver.socket.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.notification.dto.NotificationDTO;
import org.assansocketserver.domain.notification.service.NotificationService;
import org.assansocketserver.global.common.WebSocketMessage;
import org.assansocketserver.mock.PatientScheduleService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationMessageHandler implements MessageHandler {

    private final PatientScheduleService patientScheduleService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENT_SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void handleMessage(WebSocketSession session, String cmd, String data) {
        CLIENT_SESSIONS.put(session.getId(), session);
        try {
            log.info("cmd: {}", cmd);
            log.info("data: {}", data);

            switch (cmd) {
                case "ALL":
                    // 전체 알림 목록 전송
                    sendAllUnreadNotifications(session);
                    break;
                case "READ":
                    // 알림 읽음 처리 후 갱신된 알림 목록 전송
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
        // 클라이언트 세션이 없으면 알림 전송 로직을 실행하지 않음
        if (CLIENT_SESSIONS.isEmpty()) {
            return;
        }
        WebSocketMessage<NotificationDTO> newNotification = WebSocketMessage.of(
                "NOTIFICATION_NEW", notificationService.addNewNotification(notificationDTO));
        CLIENT_SESSIONS.forEach((sessionId, session) -> sendMessage(session, newNotification));
        CLIENT_SESSIONS.forEach((sessionId, session) -> sendAllUnreadNotifications(session));
        CLIENT_SESSIONS.forEach((sessionId, session) -> sendMessage(session, patientScheduleService.getPatientList()));
    }

    /**
     * 모든 읽지 않은 알림 조회 (클라이언트 요청)
     */
    private void sendAllUnreadNotifications(WebSocketSession session) {
        List<NotificationDTO> unreadNotifications = notificationService.getAllUnreadNotifications();
        sendMessage(session, WebSocketMessage.of("NOTIFICATION_ALL", unreadNotifications));
    }

    /**
     * 특정 알림 읽음 처리 (클라이언트 요청)
     */
    private void markNotificationAsRead(WebSocketSession session, NotificationDTO notificationDTO) {
        if (notificationDTO.getId() == null) {
            sendErrorMessage(session, "notification_id가 필요합니다.");
            return;
        }
        String notificationId = notificationDTO.getId();
        boolean success = notificationService.markNotificationAsRead(notificationId);
        if (!success) {
            sendErrorMessage(session, "알림 읽음 처리 실패: " + notificationId);
        }
    }

    /**
     * WebSocket 메시지 전송
     */
    private void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            if (session.isOpen()) {
                String jsonMessage = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(jsonMessage));
            }
        } catch (IOException e) {
            log.error("메시지 전송 실패", e);
        }
    }

    /**
     * WebSocket 에러 메시지 전송
     */
    private void sendErrorMessage(WebSocketSession session, String errorMessage) {
        WebSocketMessage<String> errorResponse = WebSocketMessage.of("ERROR", errorMessage);
        sendMessage(session, errorResponse);
    }
}
