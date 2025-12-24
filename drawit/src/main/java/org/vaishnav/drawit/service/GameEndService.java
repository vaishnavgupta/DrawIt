package org.vaishnav.drawit.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaishnav.drawit.entity.GameMatch;
import org.vaishnav.drawit.entity.PlayerScore;
import org.vaishnav.drawit.redis.RoomStateService;
import org.vaishnav.drawit.repository.GameMatchRepository;
import org.vaishnav.drawit.repository.PlayerScoreRepository;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameEndService {

    private final RoomStateService redisService;
    private final GameMatchRepository gameMatchRepository;
    private final PlayerScoreRepository playerScoreRepository;

    @Transactional
    public void endGame(String roomId){

        //Creating new Game
        GameMatch gameMatch = GameMatch.builder()
                .startTime(LocalDateTime.now().minusMinutes(5))     //HardCoding
                .endTime(LocalDateTime.now())
                .build();

        GameMatch savedGame = gameMatchRepository.save(gameMatch);

        //Fetching results from Redis
        Map<String, Integer> playerScores = redisService.getFinalScores(roomId);

        playerScores.forEach((k, v) -> {
            PlayerScore playerScore = PlayerScore.builder()
                    .score(v)
                    .username(k)
                    .gameMatch(savedGame)
                    .build();
            playerScoreRepository.save(playerScore);
        });

        //Redis Cleanup
        redisService.clearRoom(roomId);

    }

}
