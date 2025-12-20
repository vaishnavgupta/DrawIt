package org.vaishnav.drawit.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {

    private final Map<String, String> sessionToUser = new ConcurrentHashMap<>();
    private final Map<String, String> sessionToRoom = new ConcurrentHashMap<>();

    public void register(String sessionId, String username, String roomId){
        sessionToUser.put(sessionId, username);
        sessionToRoom.put(sessionId, roomId);
    }

    public String getUsername(String sessionId){
        return sessionToUser.get(sessionId);
    }

    public String getRoomId(String sessionId){
        return sessionToRoom.get(sessionId);
    }

    public void remove(String sessionId) {
        sessionToUser.remove(sessionId);
        sessionToRoom.remove(sessionId);
    }
}
