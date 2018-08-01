package com.jjz.splendor.jjzsplendor;

import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;

import java.util.LinkedList;
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

    public static List<GemColor> getGemCostAfterDiscountGivenBuyingPower(List<GemColor> inputBuyingPower, DevelopmentCard card) {
        List<GemColor> cost = card.getTotalGemCost();
        LinkedList<GemColor> buyingPower = new LinkedList(inputBuyingPower);
        List<GemColor> result = new LinkedList<>(cost);
        for (GemColor gem : buyingPower) {
            if (result.contains(gem)) {
                result.remove(gem);
            }
        }
        // now remove gold
        for (GemColor gem : buyingPower) {
            if (gem != null && result != null && gem.equals(GemColor.GOLD) && !result.isEmpty()) {
                result.remove(0);
            }
        }
        return result;
    }

}
