package org.vaishnav.drawit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final SimpMessagingTemplate template;

    public boolean isDrawer(String roomId, String name) {
        return true;
    }

    public String processGuess(String roomId, String message, String name) {
        return "success";
    }

    public void notifyUserJoined(String roomId, String username) {
        template.convertAndSend(
                "/topic/room/" + roomId + "/system",
                username + " joined the room"
        );
    }

    public void notifyUserLeft(String roomId, String username) {
        template.convertAndSend(
                "/topic/room/" + roomId + "/system",
                username + " left the room"
        );
    }

    public void notifyDrawerLeft(String roomId) {
        template.convertAndSend(
                "/topic/room/" + roomId + "/system",
                "Drawer left the room. Round Ended"
        );
    }

    public void notifyRoundStart(String roomId) {
        template.convertAndSend(
                "/topic/room/" + roomId + "/system",
                "New round started!"
        );
    }

    public void sendWordToDrawer(String username, String word) {
        template.convertAndSendToUser(
                username,
                "/queue/word",
                word
        );
    }
}
