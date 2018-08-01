package com.jjz.splendor.jjzsplendor.game;

import com.google.common.collect.ImmutableList;
import com.jjz.splendor.jjzsplendor.game.action.PurchaseCardAction;
import com.jjz.splendor.jjzsplendor.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Slf4j
public class CardService {
    private final Random rand;
    private final CoinService coinService;
    @Getter
    public int cardCount;

    @Autowired
    public CardService(CoinService coinService) {
        this.coinService = coinService;
        this.cardCount = -1;
        this.rand = new Random();
    }

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
        int i = 0;
        for (String s : arr) {
            result.add(new DevelopmentCard(i++, s));
        }
        shuffle(result);
        this.cardCount = result.size();
        return result;
    }

    public <T extends Object> void shuffle(List<T> result) {
        log.info("shuffling...");
        T tmp;
        for (int i = 0; i < (result.size() * 7) + Math.abs(rand.nextInt(5111)); i++) {
            int randSpot1 = Math.abs(rand.nextInt(result.size()));
            int randSpot2 = Math.abs(rand.nextInt(result.size()));
            if (randSpot1 != randSpot2) {
                tmp = result.get(randSpot1);
                result.set(randSpot1, result.get(randSpot2));
                result.set(randSpot2, tmp);
            }
        }
        log.info("shuffle complete");
    }

    protected void purchaseCard(PurchaseCardAction pca, Game g) {
        this.purchaseCard(pca.getCard(), pca.getPlayer(), g, pca.getCoins());
    }

    protected void purchaseCard(DevelopmentCard card, Player p, Game g, List<GemColor> coins) {
        log.info("player {} to purchase {} card worth {} pts: {}", p.getMyCounter(), card.getGem(), card.getPrestigePoints(), card.toString());
        log.info("player {} has {} coins {}", p.getMyCounter(), p.getCoins().size(), p.getCoins());
        log.info("player {} has {} purchased cards: ", p.getMyCounter(), p.getPurchasedCards().size());
        for (DevelopmentCard c : p.getPurchasedCards()) {
            log.info("\t {} \t{} \t {}", c.getGem(), c.getPrestigePoints(), c);
        }
        List<List<? extends Purchasable>> list1 = ImmutableList.of(p.getHandCards(), g.getPurchaseableCommunityCards());
        if (card instanceof Noble) {
            list1 = ImmutableList.of(g.getNobles());
        }
        for (List<? extends Purchasable> list2 : list1) {
            if (list2.contains(card)) {
                list2.remove(card);
                p.getPurchasedCards().add(card);
                if (!(card instanceof Noble) && g.getPurchaseableCommunityCards().contains(card)) {
                    Optional<DevelopmentCard> replenished = this.replenishCardLevel(g, card.getLevel());
                }
                if (CollectionUtils.isEmpty(coins)) {
                    // automatically figure out which coins to take, taking non-wildcards first
                    List<GemColor> totalGemCost = card.getTotalGemCost();
                    coinService.moveCoinsFromPlayerToBankForCardCost(p, totalGemCost);
                } else {
                    throw new RuntimeException("not implemented yet");
                }
                return;
            }
        }
        throw new IllegalStateException("card should have been found in list and purchased");
    }

    protected void reserveCard(DevelopmentCard card, Player p, Game g) {
        List<DevelopmentCard> cards = g.getPurchaseableCommunityCards();
        if (card != null) {
            if (cards.contains(card)) {
                cards.remove(card);
                p.getHandCards().add(card);
                replenishCardLevel(g, card.getLevel());
                if (g.getGoldCoins() > 0) {
                    coinService.moveCoinsFromBankToPlayer(ImmutableList.of(GemColor.GOLD), p, g);
                }
            } else {
                Assert.isTrue(false, "expected card to be in community cards: " + card.toString());
            }
        }
    }

    protected Optional<DevelopmentCard> replenishCardLevel(Game g, int level) {
        int tot = g.getUnseenCards().size();
//        int offset = Math.abs(rand.nextInt(tot));
        for (int i = 0; i < tot; i++) {
//            int index =
            DevelopmentCard next = g.getUnseenCards().get((i));
            if (next.getLevel() == (level)) {
                DevelopmentCard newCard = g.getUnseenCards().remove(i);
                g.getPurchaseableCommunityCards().add(newCard);
                log.info("revealed new community card: level={} gem={} pts={}: {}", next.getLevel(), next.getGem(), next.getPrestigePoints(), next.toString());
                return Optional.of(next);
            }
        }
        log.info("unable to replenish card at level {}", level);
        return Optional.empty();
    }

    public void validateCardCounts(Game g) {
        List<DevelopmentCard> allCards = new LinkedList<>(g.getPurchaseableCommunityCards());
        List<Noble> allNobles = new LinkedList<>(g.getNobles());
        allCards.addAll(g.getUnseenCards());
        Set<Integer> allCardIds = new HashSet<>();
        for (Player p : g.getPlayers()) {
            for (DevelopmentCard c : p.getAllCards()) {
                if (!(c instanceof Noble)) {
                    allCards.add(c);
                }
            }
        }
        for (DevelopmentCard c : allCards) {
            if (allCardIds.contains(c.getId())) {
                throw new IllegalStateException("card " + c.getId() + " found twice: " + c);
            } else {
                allCardIds.add(c.getId());
            }
        }
        allCardIds.clear();
        for (Noble n : allNobles) {
            if (allCardIds.contains(n.getId())) {
                throw new IllegalStateException("noble " + n.getId() + " found twice: " + n);
            } else {
                allCardIds.add(n.getId());
            }
        }

        int pcc = g.getPurchaseableCommunityCards().size();
        if (pcc > 12) {
            log.info("PurchaseableCommunityCards:");
            for(DevelopmentCard c: g.getPurchaseableCommunityCards()){
                log.info("\t {}",c);
            }
            throw new IllegalStateException("board has exceeded 12 cards: " + pcc + " found");
        }
    }
}
