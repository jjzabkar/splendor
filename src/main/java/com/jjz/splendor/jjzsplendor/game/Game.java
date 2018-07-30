package com.jjz.splendor.jjzsplendor.game;

import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Noble;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.jjz.splendor.jjzsplendor.game.GameService.END_GAME_PRESTIGE_POINTS;
import static com.jjz.splendor.jjzsplendor.model.GemColor.*;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@Data
@Slf4j
public class Game {
    private final List<DevelopmentCard> unseenCards = new LinkedList<>();
    private final List<DevelopmentCard> purchaseableCommunityCards = new LinkedList<>();
    private final List<Noble> nobles = new LinkedList<>();
    private final List<Player> players = new LinkedList<>();
    private int round = 0;
    private int whiteCoins = -1;
    private int blueCoins = -1;
    private int greenCoins = -1;
    private int redCoins = -1;
    private int blackCoins = -1;
    private int goldCoins = 5;
    private int startingChips = -1;

    public Game(List<Player> p, List<DevelopmentCard> cards, List<Noble> nobles) {
        players.addAll(p);
        startingChips = 0;
        if (players.size() == 2)
            startingChips = 4;
        else if (players.size() == 3)
            startingChips = 5;
        else
            startingChips = 7;
        whiteCoins = startingChips;
        blueCoins = startingChips;
        greenCoins = startingChips;
        redCoins = startingChips;
        blackCoins = startingChips;
        log.info("starting game with {} players and {} chips", players.size(), startingChips);
        log.info("game will go to {} prestige points", END_GAME_PRESTIGE_POINTS);
        this.unseenCards.addAll(cards);
        this.nobles.addAll(nobles);

        this.transfer4CardsOfLevel(1, this.unseenCards, this.purchaseableCommunityCards,4);
        this.transfer4CardsOfLevel(2, this.unseenCards, this.purchaseableCommunityCards,8);
        this.transfer4CardsOfLevel(3, this.unseenCards, this.purchaseableCommunityCards,12);
    }

    private void transfer4CardsOfLevel(int i, List<DevelopmentCard> unusedCards, List<DevelopmentCard> purchaseableCards, int limit) {
        Iterator<DevelopmentCard> iterator = unusedCards.iterator();
        while (purchaseableCards.size() < limit) {
            DevelopmentCard dc = iterator.next();
            if (dc.getLevel() == i) {
                purchaseableCards.add(dc);
                unusedCards.remove(dc);
                iterator = unusedCards.iterator();
            }
        }
    }

    public synchronized int getAndIncrementRound(){
        round++;
        return round;
    }

    void changeGemBankCoinCount(GemColor g, int amount) {
        log.debug("change game bank for {} by {}", g, amount);
        switch (g) {
            case RED:
                redCoins += amount;
                Assert.isTrue(redCoins >= 0, "cannot have negative");
                break;
            case BLACK:
                blackCoins += amount;
                Assert.isTrue(blackCoins >= 0, "cannot have negative");
                break;
            case BLUE:
                blueCoins += amount;
                Assert.isTrue(blueCoins >= 0, "cannot have negative");
                break;
            case WHITE:
                whiteCoins += amount;
                Assert.isTrue(whiteCoins >= 0, "cannot have negative");
                break;
            case GOLD:
                goldCoins += amount;
                Assert.isTrue(goldCoins >= 0, "cannot have negative");
                break;
            case GREEN:
                greenCoins += amount;
                Assert.isTrue(greenCoins >= 0, "cannot have negative");
                break;
        }
    }

    public List<DevelopmentCard> getPurchaseableCards(Player p) {
        List<DevelopmentCard> result = new LinkedList<>();
        List<DevelopmentCard> candidates = new LinkedList(p.getHandCards());
        candidates.addAll(this.getPurchaseableCommunityCards());
        for (DevelopmentCard c : candidates) {
            if (c.isPurchaseable(p.getBuyingPower())) {
                result.add(c);
            }
        }

        candidates.clear();
        candidates = null;
        return result;
    }

    public void removeCardAndReplace(DevelopmentCard card) {
        Assert.isTrue(this.purchaseableCommunityCards.contains(card),"contain");
        this.purchaseableCommunityCards.remove(card);
        int level = card.getLevel();
        Iterator<DevelopmentCard> iterator = this.unseenCards.iterator();
        log.info("card remove from board:", card);
        while(iterator.hasNext()){
            DevelopmentCard next = iterator.next();
            if(next.getLevel()== level){
                this.purchaseableCommunityCards.add(next);
                this.unseenCards.remove(next);
                log.info("card added to board:", next);
                return;
            }
        }
    }

    public boolean hasXGemsOfColor(int x, GemColor g){
        switch (g) {
            case RED:
                return redCoins >= x;
            case BLACK:
                return blackCoins >= x;
            case BLUE:
                return blueCoins >= x;
            case WHITE:
                return whiteCoins >= x;
            case GOLD:
                return goldCoins >= x;
            case GREEN:
                return greenCoins >= x;
        }
        return false;
    }

    public List<GemColor> getCoins() {
        List<GemColor> result = new LinkedList<>();
        result.addAll(getXCoinsOfColor(redCoins, RED));
        result.addAll(getXCoinsOfColor(blackCoins, BLACK));
        result.addAll(getXCoinsOfColor(blueCoins, BLUE));
        result.addAll(getXCoinsOfColor(whiteCoins, WHITE));
        result.addAll(getXCoinsOfColor(goldCoins, GOLD));
        result.addAll(getXCoinsOfColor(greenCoins, GREEN));
        return result;
    }

    private List<GemColor> getXCoinsOfColor(int x, GemColor g){
        List<GemColor> result = new LinkedList<>();
        for(int i = 0; i < x; i++){
            result.add(g);
        }
        return result;
    }
}
