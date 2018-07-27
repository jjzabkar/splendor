package com.jjz.splendor.jjzsplendor.game.action;

import com.google.common.collect.ImmutableList;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.Data;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jjzabkar on 2018-07-26.
 */
@Getter
public class Draw2CoinsAction implements TurnAction {

    @Getter
    private final GemColor color;
    @Getter
    private final Player player;
    @Getter
    private Optional<GemColor> discard1 = Optional.empty();
    @Getter
    private Optional<GemColor> discard2 = Optional.empty();
    @Getter
    private final List<GemColor> gems;
    @Getter
    private List<GemColor> discardGems;

    public Draw2CoinsAction(Player p, GemColor color) {
        this.player = p;
        this.color = color;
        this.gems = ImmutableList.of(color, color);
        this.discardGems = new LinkedList<>();
    }

    public Draw2CoinsAction(Player p, GemColor colorToDraw, Optional<GemColor> discard1, Optional<GemColor> discard2) {
        this(p, colorToDraw);
        this.discard1 = discard1;
        this.discard2 = discard2;
        if (discard1.isPresent()) {
            discardGems.add(discard1.get());
        }
        if (discard2.isPresent()) {
            discardGems.add(discard2.get());
        }
        discardGems = ImmutableList.copyOf(discardGems);
    }

    @Override
    public ActionType getActionType() {
        return ActionType.DRAW_2_COINS;
    }

    public String toActionString() {
        return String.format("take 2 %s coins", color);
    }
}
