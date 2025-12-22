package org.vaishnav.drawit.service;

import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    public int calculateGuessScore(int timeLeft, boolean firstCorrect){
        int base = 100;
        int timeBonus = timeLeft * 2;
        int firstBonus = firstCorrect ? 0 : 1;

        return base + timeBonus + firstBonus;
    }

    public int calculateDrawerScore() {
        return 100;
    }
}
