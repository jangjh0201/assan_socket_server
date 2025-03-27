package org.assansocketserver.socket.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.patient.service.PatientSocketService;
import org.assansocketserver.global.common.WebSocketMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class PatientMessageHandler implements MessageHandler {

    private final ObjectMapper objectMapper;
    private final PatientSocketService patientSocketService;

    // 단일 클라이언트 구독 태스크
    private ScheduledFuture<?> subscription;

    // 주기적으로 태스크를 실행할 스케줄러 (필요에 따라 스레드 풀 사이즈 조정 가능)
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PreDestroy
    public void shutdownScheduler() {
        System.out.println("PatientMessageHandler scheduler shutdown");
        scheduler.shutdownNow();
    }

    @Override
    public void handleMessage(WebSocketSession session, String cmd, String data) {
        try {
            log.info("cmd: {}", cmd);
            log.info("data: {}", data);

            switch (cmd) {
                case "SUBSCRIBE":
                    subscribe(session, data);
                    break;
                case "UNSUBSCRIBE":
                    unsubscribe(session, data);
                    break;
                default:
                    log.warn("알 수 없는 Patients cmd: {}", cmd);
            }
        } catch (Exception e) {
            log.error("Patients 메시지 처리 중 에러 발생", e);
            sendErrorMessage(session, "Patients 처리 중 에러 발생");
        }
    }

    /**
     * 환자 정보 구독 (클라이언트 요청)
     * 클라이언트에게 1000ms 주기로 환자 정보 목록 전송
     */
    private void subscribe(WebSocketSession session, String data) {

        // 기존 구독이 존재하면 해제 후 새로 등록
        if (subscription != null && !subscription.isCancelled()) {
            unsubscribe(session, data);
        }

        subscription = scheduler.scheduleAtFixedRate(() -> {
            try {
                sendMessage(session, patientSocketService.getPatientList());
            } catch (Exception e) {
                log.error("환자 정보 전송 중 에러 발생", e);
            }
        }, 0, 10000, TimeUnit.MILLISECONDS);

        log.info("환자 정보 구독 시작");
    }

    /**
     * 환자 정보 구독 해제 (클라이언트 요청)
     */
    private void unsubscribe(WebSocketSession session, String data) {
        if (subscription != null) {
            subscription.cancel(false);
            subscription = null;
            log.info("환자 정보 구독 해제");
        } else {
            log.warn("구독된 태스크가 존재하지 않음");
        }
    }

    /**
     * WebSocket 메시지 전송
     */
    private void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            if (session.isOpen()) {
                String jsonMessage = objectMapper.writeValueAsString(message);
                session.sendMessage(new org.springframework.web.socket.TextMessage(jsonMessage));
            }
        } catch (Exception e) {
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
