package com.jjz.splendor.jjzsplendor.game;

import com.google.common.collect.ImmutableList;
import com.jjz.splendor.jjzsplendor.game.action.PurchaseCardAction;
import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Component
@Slf4j
public class CardService {
    private final Random rand = new Random();
    private final CoinService coinService;

    public static final String DEV_CARDS =
            "wc=0&bc=3&gc=0&rc=0&nc=0&pp=0&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=0&gc=0&rc=2&nc=1&pp=0&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=1&gc=1&rc=1&nc=1&pp=0&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=2&gc=0&rc=0&nc=2&pp=0&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=0&gc=4&rc=0&nc=0&pp=1&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=1&gc=2&rc=1&nc=1&pp=0&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=2&gc=2&rc=0&nc=1&pp=0&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=3&bc=1&gc=0&rc=0&nc=1&pp=0&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=1&bc=0&gc=0&rc=0&nc=2&pp=0&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=0&gc=0&rc=0&nc=3&pp=0&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=1&bc=0&gc=1&rc=1&nc=1&pp=0&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=0&gc=2&rc=0&nc=2&pp=0&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=0&gc=0&rc=4&nc=0&pp=1&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=1&bc=0&gc=1&rc=2&nc=1&pp=0&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=1&bc=0&gc=2&rc=2&nc=0&pp=0&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=1&gc=3&rc=1&nc=0&pp=0&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=1;" +
                    "wc=2&bc=1&gc=0&rc=0&nc=0&pp=0&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=0&gc=0&rc=3&nc=0&pp=0&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=1;" +
                    "wc=1&bc=1&gc=0&rc=1&nc=1&pp=0&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=2&gc=0&rc=2&nc=0&pp=0&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=0&gc=0&rc=0&nc=4&pp=1&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=1;" +
                    "wc=1&bc=1&gc=0&rc=1&nc=2&pp=0&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=1&gc=0&rc=2&nc=2&pp=0&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=1;" +
                    "wc=1&bc=3&gc=1&rc=0&nc=0&pp=0&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=1;" +
                    "wc=0&bc=2&gc=1&rc=0&nc=0&pp=0&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=1;" +
                    "wc=3&bc=0&gc=0&rc=0&nc=0&pp=0&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=1;" +
                    "wc=1&bc=1&gc=1&rc=0&nc=1&pp=0&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=1;" +
                    "wc=2&bc=0&gc=0&rc=2&nc=0&pp=0&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=1;" +
                    "wc=4&bc=0&gc=0&rc=0&nc=0&pp=1&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=1;" +
                    "wc=2&bc=1&gc=1&rc=0&nc=1&pp=0&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=1;" +
                    "wc=2&bc=0&gc=1&rc=0&nc=2&pp=0&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=1;" +
                    "wc=1&bc=0&gc=0&rc=1&nc=3&pp=0&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=1;" +
                    "wc=0&bc=0&gc=2&rc=1&nc=0&pp=0&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=1;" +
                    "wc=0&bc=0&gc=3&rc=0&nc=0&pp=0&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=1;" +
                    "wc=1&bc=1&gc=1&rc=1&nc=0&pp=0&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=1;" +
                    "wc=2&bc=0&gc=2&rc=0&nc=0&pp=0&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=1;" +
                    "wc=0&bc=4&gc=0&rc=0&nc=0&pp=1&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=1;" +
                    "wc=1&bc=2&gc=1&rc=1&nc=0&pp=0&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=1;" +
                    "wc=2&bc=2&gc=0&rc=1&nc=0&pp=0&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=1;" +
                    "wc=0&bc=0&gc=1&rc=3&nc=1&pp=0&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=1;" +
                    "wc=0&bc=0&gc=0&rc=5&nc=0&pp=2&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=6&bc=0&gc=0&rc=0&nc=0&pp=3&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=0&gc=3&rc=2&nc=2&pp=1&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=0&gc=1&rc=4&nc=2&pp=2&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=2&bc=3&gc=0&rc=3&nc=0&pp=1&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=0&gc=0&rc=5&nc=3&pp=2&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=5&gc=0&rc=0&nc=0&pp=2&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=6&gc=0&rc=0&nc=0&pp=3&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=2&gc=2&rc=3&nc=0&pp=1&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=2&bc=0&gc=0&rc=1&nc=4&pp=2&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=5&bc=3&gc=0&rc=0&nc=0&pp=2&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=0&gc=5&rc=0&nc=0&pp=2&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=0&gc=6&rc=0&nc=0&pp=3&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=2;" +
                    "wc=2&bc=3&gc=0&rc=0&nc=2&pp=1&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=2;" +
                    "wc=3&bc=0&gc=2&rc=2&nc=0&pp=1&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=2;" +
                    "wc=4&bc=2&gc=0&rc=0&nc=1&pp=2&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=5&gc=3&rc=0&nc=0&pp=2&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=2;" +
                    "wc=0&bc=0&gc=0&rc=0&nc=5&pp=2&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=2;" +
                    "wc=0&bc=0&gc=0&rc=6&nc=0&pp=3&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=2;" +
                    "wc=2&bc=0&gc=0&rc=2&nc=3&pp=1&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=2;" +
                    "wc=1&bc=4&gc=2&rc=0&nc=0&pp=2&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=2;" +
                    "wc=0&bc=3&gc=0&rc=2&nc=3&pp=1&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=2;" +
                    "wc=3&bc=0&gc=0&rc=0&nc=5&pp=2&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=2;" +
                    "wc=0&bc=0&gc=0&rc=0&nc=5&pp=2&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=2;" +
                    "wc=0&bc=0&gc=0&rc=0&nc=6&pp=3&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=2;" +
                    "wc=3&bc=2&gc=2&rc=0&nc=0&pp=1&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=2;" +
                    "wc=0&bc=1&gc=4&rc=2&nc=0&pp=2&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=2;" +
                    "wc=3&bc=0&gc=3&rc=0&nc=2&pp=1&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=2;" +
                    "wc=0&bc=0&gc=5&rc=3&nc=0&pp=2&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=2;" +
                    "wc=0&bc=0&gc=0&rc=0&nc=7&pp=4&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=3;" +
                    "wc=3&bc=0&gc=0&rc=0&nc=7&pp=5&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=3;" +
                    "wc=3&bc=0&gc=0&rc=3&nc=6&pp=4&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=3;" +
                    "wc=0&bc=3&gc=3&rc=5&nc=3&pp=3&wf=1&bf=0&gf=0&rf=0&nf=0&lvl=3;" +
                    "wc=7&bc=0&gc=0&rc=0&nc=0&pp=4&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=3;" +
                    "wc=7&bc=3&gc=0&rc=0&nc=0&pp=5&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=3;" +
                    "wc=6&bc=3&gc=0&rc=0&nc=3&pp=4&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=3;" +
                    "wc=3&bc=0&gc=3&rc=3&nc=5&pp=3&wf=0&bf=1&gf=0&rf=0&nf=0&lvl=3;" +
                    "wc=0&bc=7&gc=0&rc=0&nc=0&pp=4&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=3;" +
                    "wc=0&bc=7&gc=3&rc=0&nc=0&pp=5&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=3;" +
                    "wc=3&bc=6&gc=3&rc=0&nc=0&pp=4&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=3;" +
                    "wc=5&bc=3&gc=0&rc=3&nc=3&pp=3&wf=0&bf=0&gf=1&rf=0&nf=0&lvl=3;" +
                    "wc=0&bc=0&gc=7&rc=0&nc=0&pp=4&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=3;" +
                    "wc=0&bc=0&gc=7&rc=3&nc=0&pp=5&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=3;" +
                    "wc=0&bc=3&gc=6&rc=3&nc=0&pp=4&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=3;" +
                    "wc=3&bc=5&gc=3&rc=0&nc=3&pp=3&wf=0&bf=0&gf=0&rf=1&nf=0&lvl=3;" +
                    "wc=0&bc=0&gc=0&rc=7&nc=0&pp=4&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=3;" +
                    "wc=0&bc=0&gc=0&rc=7&nc=3&pp=5&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=3;" +
                    "wc=0&bc=0&gc=3&rc=6&nc=3&pp=4&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=3;" +
                    "wc=3&bc=3&gc=5&rc=3&nc=0&pp=3&wf=0&bf=0&gf=0&rf=0&nf=1&lvl=3";

