package com.jjz.splendor.jjzsplendor.game;

import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@Data
@Slf4j
public class Game {
    public static int END_GAME_PRESTIGE_POINTS = 1;
    private final List<DevelopmentCard> unseenCards = new LinkedList<>();
    private final List<DevelopmentCard> purchaseableCards = new LinkedList<>();
    private final List<Player> players = new LinkedList<>();
    private int round = 0;
    private int whiteCoins = -1;
    private int blueCoins = -1;
    private int greenCoins = -1;
    private int redCoins = -1;
    private int blackCoins = -1;
    private int goldCoins = 5;

    public Game(List<Player> p, List<DevelopmentCard> cards) {
        players.addAll(p);
        int startingChips = 0;
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

        this.transfer4CardsOfLevel(1, this.unseenCards, this.purchaseableCards,4);
        this.transfer4CardsOfLevel(2, this.unseenCards, this.purchaseableCards,8);
        this.transfer4CardsOfLevel(3, this.unseenCards, this.purchaseableCards,12);
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

    /**
     * @return true if end game condition met
     */
    public boolean playRound() {
        round++;
        log.info("play round {}", round);
        boolean endGame = false;
        for (Player p : players) {
            p.play();
            endGame = checkEndGameCondition();
            log.info("round {} results: player {} has {} prestige, {} coins, {} development cards, and {} reserved cards",
                    round, p.getMyCounter(), p.getPrestige(), p.getCoins().size(), p.getPurchasedCards().size(), p.getHandCards().size()
            );
            log.info("board has {} cards: {}", this.getPurchaseableCards().size(), this.getPurchaseableCards());
        }
        return endGame;
    }

    private boolean checkEndGameCondition() {
        boolean result = false;
        for (Player p : players) {
            int prestigePoints = 0;
            List<DevelopmentCard> cards = p.getPurchasedCards();
            for (DevelopmentCard c : cards) {
                prestigePoints += c.getPrestigePoints();
            }
            if (prestigePoints >= END_GAME_PRESTIGE_POINTS) {
                log.warn("TODO: let round finish");
                log.info("player {} is the winner",p.getMyCounter());
                result = true;
            }
        }

        return result;
    }

    public int getBankGems(final GemColor g) {
        switch (g) {
            case RED:
                return redCoins;
            case BLACK:
                return blackCoins;
            case BLUE:
                return blueCoins;
            case WHITE:
                return whiteCoins;
            case GOLD:
                return goldCoins;
            case GREEN:
                return greenCoins;
        }
        return 0;
    }

    public void changeGemBankCoinCount(GemColor g, int amount) {
        switch (g) {
            case RED:
                redCoins += amount;
                break;
            case BLACK:
                blackCoins += amount;
                break;
            case BLUE:
                blueCoins += amount;
                break;
            case WHITE:
                whiteCoins += amount;
                break;
            case GOLD:
                goldCoins += amount;
                break;
            case GREEN:
                greenCoins += amount;
                break;
        }
    }

    public List<DevelopmentCard> getPurchaseableCards(Player p) {
        List<DevelopmentCard> result = new LinkedList<>();
        List<DevelopmentCard> candidates = new LinkedList(p.getHandCards());
        candidates.addAll(this.getPurchaseableCards());
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
        Assert.isTrue(this.purchaseableCards.contains(card),"contain");
        this.purchaseableCards.remove(card);
        int level = card.getLevel();
        Iterator<DevelopmentCard> iterator = this.unseenCards.iterator();
        log.info("card remove from board:", card);
        while(iterator.hasNext()){
            DevelopmentCard next = iterator.next();
            if(next.getLevel()== level){
                this.purchaseableCards.add(next);
                this.unseenCards.remove(next);
                log.info("card added to board:", next);
                return;
            }
        }
    }
}
