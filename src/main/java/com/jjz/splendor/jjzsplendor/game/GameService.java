package com.jjz.splendor.jjzsplendor.game;

import com.google.common.collect.ImmutableList;
import com.jjz.splendor.jjzsplendor.game.action.*;
import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Noble;
import com.jjz.splendor.jjzsplendor.model.Player;
import com.jjz.splendor.jjzsplendor.players.BuyCheapestCardPlayer;
import com.jjz.splendor.jjzsplendor.players.RandomActionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class GameService {
    public static int END_GAME_PRESTIGE_POINTS = 15;
    private final CardService cardService;
    private final CoinService coinService;
    private final NobleService nobleService;

    @PostConstruct
    public void postConstruct() throws InterruptedException {
        Game g = this.newGame();
        boolean winner = playRound(g);
        while (winner == false) {
            winner = playRound(g);
//            Thread.sleep(100);
        }
    }

    public Game newGame() {
        Player p1 = new BuyCheapestCardPlayer();
        Player p2 = new BuyCheapestCardPlayer();
        Player p3 = new RandomActionStrategy();
        List<Player> players = ImmutableList.of(p1, p2, p3);
        List<DevelopmentCard> cards = cardService.getDevelopmentCards();
        List<Noble> nobles = nobleService.getNobles();
        Game g = new Game(players, cards, nobles);
        for (Player p : players) {
            p.setGame(g);
        }
        return g;
    }

    /**
     * @return true if end game condition met
     */
    public boolean playRound(final Game g) {
        int round = g.getAndIncrementRound();
        log.info("play round {}", round);
        if (round > 1000) {
            throw new RuntimeException("something went wrong. game should have finished before round 1000");
        }
        boolean endGame = false;
        for (Player p : g.getPlayers()) {
            TurnAction action = p.play();
            List<GemColor> discardCoins = new LinkedList<>();
            switch (action.getActionType()) {
                case PURCHASE_CARD:
                    PurchaseCardAction pca = (PurchaseCardAction) action;
                    cardService.purchaseCard(pca, g);
                    break;
                case RESERVE_COMMUNITY_CARD:
                    ReserveCommunityCardAction rca = (ReserveCommunityCardAction) action;
                    log.warn("TODO: add ability to reserve unseen card");
                    cardService.reserveCard(rca.getCard(), p, g);
                    break;
                case DRAW_2_COINS:
                    final Draw2CoinsAction d2 = (Draw2CoinsAction) action;
                    discardCoins = doDraw2CoinsAction(g, d2);
                    break;
                case DRAW_3_COINS:
                    final Draw3CoinsAction d3 = (Draw3CoinsAction) action;
                    discardCoins = doDraw3CoinsAction(g, d3);
                    break;
                default:
                    log.error("invalid action specified: {}", action);
                    break;
            }
            log.info("player {} will {}", p.getMyCounter(), action.toActionString());
            endGame = checkEndGameCondition(g);

            coinService.checkPlayerOverLimit(p, discardCoins, g);

            nobleService.checkVisitFromNoble(p, g);

            log.info("\n");
            log.info("round {}-{} results: player {} has {} prestige, {} development cards, {} reserved cards, and {} coins: {}",
                    round, p.getMyCounter(), p.getMyCounter(), p.getPrestige(), p.getPurchasedCards().size(), p.getHandCards().size(), p.getCoins().size(), p.getCoins()
            );
            for (Player p2 : g.getPlayers()) {
                log.info("player {} ({}) has:", p2.getMyCounter(), p2.getClass().getSimpleName());
                log.info("\t {} prestige points", p2.getPrestige());
                log.info("\t {} coins:", p2.getCoins().size());
                log.info("\t\t {}", p2.getCoins());
                log.info("\t {} purchased cards:", p2.getPurchasedCards().size());
                for (DevelopmentCard c : p2.getPurchasedCards()) {
                    log.info("\t\t {}", c);
                }
                log.info("\t {} reserved cards in hand:", p2.getHandCards().size());
                for (DevelopmentCard c : p2.getHandCards()) {
                    log.info("\t\t {}", c);
                }
            }
            log.info("board has {} cards:  {}", g.getPurchaseableCommunityCards().size(), g.getPurchaseableCommunityCards());
            for (DevelopmentCard c : g.getPurchaseableCommunityCards()) {
                log.info("\t {}", c);
            }
            log.info("board has {} coins:  {}", g.getCoins().size(), g.getCoins());
            log.info("board has {} nobles: {}", g.getNobles().size(), g.getNobles());
            log.info("PurchaseableCommunityCards:");
            for (Noble c : g.getNobles()) {
                log.info("\t {}", c);
            }
            log.info("\n\n");

            coinService.validateCoinCounts(g);
            cardService.validateCardCounts(g);
        }
        coinService.validateCoinCounts(g);
        cardService.validateCardCounts(g);

        return endGame;
    }

    private List<GemColor> doDraw2CoinsAction(Game g, Draw2CoinsAction d2) {
        Player p = d2.getPlayer();
        List<GemColor> discardCoins;
        discardCoins = d2.getDiscardGems();
        ImmutableList<GemColor> proposedGems = ImmutableList.copyOf(d2.getGems());
        log.info("\t proposed coins: {}", proposedGems);
        log.info("\t bank coins    : {}", g.getCoins());
        List<GemColor> actualGems = new LinkedList<>();
        for (GemColor gem : proposedGems) {
            if (gem != null) {
                if (g.getCoins().contains(gem)) {
                    actualGems.add(gem);
                } else {
                    log.warn("bank does not contain a {} coin", gem);
                }
            }
        }
        log.warn("\t TODO: account for discardCoins: {}", discardCoins);
        coinService.moveCoinsFromBankToPlayer(actualGems, p, g);
        return actualGems;
    }

    private List<GemColor> doDraw3CoinsAction(Game g, Draw3CoinsAction d3) {
        List<GemColor> discardCoins;
        discardCoins = d3.getDiscardGems();
        Player p = d3.getPlayer();
        ImmutableList<GemColor> proposedGems = d3.getGems();
        log.info("\t proposed coins: {}", proposedGems);
        log.info("\t bank coins    : {}", g.getCoins());
        List<GemColor> actualGems = new LinkedList<>();
        for (GemColor gem : proposedGems) {
            if (gem != null) {
                if (g.getCoins().contains(gem)) {
                    actualGems.add(gem);
                } else {
                    log.warn("bank does not contain a {} coin", gem);
                }
            }
        }
        log.info("\t moving {} actual coins from bank to player: {}", actualGems.size(), actualGems);
        log.warn("\t TODO: account for discardCoins: {}", discardCoins);
        coinService.moveCoinsFromBankToPlayer(actualGems, p, g);
        return actualGems;
    }

    /**
     * @return {@code true} if end game condition met
     */
    public boolean checkEndGameCondition(final Game g) {
        boolean result = false;
        int winnerCount = 0;
        for (Player p : g.getPlayers()) {
            int prestigePoints = 0;
            List<DevelopmentCard> cards = p.getPurchasedCards();
            for (DevelopmentCard c : cards) {
                prestigePoints += c.getPrestigePoints();
            }
            if (prestigePoints >= END_GAME_PRESTIGE_POINTS) {
                log.warn("TODO: let round finish");
                log.info("player {} ({}) is the winner", p.getMyCounter(), p.getClass().getSimpleName());
                result = true;
                winnerCount++;
            }
        }
        if (winnerCount > 1) {
            throw new RuntimeException("TODO: add tiebreak logic");
        }

        return result;
    }

}
