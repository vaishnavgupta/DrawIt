package org.vaishnav.drawit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.vaishnav.drawit.dto.GuessResultDto;
import org.vaishnav.drawit.redis.RoomStateService;

@Service
@RequiredArgsConstructor
public class GameService {

    private final SimpMessagingTemplate template;
    private final DrawerService drawerService;
    private final WordsService wordsService;
    private final RoomStateService redisService;
    private final RoundTimerService roundTimerService;
    private final ScoreService scoreService;

    public boolean isDrawer(String roomId, String name) {
        return true;
    }

    public void processGuess(String roomId, String username, String guess) {

        if(!redisService.isRoundActive(roomId)){
            return;
        }

        //Drawer cannot guess
        String drawer = redisService.getDrawer(roomId);
        if(username.equals(drawer)){
            return;
        }

        boolean firstTime = redisService.markGuessed(roomId, username);
        if(!firstTime){         //Not first time
            return;
        }

        //Comparing the word with stored one
        String correctedWord = redisService.getWord(roomId);
        boolean isCorrect = correctedWord.equalsIgnoreCase(guess.trim());

        if(!isCorrect){
            broadcastWrongGuess(roomId, username, guess);
            return;
        }

        handleCorrectGuess(roomId, username);
    }

    private void handleCorrectGuess(String roomId, String username) {
        roundTimerService.stopTimer(roomId);

        int timeLeft = redisService.getTimer(roomId);

        //Score Calculation
        int guesserScore = scoreService.calculateGuessScore(timeLeft, true);
        int drawerScore = scoreService.calculateDrawerScore();

        redisService.addScore(roomId, username, guesserScore);
        redisService.addScore(roomId, redisService.getDrawer(roomId), drawerScore);

        template.convertAndSend(
                "/topic/room/" + roomId + "/guess",
                new GuessResultDto(username, "guessed correctly!", true)
        );

        roundTimerService.endRound(roomId);
    }

    private void broadcastWrongGuess(
            String roomId,
            String username,
            String guess
    ){
        template.convertAndSend(
                "/topic/room/" + roomId + "/guess",
                new GuessResultDto(username, guess, false)
        );
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
