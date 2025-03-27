package org.assansocketserver.socket.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assansocketserver.global.common.WebSocketMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class SessionSender {

    private final WebSocketSession session;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private final ObjectMapper objectMapper;

    public SessionSender(WebSocketSession session, ObjectMapper objectMapper) {
        this.session = session;
        this.objectMapper = objectMapper;

        Thread senderThread = new Thread(this::processQueue);
        senderThread.setDaemon(true);
        senderThread.start();
    }

    public void send(WebSocketMessage<?> message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            messageQueue.offer(json);
        } catch (Exception e) {
            log.error("메시지 직렬화 실패", e);
        }
    }

    private void processQueue() {
        try {
            while (true) {
                String json = messageQueue.take();
                try {
                    synchronized (session) {
                        if (session.isOpen()) {
                            session.sendMessage(new TextMessage(json));
                        } else {
                            log.warn("세션이 이미 닫혀 있어 메시지 전송 생략: {}", session.getId());
                            break; // 반복문 종료
                        }
                    }
                } catch (IllegalStateException e) {
                    log.warn("세션 상태 오류로 메시지 전송 실패: {}", session.getId(), e);
                    break; // 세션이 닫혔으므로 루프 종료
                } catch (Exception e) {
                    log.error("메시지 전송 중 예외 발생: {}", session.getId(), e);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태 복구
            log.warn("메시지 큐 처리 스레드 인터럽트", e);
        } finally {
            log.warn("세션 전송 종료: {}", session.getId());
        }
    }

}
