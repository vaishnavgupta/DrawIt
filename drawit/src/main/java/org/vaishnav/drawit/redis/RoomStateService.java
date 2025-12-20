package org.vaishnav.drawit.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RoomStateService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String generateKey(String roomId, String suffix){
        return "room:" + roomId + ":" + suffix;
    }

    public void addPlayer(String roomId, String username){
        redisTemplate.opsForSet()
                .add(generateKey(roomId, "players"), username);
    }

    public void removePlayer(String roomId, String username){
        redisTemplate.opsForSet()
                .remove(generateKey(roomId, "players"), username);
    }

    public Set<Object> getPlayers(String roomId){
        return redisTemplate.opsForSet()
                .members(generateKey(roomId, "players"));
    }

    public void setDrawer(String roomId, String username){
        redisTemplate.opsForValue()
                .set(generateKey(roomId, "drawer"), username);
    }

    public String getDrawer(String roomId){
        return (String) redisTemplate.opsForValue()
                .get(generateKey(roomId, "drawer"));
    }

    public void setWord(String roomId, String word){
        redisTemplate.opsForValue()
                .set(generateKey(roomId, "word"), word);
    }

    public void getWord(String roomId){
        redisTemplate.opsForValue()
                .get(generateKey(roomId, "word"));
    }

    public void setTimer(String roomId, int seconds){
        redisTemplate.opsForValue()
                .set(generateKey(roomId, "timer"), seconds);
    }

    public int decrementTimer(String roomId){
        Long time = redisTemplate.opsForValue()
                .decrement(generateKey(roomId, "timer"));
        return time != null ? time.intValue() : 0;
    }

    public int getTimer(String roomId){
        Integer time = (Integer) redisTemplate.opsForValue()
                .get(generateKey(roomId, "timer"));
        return time != null ? time : 0;
    }

    public void addScore(String roomId, String username, int score){
        redisTemplate.opsForHash()
                .increment(generateKey(roomId, "scores"), username, score);
    }
    
    public Map<Object, Object> getScores(String roomId){
        return redisTemplate.opsForHash()
                .entries(generateKey(roomId, "scores"));
    }

    public boolean markGuessed(String roomId, String username){
        Long added = redisTemplate.opsForSet()
                .add(generateKey(roomId, "guessed"), username);
        return added != null && added == 1;
    }

    public void clearRoom(String roomId) {
        redisTemplate.delete(List.of(
                generateKey(roomId, "players"),
                generateKey(roomId, "drawer"),
                generateKey(roomId, "word"),
                generateKey(roomId, "timer"),
                generateKey(roomId, "scores"),
                generateKey(roomId, "guessed")
        ));
    }

    public void setRoomTTL(String roomId, long minutes) {
        redisTemplate.expire("room:" + roomId, minutes, TimeUnit.MINUTES);
    }
}
