package org.assansocketserver.socket.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.socket.dispatcher.MessageDispatcher;
import org.assansocketserver.socket.utils.SessionWardMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MessageDispatcher messageDispatcher;
    private final SessionWardMapper sessionWardMapper;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessionWardMapper.register(session);
        log.info("새 WebSocket 세션 연결: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        sessionWardMapper.remove(session);
        log.info("WebSocket 세션 종료: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("받은 메시지: {}", payload);

        JsonNode jsonNode = objectMapper.readTree(payload);
        String type = jsonNode.get("type").asText();
        String data = jsonNode.get("data").toString();

        // 타입에 따라 적절한 핸들러로 위임
        messageDispatcher.dispatch(session, type, data);
    }
}
