package com.jjz.splendor.jjzsplendor.game.action;

import com.google.common.collect.ImmutableList;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.validation.constraints.AssertTrue;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jjzabkar on 2018-07-26.
 */
public class Draw3CoinsAction implements TurnAction {
    @Getter
    private final Player player;
    @Getter
    private GemColor gem1, gem2, gem3;
    @Getter
    private final ImmutableList<GemColor> gems;
    @Getter
    private Optional<GemColor> discard1 = Optional.empty();
    @Getter
    private Optional<GemColor> discard2 = Optional.empty();
    @Getter
    private Optional<GemColor> discard3 = Optional.empty();
    @Getter
    private List<GemColor> discardGems;

    public Draw3CoinsAction(Player p, List<GemColor> g) {
        this.player = p;
        Assert.isTrue(g != null, "gems required");
        List<GemColor> result = new LinkedList<>();
        if(g.size() > 0) {
            this.gem1 = g.get(0);
            result.add(this.gem1);
        }
        if (g.size() > 1) {
            this.gem2 = g.get(1);
            result.add(this.gem2);
        }
        if (g.size() > 2) {
            this.gem3 = g.get(2);
            result.add(this.gem3);
        }

        gems = ImmutableList.copyOf(result);
        this.discardGems = new LinkedList();
    }

    public Draw3CoinsAction(Player p, List<GemColor> g, Optional<GemColor> discard1, Optional<GemColor> discard2, Optional<GemColor> discard3) {
        this(p, g);
        this.discard1 = discard1;
        this.discard2 = discard2;
        this.discard3 = discard3;
        if (discard1.isPresent()) {
            discardGems.add(discard1.get());
        }
        if (discard2.isPresent()) {
            discardGems.add(discard2.get());
        }
        if (discard3.isPresent()) {
            discardGems.add(discard3.get());
        }
        discardGems = ImmutableList.copyOf(discardGems);
    }

    @Override
    public ActionType getActionType() {
        return ActionType.DRAW_3_COINS;
    }

    @Override
    public String toActionString() {
        return String.format("take 3 coins %s %s %s", gem1, gem2, gem3);
    }

}
