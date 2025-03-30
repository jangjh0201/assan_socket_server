package org.assansocketserver.socket.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
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

    // 세션 ID와 Ward 정보를 보관하는 맵
    private static final ConcurrentHashMap<String, Ward> SESSION_WARD_MAPPING = new ConcurrentHashMap<>();
    // 세션 ID와 WebSocketSession을 보관하는 맵
    private static final ConcurrentHashMap<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    public boolean mapSessionWithWard(WebSocketSession session, String token) throws Exception {
        if (token == null) {
            log.error("token이 존재하지 않습니다.");
            return false;
        }
        String username = jwtUtil.getUsername(token);
        Account account = accountService.getAccount(username);
        if (account == null) {
            log.error("해당 username에 대한 계정을 찾을 수 없습니다: {}", username);
            return false;
        }
        Ward ward = wardService.getWardByAccount(account);
        if (ward == null) {
            log.error("계정에 매핑된 병동 정보가 없습니다: {}", username);
            return false;
        }
        SESSION_WARD_MAPPING.put(session.getId(), ward);
        SESSIONS.put(session.getId(), session);
        return true;
    }

    public Ward getWardBySession(WebSocketSession session) {
        return SESSION_WARD_MAPPING.get(session.getId());
    }

    /**
     * 지정된 Ward와 매핑된 WebSocketSession을 반환합니다.
     * 만약 여러 세션이 해당 Ward에 매핑되어 있다면, 첫 번째로 찾은 세션을 반환합니다.
     */
    public WebSocketSession getSessionByWard(Ward ward) {
        for (Map.Entry<String, Ward> entry : SESSION_WARD_MAPPING.entrySet()) {
            if (entry.getValue().getId().equals(ward.getId())) {
                return SESSIONS.get(entry.getKey());
            }
        }
        return null;
    }

    public void removeMapping(WebSocketSession session) {
        SESSION_WARD_MAPPING.remove(session.getId());
        SESSIONS.remove(session.getId());
    }
}
