package com.jjz.splendor.jjzsplendor.game;

import com.jjz.splendor.jjzsplendor.model.GemColor;
import com.jjz.splendor.jjzsplendor.model.Noble;
import com.jjz.splendor.jjzsplendor.model.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjzabkar on 2018-07-28.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class NobleService {
    public final CardService cardService;

    public static final String NOBLE_CARDS =
            "wc=3&bc=3&gc=0&rc=0&nc=3&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble;" +
                    "wc=0&bc=3&gc=3&rc=3&nc=0&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble;" +
                    "wc=3&bc=0&gc=0&rc=3&nc=3&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble;" +
                    "wc=0&bc=0&gc=4&rc=4&nc=0&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble;" +
                    "wc=0&bc=4&gc=4&rc=0&nc=0&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble;" +
                    "wc=0&bc=0&gc=0&rc=4&nc=4&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble;" +
                    "wc=4&bc=0&gc=0&rc=0&nc=4&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble;" +
                    "wc=3&bc=3&gc=3&rc=0&nc=0&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble;" +
                    "wc=0&bc=0&gc=3&rc=3&nc=3&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble;" +
                    "wc=4&bc=4&gc=0&rc=0&nc=0&pp=3&wf=0&bf=0&gf=0&rf=0&nf=0&lvl=Noble";

    public List<Noble> getNobles() {
        List<Noble> result = new LinkedList<>();
        String[] arr = NOBLE_CARDS.split("\\;");
        int i = 0;
        for (String s : arr) {
            result.add(new Noble(i++, s));
        }
        cardService.shuffle(result);
        return result.subList(0, 2);
    }

    public void checkVisitFromNoble(Player p, Game g) {
        List<GemColor> bp = p.getBuyingPower();
        for (Noble n : g.getNobles()) {
            if (n.isPurchaseable(bp)) {
                log.info("player {} to be visited by a noble: {}", p.getMyCounter(), n);
                cardService.purchaseCard(n, p, g, null);
                return;
            }
        }

    }
}
