package org.vaishnav.drawit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawMessage {
    private double x1;
    private double y1;

    private double x2;
    private double y2;

    private String color;
    private int thickness;

    private String type;    // DRAW or CLEAR
}
