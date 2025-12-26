package org.vaishnav.drawit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardEntryDto {
    private String username;
    private Long score;

    public LeaderboardEntryDto(String username, Integer score) {
        this.username = username;
        this.score = score.longValue();
    }

}
