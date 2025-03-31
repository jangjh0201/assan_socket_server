package org.assansocketserver.socket.utils;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.assansocketserver.auth.jwt.JWTUtil;
import org.assansocketserver.auth.entity.Account;
import org.assansocketserver.auth.service.AccountService;
import org.assansocketserver.domain.ward.entity.Ward;
import org.assansocketserver.domain.ward.service.WardService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionWardMapper {

    private final JWTUtil jwtUtil;
    private final AccountService accountService;
    private final WardService wardService;

    // WebSocket 세션 저장
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    // WebSocket 세션과 Ward 매핑을 위한 맵
    private final ConcurrentHashMap<String, Ward> sessionWardMap = new ConcurrentHashMap<>();

    public void register(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    public boolean mapSessionWithWard(WebSocketSession session, String token) throws Exception {
        Ward ward = validateTokenAndGetWard(token);
        if (ward != null) {
            sessionWardMap.put(session.getId(), ward);
            return true;
        }
        return false;
    }

    // 특정 세션에 매핑된 Ward 정보 조회
    public Ward getWardBySession(WebSocketSession session) {
        return sessionWardMap.get(session.getId());
    }

    // 특정 Ward에 해당하는 첫번째 세션 반환 (필요 시 구현)
    public WebSocketSession getSessionByWard(Ward ward) {
        return sessions.values().stream()
                .filter(session -> ward.equals(getWardBySession(session)))
                .findFirst().orElse(null);
    }

    // 특정 Ward에 해당하는 모든 세션 반환 (필요 시 구현)
    public Collection<WebSocketSession> getSessionsByWard(Ward ward) {
        return sessions.values().stream()
                .filter(session -> ward.equals(getWardBySession(session)))
                .toList();
    }

    // 전체 세션 조회 (broadcast 등에서 사용)
    public Collection<WebSocketSession> getAllSessions() {
        return sessions.values();
    }

    // 연결 종료 시 세션 및 매핑 제거
    public void remove(WebSocketSession session) {
        sessions.remove(session.getId());
        sessionWardMap.remove(session.getId());
    }

    // 예시용 토큰 검증 및 Ward 반환 로직 (실제 구현에 맞게 수정)
    private Ward validateTokenAndGetWard(String token) {
        if (token == null) {
            log.error("token이 존재하지 않습니다.");
            return null;
        }
        String username = jwtUtil.getUsername(token);
        Account account = accountService.getAccount(username);
        if (account == null) {
            log.error("해당 username에 대한 계정을 찾을 수 없습니다: {}", username);
            return null;
        }
        return wardService.getWardByAccount(account);
    }
}