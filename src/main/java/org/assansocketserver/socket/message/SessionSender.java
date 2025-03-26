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
            while (session.isOpen()) {
                String json = messageQueue.take();
                synchronized (session) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (Exception e) {
            log.warn("세션 전송 종료: {}", session.getId(), e);
        }
    }
}
