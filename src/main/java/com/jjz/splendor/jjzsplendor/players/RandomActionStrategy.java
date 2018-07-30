package com.jjz.splendor.jjzsplendor.players;

import com.jjz.splendor.jjzsplendor.game.Game;
import com.jjz.splendor.jjzsplendor.game.action.*;
import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.jjz.splendor.jjzsplendor.game.action.ActionType.PURCHASE_CARD;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@NoArgsConstructor
@Slf4j
public class RandomActionStrategy extends Player {
    private Random r = new Random();

    @Override
    public TurnAction play() {
        ActionType a = ActionType.values()[Math.abs(r.nextInt(ActionType.values().length))];
        boolean canReserveCards = this.getHandCards().size() < 3;
        List<DevelopmentCard> purchaseableCards = this.getGame().getPurchaseableCards(this);
        Optional<? extends TurnAction> turnAction = Optional.empty();
        log.info("player {} will do {}", this.getMyCounter(), a);
        switch (a) {
            case DRAW_2_COINS:
                turnAction = draw2RandomCoins();
                break;
            case RESERVE_COMMUNITY_CARD:
                turnAction = reserveRandomCard(canReserveCards);
                break;
            case PURCHASE_CARD:
                turnAction = purachaseRandomCard(purchaseableCards);
                break;
            default:
                break;
        }
        // default: DRAW_3_COINS
        if (turnAction.isPresent()) {
            return turnAction.get();
        } else {
            return draw3RandomCoins();
        }
    }

    private Optional<PurchaseCardAction> purachaseRandomCard(List<DevelopmentCard> pCards) {
        if (CollectionUtils.isEmpty(pCards)) {
            return Optional.empty();
        } else {
            int offset = Math.abs(r.nextInt(pCards.size()));
            return Optional.of(new PurchaseCardAction(this, pCards.get((offset))));
        }
    }

    private Optional<ReserveCommunityCardAction> reserveRandomCard(boolean canReserveCards) {
        List<DevelopmentCard> cards = this.getGame().getPurchaseableCommunityCards();
        if (!canReserveCards || cards.size() == 0)
            return Optional.empty();
        return Optional.of(new ReserveCommunityCardAction(this, cards.get(Math.abs(r.nextInt(cards.size() - 1)))));
    }

    protected Optional<Draw2CoinsAction> draw2RandomCoins() {
        int s = Math.abs(r.nextInt());
        for (int i = 0; i < GemColor.values().length; i++) {
            GemColor g = GemColor.values()[(s + i - 1) % (GemColor.values().length - 1)]; // omit gold
            if (this.getGame().hasXGemsOfColor(2, g)) {
                return Optional.of(new Draw2CoinsAction(this, g));
            }
        }
        return Optional.empty();
    }

    protected Draw3CoinsAction draw3RandomCoins() {
        List<GemColor> g = get3RandomCoins(super.getGame());
        return new Draw3CoinsAction(this, g);
    }

    protected List<GemColor> get3RandomCoins(Game g) {
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
        return gemsToDraw;
    }
}
