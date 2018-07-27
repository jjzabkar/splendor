package com.jjz.splendor.jjzsplendor.game.action;

import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.Data;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjzabkar on 2018-07-26.
 */
public class PurchaseCardAction implements TurnAction {
    @Getter
    private final DevelopmentCard card;
    @Getter
    private final List<GemColor> coins;
    @Getter
    private final Player player;

    public PurchaseCardAction(Player p, DevelopmentCard c) {
        this.player = p;
        this.card = c;
        this.coins = new LinkedList<>();
    }

    public PurchaseCardAction(Player p, DevelopmentCard c, List<GemColor> gems) {
        this(p, c);
        this.coins.addAll(gems);
    }

    @Override
    public ActionType getActionType() {
        return ActionType.PURCHASE_CARD;
    }

    @Override
    public String toActionString() {
        return String.format("purchase a %s card worth %s prestige: %s", card.getGem(), card.getPrestigePoints(), card.toString());
    }

}
