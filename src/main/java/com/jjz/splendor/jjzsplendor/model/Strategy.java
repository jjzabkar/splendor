package com.jjz.splendor.jjzsplendor.model;

import com.jjz.splendor.jjzsplendor.game.Game;

import java.util.List;

/**
 * Created by jjzabkar on 2018-07-24.
 */
public interface Strategy {
    void play(Game game, List<DevelopmentCard> handCards, List<DevelopmentCard> purchasedCards, List<GemColor> coins);
}