    public List<DevelopmentCard> getDevelopmentCards() {
        List<DevelopmentCard> result = new LinkedList<>();
        String[] arr = DEV_CARDS.split("\\;");
        for (String s : arr) {
            String[] tokens = s.split("\\&");
            DevelopmentCard c = new DevelopmentCard();
            for (String token : tokens) {
                if (token.startsWith("lvl")) {
                    c.setLevel(Integer.parseInt(token.split("\\=")[1]));
                } else if (token.startsWith("pp")) {
                    c.setPrestigePoints(Integer.parseInt(token.split("\\=")[1]));

                } else if (token.equals("wf=1")) {
                    c.setGem(GemColor.WHITE);
                } else if (token.equals("bf=1")) {
                    c.setGem(GemColor.BLUE);
                } else if (token.equals("gf=1")) {
                    c.setGem(GemColor.GREEN);
                } else if (token.equals("rf=1")) {
                    c.setGem(GemColor.RED);
                } else if (token.equals("nf=1")) {
                    c.setGem(GemColor.BLACK);

                } else if (token.startsWith("wc")) {
                    c.setWhiteCost(Integer.parseInt(token.split("\\=")[1]));
                } else if (token.startsWith("bc")) {
                    c.setBlueCost(Integer.parseInt(token.split("\\=")[1]));
                } else if (token.startsWith("gc")) {
                    c.setGreenCost(Integer.parseInt(token.split("\\=")[1]));
                } else if (token.startsWith("rc")) {
                    c.setRedCost(Integer.parseInt(token.split("\\=")[1]));
                } else if (token.startsWith("nc")) {
                    c.setBlackCost(Integer.parseInt(token.split("\\=")[1]));
                }

            }

            result.add(c);
        }

        return result;
    }

