package com.jjz.splendor.jjzsplendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class DevelopmentCard implements Purchasable{
    /**
     * the color of the card; used for future discounts.
     */
    private GemColor gem;
    /**
     * 1, 2, 3
     */
    private int level;

    private int prestigePoints;

    private int whiteCost;
    private int blueCost;
    private int greenCost;
    private int redCost;
    private int blackCost;

    public int getTotalCost(){
        return whiteCost + blueCost + greenCost + redCost + blackCost;
    }

    public boolean isPurchaseable(List<GemColor> allColorBuyingPower){
        boolean result = true;
        int goldCount = (int)allColorBuyingPower.stream().filter(x -> x.equals(GemColor.GOLD)).count();
//        log.warn("TODO: account for goldCount={}" , goldCount);
        for(GemColor g : GemColor.values()){
            int currentCost = getCostPerColor(g);
            int have = (int)allColorBuyingPower.stream().filter(x -> x.equals(g)).count();
            if (have < currentCost){
                int need = currentCost - have;
                if(goldCount > need){
                    goldCount -= need;
                }else {
//                    log.info("not purchaseable because not enough {} for card {}", g, this.toString());
                    return false;
                }
            }
        }
        return result;
    }

    public int getCostPerColor(GemColor g){
        switch (g) {
            case RED:
                return redCost;
            case BLACK:
                return blackCost;
            case BLUE:
                return blueCost;
            case WHITE:
                return whiteCost;
            case GREEN:
                return greenCost;
        }
        return 0;
    }


}
