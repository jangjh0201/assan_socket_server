package org.assansocketserver.socket.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.assansocketserver.domain.patient.service.PatientSocketService;
import org.assansocketserver.domain.ward.entity.Ward;
import org.assansocketserver.domain.ward.repository.WardRepository;
import org.assansocketserver.global.common.WebSocketMessage;
import org.assansocketserver.socket.utils.SessionWardMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class PatientMessageHandler implements MessageHandler {

    private final WardRepository wardRepository;
    private final ObjectMapper objectMapper;
    private final PatientSocketService patientSocketService;
    private final SessionWardMapper sessionWardMapper;
    private final RedisTemplate<String, Object> redisTemplate; // RedisTemplate 추가

    // ward별 구독 태스크를 관리하기 위한 맵 (key: Ward ID)
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> subscriptions = new ConcurrentHashMap<>();

    // 주기적으로 태스크를 실행할 스케줄러 (필요에 따라 스레드 풀 사이즈 조정 가능)
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    @PreDestroy
    public void shutdownScheduler() {
        log.info("PatientMessageHandler scheduler shutdown");
        scheduler.shutdownNow();
    }

    @Override
    public void handleMessage(WebSocketSession session, String cmd, String data) {
        try {
            log.info("cmd: {}", cmd);
            log.info("data: {}", data);
            Map<String, String> fields = objectMapper.readValue(data, new TypeReference<Map<String, String>>() {
            });
            // 메시지에 포함된 token을 이용해 session과 ward 매핑 시도
            boolean mapped = sessionWardMapper.mapSessionWithWard(session, fields.get("token"));
            if (!mapped) {
                sendErrorMessage(session, "토큰 매핑 실패");
                return;
            }

            switch (cmd) {
                case "SUBSCRIBE":
                    // session에 매핑된 ward 기준 구독 시작
                    subscribe(sessionWardMapper.getWardBySession(session));
                    break;
                case "UNSUBSCRIBE":
                    unsubscribe(sessionWardMapper.getWardBySession(session));
                    break;
                case "_WARD_SUBSCRIBE":
                    // 클라이언트에서 명시적으로 ward_id를 전달하는 경우
                    subscribe(wardRepository.findById(Long.valueOf(fields.get("ward_id"))).orElse(null));
                    break;
                case "_WARD_UNSUBSCRIBE":
                    unsubscribe(wardRepository.findById(Long.valueOf(fields.get("ward_id"))).orElse(null));
                    break;
                default:
                    log.warn("알 수 없는 Patients cmd: {}", cmd);
            }
        } catch (Exception e) {
            log.error("Patients 메시지 처리 중 에러 발생", e);
            sendErrorMessage(null, "Patients 처리 중 에러 발생");
        }
    }

    /**
     * Redis를 사용하여 위급 상태를 조회하는 헬퍼 메서드.
     */
    private boolean isEmergencyActive(Ward ward) {
        String key = "emergency:" + ward.getId();
        Object value = redisTemplate.opsForValue().get(key);
        return Boolean.TRUE.equals(value);
    }

    /**
     * ward 단위 환자 정보 구독
     * 주기적으로 해당 ward에 매핑된 모든 세션에 환자 정보 전송
     */
    private void subscribe(Ward ward) {
        if (ward == null) {
            log.error("구독할 ward가 null입니다.");
            return;
        }
        // 이미 해당 ward에 대해 구독 작업이 실행 중이면 재실행하지 않음
        subscriptions.computeIfAbsent(ward.getId(), id -> {
            log.info("ward {}에 대한 환자 정보 구독 시작", id);
            return scheduler.scheduleAtFixedRate(() -> {
                try {
                    Collection<WebSocketSession> sessions = sessionWardMapper.getSessionsByWard(ward);
                    // Redis에서 위급 상태 조회
                    boolean emergency = isEmergencyActive(ward);
                    sessions.forEach(session -> {
                        sendMessage(session, patientSocketService.getPatientList(ward, emergency));
                    });
                } catch (Exception e) {
                    log.error("환자 정보 전송 중 에러 발생", e);
                }
            }, 0, 10000, TimeUnit.MILLISECONDS);
        });
    }

    /**
     * 환자 정보 구독 해제 (ward 단위)
     */
    private void unsubscribe(Ward ward) {
        if (ward == null) {
            log.error("해지할 ward가 null입니다.");
            return;
        }
        ScheduledFuture<?> future = subscriptions.remove(ward.getId());
        if (future != null) {
            future.cancel(false);
            log.info("ward {}에 대한 환자 정보 구독 해제", ward.getId());
        } else {
            log.warn("ward {}에 대해 구독된 태스크가 존재하지 않음", ward.getId());
        }
    }

    /**
     * WebSocket 메시지 전송
     */
    private void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            if (session != null && session.isOpen()) {
                TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(message));
                session.sendMessage(textMessage);
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
