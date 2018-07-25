package com.jjz.splendor.jjzsplendor;

import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;

import java.util.List;

/**
 * Created by jjzabkar on 2018-07-24.
 */
public class Utils {

    public static int countGemsOfColor(final GemColor desiredColor, final List<GemColor> coins, final List<DevelopmentCard> purchasedCards) {
        int result = 0;
        for (GemColor c : coins) {
            if (c.equals(desiredColor))
                result++;
        }
        for (DevelopmentCard c : purchasedCards) {
            if (c.getGem().equals(desiredColor))
                result++;
        }
        return result;
    }
}
