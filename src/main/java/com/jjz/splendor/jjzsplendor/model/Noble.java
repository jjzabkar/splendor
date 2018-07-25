package com.jjz.splendor.jjzsplendor.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@Builder
@Data
public class Noble {
    private int whiteCost;
    private int blueCost;
    private int greenCost;
    private int redCost;
    private int blackCost;

    private int prestigePoints;
}
