package com.jjz.splendor.jjzsplendor.players;

import com.jjz.splendor.jjzsplendor.game.Game;
import com.jjz.splendor.jjzsplendor.model.Action;
import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.smartcardio.Card;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@NoArgsConstructor
@Slf4j
public class RandomActionStrategy extends Player {
    private Random r = new Random();

    @Override
    public Action play() {
        int action = r.nextInt(3);
        boolean canReserveCards = this.getHandCards().size() < 3;
        List<DevelopmentCard> purchaseableCards = this.getGame().getPurchaseableCards(this);
        Action result = null;
        if (action == 0) {
            draw3RandomCoins();
            result = Action.DRAW_3_COINS;
        } else if (action == 1) {
            result = Action.DRAW_2_COINS;
        } else if (action == 2 && purchaseableCards.size() > 0) {
            int index = Math.abs(r.nextInt()) % purchaseableCards.size();
            purchaseCard(purchaseableCards.get(index));
            result = Action.PURCHASE_CARD;
        } else if (action == 3 && canReserveCards) {
            result = Action.RESERVE_CARD;
        } else {
            draw3RandomCoins();
            result = Action.DRAW_3_COINS;
        }

        while (this.getCoins().size() > 10) {
            int index = r.nextInt(this.getCoins().size());
            GemColor gem = this.getCoins().remove(index);
            log.info("player {} discarded a {} coin", this.getMyCounter(), gem);
            this.getGame().changeGemBankCoinCount(gem, 1);
        }
        log.info("player {} action was {}", this.getMyCounter(), result);
        return result;
    }


    private void draw3RandomCoins() {
        Game g = super.getGame();
        GemColor[] gems = new GemColor[5];
        gems[0] = g.getBlackCoins() > 0 ? GemColor.BLACK : null;
        gems[1] = g.getBlueCoins() > 0 ? GemColor.BLUE : null;
        gems[2] = g.getGreenCoins() > 0 ? GemColor.GREEN : null;
        gems[3] = g.getRedCoins() > 0 ? GemColor.RED : null;
        gems[4] = g.getWhiteCoins() > 0 ? GemColor.WHITE : null;

        List<GemColor> gemsToDraw = new LinkedList<>();

        int colors = 0;
        int offset = Math.abs(r.nextInt());
        for (int i = 0; i < 5 && colors < 3; i++) {
            int index = (i + offset) % 5;
            if (gems[index] != null) {
                gemsToDraw.add(gems[index]);
                gems[index] = null;
                colors++;
            }
        }

        for (GemColor gc : gemsToDraw) {
            this.getGame().changeGemBankCoinCount(gc, -1);
            log.info("player {} added a {} coin", this.getMyCounter(), gc);
            super.getCoins().add(gc);
        }
    }
}
