package org.vaishnav.drawit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuessResultDto {
    private String username;
    private String message;
    private boolean correct;
}
