package org.assansocketserver.socket.listener;

import org.assansocketserver.domain.watch.repository.WatchLiveRepository;
import org.assansocketserver.socket.interceptor.StompInterceptor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    private final WatchLiveRepository watchLiveRepository;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Long watchId = (Long) accessor.getSessionAttributes().get("watchId");

        if (watchId != null && !watchId.equals(StompInterceptor.monitoringId)) {
            watchLiveRepository.deleteById(watchId);
            log.info("[FORCE DISCONNECT]:: watchId : {}", watchId);
        }
    }
}
