package org.asan.socket.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.asan.auth.jwt.WebSocketFilter;
import org.asan.domain.ward.entity.Ward;
import org.asan.socket.dispatcher.MessageDispatcher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MessageDispatcher messageDispatcher;
    private final WebSocketFilter webSocketFilter;

    // WebSocket 세션 저장 (1:1 통신)
    private static final ConcurrentHashMap<String, WebSocketSession> CLIENT_SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        CLIENT_SESSIONS.put(session.getId(), session);
        log.info("새 WebSocket 세션 연결: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        CLIENT_SESSIONS.remove(session.getId());
        log.info("WebSocket 세션 종료: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("받은 메시지: {}", payload);

        JsonNode jsonNode = objectMapper.readTree(payload);
        String type = jsonNode.get("type").asText();
        String data = jsonNode.get("data").toString();
        // String token = jsonNode.get("data").get("token").toString();

        // Ward ward = webSocketFilter.getWardInfo(token);

        // 타입에 따라 적절한 핸들러로 위임
        messageDispatcher.dispatch(session, type, data);
    }
}
