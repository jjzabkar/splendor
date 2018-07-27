package com.jjz.splendor.jjzsplendor.game;

import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjzabkar on 2018-07-26.
 */
@Component
@Slf4j
public class CoinService {

    protected void checkPlayerOverLimit(Player p, List<GemColor> discardCoins, Game game) {
        GemColor removed = null;
        if (p.getCoins().size() > 10) {
            int toDiscard = p.getCoins().size() - 10;
            log.info("player {} has {} coins; must discard {}", p.getMyCounter(), p.getCoins().size(), toDiscard);
            // try discardCoins (optional)
            for (GemColor g : discardCoins) {
                boolean stop = false;
                for (Iterator<GemColor> iter = p.getCoins().listIterator(); !stop && iter.hasNext(); ) {
                    GemColor a = iter.next();
                    if (a.equals(g)) {
                        log.debug("remove coin {} from player {}", g, p.getMyCounter());
                        iter.remove();
                        game.changeGemBankCoinCount(a, 1);
                        stop = true;
                    }
                }
            }
            while (p.getCoins().size() > 10) {
                removed = p.getCoins().remove(0);
                log.warn("player {} forced to remove {} coin over 10 limit", p.getMyCounter(), removed);
                game.changeGemBankCoinCount(removed, 1);
            }
        }
    }

    /**
     * @param bankCoinsToPlayer
     * @param player
     * @return the count of coins the player {@code player} has
     */
    protected int moveCoinsFromBankToPlayer(List<GemColor> bankCoinsToPlayer, Player player, Game game) {
        synchronized (player) {
            synchronized (game) {
                for (GemColor g : bankCoinsToPlayer) {
                    this.changePlayerCoinCount(player, g, 1);
                    game.changeGemBankCoinCount(g, -1);
                }
            }
        }
        return player.getCoins().size();
    }

    protected void changeGameCoinCount(Game game, GemColor g, int i) {
        log.info("change game coin count: {} {}{}",g ,(i > 0? "+": ""), i);
        log.debug("\t before: {}", game.getCoins());
        switch(g){
            case RED:
                game.setRedCoins(game.getRedCoins() + i);
                break;
            case BLACK:
                game.setBlackCoins(game.getBlackCoins() + i);
                break;
            case BLUE:
                game.setBlueCoins(game.getBlueCoins() + i);
                break;
            case WHITE:
                game.setWhiteCoins(game.getWhiteCoins() + i);
                break;
            case GOLD:
                game.setGoldCoins(game.getGoldCoins() + i);
                break;
            case GREEN:
                game.setGreenCoins(game.getGreenCoins() + i);
                break;
        }
        log.debug("\t after:  {}", game.getCoins());
    }

    protected void changePlayerCoinCount(Player p, GemColor g, int amount) {
        Assert.notNull(g, "gemcolor required");
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                log.debug("add coin {} to player {}", g, p.getMyCounter());
                p.getCoins().add(g);
            }
        }
        if (amount < 0) {
            for (int i = 0; i < Math.abs(amount); i++) {
                boolean stop = false;
                for (Iterator<GemColor> iter = p.getCoins().listIterator(); !stop && iter.hasNext(); ) {
                    GemColor a = iter.next();
                    if (a.equals(g)) {
                        log.debug("remove coin {} from player {}", g, p.getMyCounter());
                        iter.remove();
                        stop = true;
                    }
                }
            }
        }
    }

    public void moveCoinsFromPlayerToBankForCardCost(final Player p, final List<GemColor> srcTotalGemCost) {
        log.info("player {} to spend: {}", p.getMyCounter(), srcTotalGemCost);
        List<GemColor> totalGemCost = new LinkedList<>(srcTotalGemCost);
        List<GemColor> playerCoins = p.getCoins();
        log.debug("player {} has     : {}", p.getMyCounter(), p.getCoins());
        // apply discount
        log.debug("before discount cost is {}", totalGemCost);
        for(DevelopmentCard c: p.getPurchasedCards()){
            log.debug("\t discount: {}", c.getGem());
            this.removeCoinFromCollecton(totalGemCost, c.getGem());
        }
        log.info("player {} has discounts: {}", p.getMyCounter(), totalGemCost);
        int before = playerCoins.size();
        //TODO: check first
        for (GemColor g : totalGemCost) {
           removeCoin(playerCoins, g, p.getGame());
        }
        int after = playerCoins.size();
        log.debug("before = {}, after = {}, srcTotalGemCost = {}", before, after, srcTotalGemCost.size());
    }

    private GemColor removeCoin(List<GemColor> playerCoins, GemColor g, Game game) {
        log.info("remove coin {} from playercoins ({})", g, playerCoins);
        GemColor result = removeCoinFromCollecton(playerCoins, g);
        if(result != null){
            this.changeGameCoinCount(game, result, 1);
            return result;
        }
        log.info("\t did not find {} coin; spend GOLD", g);
        result = removeCoinFromCollecton(playerCoins, GemColor.GOLD);
        if(result != null){
            this.changeGameCoinCount(game, result, 1);
            return result;
        }
        throw new RuntimeException("mmkay bad");
    }

    /**
     *
     * @return color removed
     */
    public GemColor removeCoinFromCollecton(List<GemColor> list, GemColor toRemove){
        Iterator<GemColor> iterator = list.iterator();
        while (iterator.hasNext()) {
            GemColor next = iterator.next();
            if (toRemove.equals(next)) {
                list.remove(next);
                log.debug("\t remove coin {}", toRemove);
                return toRemove;
            }
        }
        return null;
    }

    public void validateCoinCounts(Game g) {
        List<GemColor> allCoins = new LinkedList(g.getCoins());
        for(Player p: g.getPlayers()){
            allCoins.addAll(p.getCoins());
        }
        for(GemColor c: GemColor.values()) {
            int totalPerColor = (int) allCoins.stream().filter(x -> x.equals(c)).count();
            if (c.equals(GemColor.GOLD)) {
                Assert.isTrue(totalPerColor==5, "expected 5 gold got "+totalPerColor);
            }else{
                Assert.isTrue(totalPerColor==g.getStartingChips(), "expected "+g.getStartingChips()+" got "+totalPerColor);
            }
        }

    }

}
