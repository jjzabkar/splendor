package com.jjz.splendor.jjzsplendor.game;

import com.google.common.collect.ImmutableList;
import com.jjz.splendor.jjzsplendor.game.action.*;
import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Noble;
import com.jjz.splendor.jjzsplendor.model.Player;
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
            Thread.sleep(100);
        }
    }

    public Game newGame() {
        Player p1 = new RandomActionStrategy();
        Player p2 = new RandomActionStrategy();
        List<Player> players = ImmutableList.of(p1, p2);
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
                    discardCoins = d2.getDiscardGems();
                    coinService.moveCoinsFromBankToPlayer(d2.getGems(), p, g);
                    break;
                case DRAW_3_COINS:
                    final Draw3CoinsAction d3 = (Draw3CoinsAction) action;
                    discardCoins = d3.getDiscardGems();
                    coinService.moveCoinsFromBankToPlayer(d3.getGems(), p, g);
                    break;
                default:
                    log.error("invalid action specified: {}", action);
                    break;
            }
            log.info("player {} will {}", p.getMyCounter(), action.toActionString());
            endGame = checkEndGameCondition(g);

            coinService.checkPlayerOverLimit(p, discardCoins, g);

            nobleService.checkVisitFromNoble(p, g);

            log.info("round {} results: player {} has {} prestige, {} development cards, {} reserved cards, and {} coins: {}",
                    round, p.getMyCounter(), p.getPrestige(), p.getPurchasedCards().size(), p.getHandCards().size(), p.getCoins().size(), p.getCoins()
            );
            log.info("board has {} cards:  {}", g.getPurchaseableCommunityCards().size(), g.getPurchaseableCommunityCards());
            log.info("board has {} coins:  {}", g.getCoins().size(), g.getCoins());
            log.info("board has {} nobles: {}", g.getNobles().size(), g.getNobles());

            coinService.validateCoinCounts(g);
            cardService.validateCardCounts(g);
        }
        coinService.validateCoinCounts(g);
        cardService.validateCardCounts(g);

        return endGame;
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
                log.info("player {} is the winner", p.getMyCounter());
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
