package org.vaishnav.drawit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.vaishnav.drawit.redis.RoomStateService;

@Service
@RequiredArgsConstructor
public class GameService {

    private final SimpMessagingTemplate template;
    private final DrawerService drawerService;
    private final WordsService wordsService;
    private final RoomStateService redisService;
    private final RoundTimerService roundTimerService;

    public boolean isDrawer(String roomId, String name) {
        return true;
    }

    public String processGuess(String roomId, String message, String name) {
        return "success";
    }

    public void startRound(String roomId) {
        redisService.resetRound(roomId);

        String drawer = drawerService.selectRandomDrawer(roomId);
        String word = wordsService.getRandomWord();

        //Starting round
        notifyRoundStart(roomId);

        //Storing in Redis
        redisService.setWord(roomId, word);
        redisService.setDrawer(roomId, drawer);
        redisService.setTimer(roomId, 65);

        //Sending only to Drawer
        sendWordToDrawer(drawer, word);

        //notify other for drawer & Word length
        notifyDrawerName(roomId, drawer);
        sendWordLength(roomId, word);

        //starting the timer
        roundTimerService.startTimer(roomId);
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

    public void notifyDrawerName(String roomId, String drawer) {
        template.convertAndSend(
                "/topic/room/" + roomId + "/system",
                 drawer + " is drawing now!"
        );
    }

    public void sendWordLength(String roomId, String word) {
        template.convertAndSend(
                "/topic/room/" + roomId + "/system",
                "Word has " + word.length() + " letters"
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