    protected void purchaseCard(PurchaseCardAction pca, Game g) {
        DevelopmentCard card = pca.getCard();
        Player p = pca.getPlayer();
        log.info("player {} to purchase {} card worth {} pts: {}", p.getMyCounter(), card.getGem(), card.getPrestigePoints(), card.toString());
        log.info("player {} has coins {}", p.getMyCounter(), p.getCoins());
        List<List<DevelopmentCard>> list1 = ImmutableList.of(p.getHandCards(), g.getPurchaseableCommunityCards());
        for (List<DevelopmentCard> list2 : list1) {
            list2.remove(card);
            pca.getPlayer().getPurchasedCards().add(card);
            if (CollectionUtils.isEmpty(pca.getCoins())) {
                // take non-wildcards first
                List<GemColor> totalGemCost = card.getTotalGemCost();
                coinService.moveCoinsFromPlayerToBankForCardCost(p, totalGemCost);
                return;
            } else {
                throw new RuntimeException("not implemented yet");
            }
        }
    }

    protected void reserveCard(DevelopmentCard card, Player p, Game g) {
        List<DevelopmentCard> cards = g.getPurchaseableCommunityCards();
        if (card != null) {
            if (cards.contains(card)) {
                cards.remove(card);
                p.getHandCards().add(card);
                coinService.moveCoinsFromBankToPlayer(ImmutableList.of(GemColor.GOLD), p, g);
                replenishCardLevel(g, card.getLevel());
            } else {
                Assert.isTrue(false, "expected card to be in community cards: " + card.toString());
            }
        }
    }

    protected void replenishCardLevel(Game g, int level) {
        int tot = g.getUnseenCards().size();
        int offset = Math.abs(rand.nextInt(tot));
        for (int i = 0; i < tot; i++) {
            DevelopmentCard next = g.getUnseenCards().get((i + offset) % tot);
            if (next.getLevel() == (level)) {
                g.getUnseenCards().remove(next);
                g.getPurchaseableCommunityCards().add(next);
                log.info("revealed new community card: level={} gem={} pts={}: {}", next.getLevel(), next.getGem(), next.getPrestigePoints(), next.toString());
                return;
            }
        }
        log.info("unable to replinsh card at level {}", level);
    }

    @PostConstruct
    public void postConstruct() {
        this.getDevelopmentCards();
    }
}
