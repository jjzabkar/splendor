package com.jjz.splendor.jjzsplendor.model;

import com.google.common.collect.ImmutableList;
import com.jjz.splendor.jjzsplendor.game.Game;
import com.jjz.splendor.jjzsplendor.game.action.TurnAction;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

    public abstract TurnAction play();

    @Getter
    private int myCounter;

    public Player() {
        this.myCounter = counter++;
    }


    public List<GemColor> getBuyingPower() {
        List<GemColor> result = new LinkedList<>();
        for (DevelopmentCard c : this.getPurchasedCards()) {
            result.add(c.getGem());
        }
        result.addAll(this.getCoins());
        return result;
    }

    public int getPrestige() {
        int result = 0;
        for (DevelopmentCard c : this.getPurchasedCards()) {
            result += c.getPrestigePoints();
        }
        return result;
    }

    public void purchaseCard(DevelopmentCard card) {
        boolean inHand = this.getHandCards().contains(card);
        log.info("player {} has coins in hand: {} [before]", this.getMyCounter(), this.getCoins());
        log.info("player {} has buying power:  {} [before]", this.getMyCounter(), this.getBuyingPower());
        this.getPurchasedCards().add(card);
        if (inHand) {
            this.getHandCards().remove(card);
        } else {
            this.getGame().removeCardAndReplace(card);
        }
        log.info("TODO: COIN ACCOUNTING");
        log.info("player {} purchased card {}", this.getMyCounter(), card);

    }

    public ImmutableList<DevelopmentCard> getAllCards(){
        return ImmutableList.<DevelopmentCard>builder()
                .addAll(this.getHandCards())
                .addAll(this.getPurchasedCards())
                .build();
    }

    public List<DevelopmentCard> getPurchaseableCards(){
        return this.getGame().getPurchaseableCards(this);
    }
}
