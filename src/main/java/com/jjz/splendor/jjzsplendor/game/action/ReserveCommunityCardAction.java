package com.jjz.splendor.jjzsplendor.game.action;

import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.Getter;

/**
 * Created by jjzabkar on 2018-07-26.
 */
public class ReserveCommunityCardAction implements TurnAction {
    @Getter
    private final Player player;
    @Getter
    private final DevelopmentCard card;

    public ReserveCommunityCardAction(Player p, DevelopmentCard c){
        this.player = p;
        this.card = c;
    }
    @Override
    public ActionType getActionType() {
        return ActionType.RESERVE_COMMUNITY_CARD;
    }

    @Override
    public String toActionString() {
        return String.format("reserve a %s card worth %s prestige: %s", card.getGem(), card.getPrestigePoints(), card.toString());
    }
}
