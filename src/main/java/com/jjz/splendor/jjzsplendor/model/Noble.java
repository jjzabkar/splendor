package com.jjz.splendor.jjzsplendor.model;

import lombok.*;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@Data
@EqualsAndHashCode(of = {"id", "whiteCost", "blueCost", "greenCost", "redCost", "blackCost"})
public class Noble extends DevelopmentCard {

    private boolean isNoble = true;

    public Noble(int id, String s) {
        super( id, s);
        String[] tokens = s.split("\\&");
        for (String token : tokens) {
            if (token.startsWith("pp")) {
                this.setPrestigePoints(Integer.parseInt(token.split("\\=")[1]));
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
    }

    @Override
    public String toString() {
        return new StringBuilder("Noble(")
                .append("id=" + getId())
                .append(", prestigePoints=" + getPrestigePoints())
                .append(", whiteCost=" + getWhiteCost())
                .append(", blueCost=" + getBlueCost())
                .append(", greenCost=" + getGreenCost())
                .append(", redCost=" + getRedCost())
                .append(", blackCost=" + getBlackCost())
                .append(")")
                .toString();
    }
}
