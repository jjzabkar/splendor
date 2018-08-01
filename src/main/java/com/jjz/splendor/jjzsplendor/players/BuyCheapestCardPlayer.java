package com.jjz.splendor.jjzsplendor.players;

import com.google.common.collect.ComparisonChain;
import com.jjz.splendor.jjzsplendor.Utils;
import com.jjz.splendor.jjzsplendor.game.action.*;
import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class BuyCheapestCardPlayer extends Player {

    @Override
    public TurnAction play() {
        if (this.getHandCards().size() < 3) {
            DevelopmentCard cheaphold = findCheapestUnreservedCard();
            log.info("reserve cheapest card: {}", cheaphold.getPrestigePoints(), cheaphold);
            return new ReserveCommunityCardAction(this, cheaphold);
        }

        List<DevelopmentCard> candidates = this.getPurchaseableCards();
        if (!candidates.isEmpty()) {
            log.info("purchase card: {}", candidates.get(0));
            return new PurchaseCardAction(this, candidates.get(0));
        }

        List<DevelopmentCard> allPurchaseable = new LinkedList(candidates);
        allPurchaseable.addAll(this.getHandCards());
        allPurchaseable.sort(byCost);
        for (DevelopmentCard cheapCard : allPurchaseable) {
            log.info("\t cheap card in hand:    {} \t {}", cheapCard.getPrestigePoints(), cheapCard);
            log.info("\t current coins in hand: {} \t {}", this.getCoins().size(), this.getCoins());


            log.info("\t card {}({}) is not purchaseable with {}", cheapCard.getId(), cheapCard.getTotalGemCost(), this.getBuyingPower());
            List<GemColor> gemCost = Utils.getGemCostAfterDiscountGivenBuyingPower(this.getBuyingPower(), cheapCard);
            log.info("\t get remaining gemCost for card [{}]: gemCost={}", gemCost, cheapCard.getId());

            Optional<GemColor> twoOfOneCoor = Optional.empty();
            for (GemColor gem : GemColor.values()) {
                int need = (int) gemCost.stream().filter(x -> x.equals(gem)).count();
                boolean bankHas = this.getGame().getCoins().stream().filter(x -> x.equals(gem)).count() > 1L;
                if (bankHas && need > 1) {
                    log.info("\t need {} more {} coins", need, gem);
                    twoOfOneCoor = Optional.of(gem);
                    break;
                }
            }
            log.info("\t total gems needed: {} \t {}", gemCost.size(), gemCost);
//            boolean atLeastTwoColors = gemCost.stream().collect(Collectors.toSet()).size() > 1;


            if (twoOfOneCoor.isPresent()) {
                log.info("do draw2 action {}", twoOfOneCoor.get());
                return new Draw2CoinsAction(this, twoOfOneCoor.get());
            }

            List<GemColor> set = gemCost.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
            boolean bankHasAtLeastOneOfSet = set.stream().filter((x -> this.getGame().getCoins().contains(x))).findAny().isPresent();


            if (bankHasAtLeastOneOfSet) {
                log.info("bankHasAtLeastOneOfSet; do draw3 action {}", set);
                return new Draw3CoinsAction(this, set);
            }
//            }

            log.info("fell through with card; try next cheapest after {}", cheapCard);
        }
        log.info("still fell through. draw first 3 coins");
        return new Draw3CoinsAction(this, this.getGame().getCoins());
    }

    private DevelopmentCard findCheapestReservedCard() {
        return findCheapestCard(this.getHandCards());
    }

    private DevelopmentCard findCheapestUnreservedCard() {
        return findCheapestCard(this.getGame().getPurchaseableCommunityCards());
    }

    private final Comparator<DevelopmentCard> byCost =
            (DevelopmentCard o1, DevelopmentCard o2) -> ComparisonChain.start()
                    .compare(o1.getTotalCost(), o2.getTotalCost())
                    .compare(o1.getLevel(), o2.getLevel())
                    .compare(o1.getId(), o2.getId())
                    .result();

    private DevelopmentCard findCheapestCard(List<DevelopmentCard> list) {
        LinkedList<DevelopmentCard> pcc = new LinkedList<>(list);
        pcc.sort(byCost);
        return pcc.remove();
    }
}
