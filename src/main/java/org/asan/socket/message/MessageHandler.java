package org.asan.socket.message;

import org.springframework.web.socket.WebSocketSession;

public interface MessageHandler {
    /**
     * 클라이언트로부터 받은 메시지의 데이터를 처리하는 메서드
     */
    void handleMessage(WebSocketSession session, String type, String data);

}
