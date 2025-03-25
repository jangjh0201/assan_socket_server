package org.assansocketserver.socket.dispatcher;

import org.assansocketserver.socket.message.MessageHandler;
import org.assansocketserver.socket.message.NotificationMessageHandler;
import org.assansocketserver.socket.message.PatientMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MessageDispatcher {

    // 키: 메시지 타입 접두어, 값: 해당 핸들러
    private final Map<String, MessageHandler> handlerMap = new HashMap<>();

    /**
     * 핸들러 목록을 받아서 초기화
     */
    public MessageDispatcher(List<MessageHandler> handlers) {
        for (MessageHandler handler : handlers) {
            // 간단하게 instanceof로 분기 (실제 프로젝트에서는 어노테이션이나 커스텀 매핑을 고려)
            if (handler instanceof NotificationMessageHandler) {
                handlerMap.put("NOTIFICATION", handler);
            } else if (handler instanceof PatientMessageHandler) {
                handlerMap.put("PATIENT", handler);
            }
            // 추가 핸들러가 있다면 여기서 등록
        }
    }

    /**
     * type (예: "NOTIFICATION_ALL")의 접두어를 분석해 적절한 핸들러에게 위임
     */
    public void dispatch(WebSocketSession session, String type, String data) {
        String key = type.split("_")[0]; // "NOTIFICATION_ALL" → "NOTIFICATION"
        String cmd = type.split("_")[1]; // "NOTIFICATION_ALL" → "ALL"
        MessageHandler handler = handlerMap.get(key);
        if (handler != null) {
            handler.handleMessage(session, cmd, data);
        } else {
            log.info("지원하지 않는 메시지 타입: " + type);
        }
    }
}
