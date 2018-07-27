package com.jjz.splendor.jjzsplendor.game.action;

import com.jjz.splendor.jjzsplendor.model.Player;

/**
 * the action that a player takes on their turn
 */
public interface TurnAction {
    ActionType getActionType();
    String toActionString();
    Player getPlayer();
}
