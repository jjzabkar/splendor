package com.jjz.splendor.jjzsplendor.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@Data
@Slf4j
@EqualsAndHashCode(of = {"id", "gem", "level", "whiteCost", "blueCost", "greenCost", "redCost", "blackCost"})
public class DevelopmentCard implements Purchasable {
    private final int id;
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

    /**
     * @param s - parseable string, eg: wc=3&bc=3&gc=5&rc=3&nc=0&pp=3&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=3
     */
    public DevelopmentCard(int id, String s) {
        this.id = id;
        String[] tokens = s.split("\\&");
        boolean isNoble = false;
        for (String token : tokens) {
            if (token.startsWith("lvl")) {
                String value = token.split("\\=")[1];
                if (value.equals("Noble")) {
                    isNoble = true;
                } else {
                    this.setLevel(Integer.parseInt(value));
                }
            } else if (token.startsWith("pp")) {
                this.setPrestigePoints(Integer.parseInt(token.split("\\=")[1]));

            } else if (token.equals("wf=1")) {
                this.setGem(GemColor.WHITE);
            } else if (token.equals("bf=1")) {
                this.setGem(GemColor.BLUE);
            } else if (token.equals("gf=1")) {
                this.setGem(GemColor.GREEN);
            } else if (token.equals("rf=1")) {
                this.setGem(GemColor.RED);
            } else if (token.equals("nf=1")) {
                this.setGem(GemColor.BLACK);

            } else if (token.startsWith("wc")) {
                this.setWhiteCost(Integer.parseInt(token.split("\\=")[1]));
            } else if (token.startsWith("bc")) {
                this.setBlueCost(Integer.parseInt(token.split("\\=")[1]));
            } else if (token.startsWith("gc")) {
                this.setGreenCost(Integer.parseInt(token.split("\\=")[1]));
            } else if (token.startsWith("rc")) {
                this.setRedCost(Integer.parseInt(token.split("\\=")[1]));
            } else if (token.startsWith("nc")) {
                this.setBlackCost(Integer.parseInt(token.split("\\=")[1]));
            }
        }
        if (!isNoble) {
            Assert.isTrue(this.getGem() != null, "gem color required: " + s);
        }
        Assert.isTrue(this.getTotalCost() > 2, "expected cost > 2");
    }

    public int getTotalCost() {
        return whiteCost + blueCost + greenCost + redCost + blackCost;
    }

    public List<GemColor> getTotalGemCost() {
        List<GemColor> result = new LinkedList<>();
        for (GemColor g : GemColor.values()) {
            int tot = this.getCostPerColor(g);
            for (int i = 0; i < tot; i++) {
                result.add(g);
            }
        }
        return result;
    }

    public boolean isPurchaseable(List<GemColor> allColorBuyingPower) {
        boolean result = true;
        int goldCount = (int) allColorBuyingPower.stream().filter(x -> x != null && x.equals(GemColor.GOLD)).count();
        for (GemColor g : GemColor.values()) {
            int currentCost = getCostPerColor(g);
            int have = (int) allColorBuyingPower.stream().filter(x -> x != null && x.equals(g)).count();
            if (have < currentCost) {
                int need = currentCost - have;
                if (goldCount > need) {
                    goldCount -= need;
                } else {
                    return false;
                }
            }
        }
        return result;
    }

    public int getCostPerColor(GemColor g) {
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
