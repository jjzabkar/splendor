package com.jjz.splendor.jjzsplendor.model;

import com.jjz.splendor.jjzsplendor.game.Game;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjzabkar on 2018-07-24.
 */

@Slf4j
public abstract class Player {
    public static int counter = 0;
    @Getter
    @Setter
    private List<DevelopmentCard> handCards = new LinkedList<>();
    @Getter
    @Setter
    private List<DevelopmentCard> purchasedCards = new LinkedList<>();
    @Getter
    @Setter
    private List<GemColor> coins = new LinkedList<>();
    @Getter
    @Setter
    private Game game;

    public abstract Action play();

    @Getter
    private int myCounter;

    public Player() {
        this.myCounter = counter++;
    }

    //actions
    public void draw3Coins(GemColor c1, GemColor c2, GemColor c3){
        if (c1 != null && c2 != null) {
            Assert.isTrue(!c1.equals(c2),String.format("%s cannot be %s",c1,c2));
            if(c3 != null) {
                Assert.isTrue(!c2.equals(c3), String.format("%s cannot be %s", c2, c3));
                Assert.isTrue(!c1.equals(c3), String.format("%s cannot be %s", c1, c3));
            }
        }

        //TODO: implement

    }

    public void draw2Coins(GemColor g){
        //TODO: implement
    }

    public List<GemColor> getBuyingPower() {
        List<GemColor> result = new LinkedList<>();
        for(DevelopmentCard c: this.getPurchasedCards()){
            result.add(c.getGem());
        }
        result.addAll(this.getCoins());
        return result;
    }

    public int getPrestige() {
        int result = 0;
        for(DevelopmentCard c : this.getPurchasedCards()){
            result += c.getPrestigePoints();
        }
        return result;
    }

    public void purchaseCard(DevelopmentCard card) {
        boolean inHand = this.getHandCards().contains(card);
        log.info("player {} has coins in hand: {} [before]", this.getMyCounter(), this.getCoins());
        log.info("player {} has buying power:  {} [before]", this.getMyCounter(), this.getBuyingPower());
        this.getPurchasedCards().add(card);
        if(inHand){
            this.getHandCards().remove(card);
        }else{
            this.getGame().removeCardAndReplace(card);
        }
        log.info("TODO: COIN ACCOUNTING");
        log.info("player {} purchased card {}",this.getMyCounter(), card);

    }


}
