package com.jjz.splendor.jjzsplendor.game;

import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.GemColor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@Component
@Slf4j
public class CardService {

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
//        Assert.isTrue(arr.length == 90, "should be 90 cards, found "+arr.length);
        for (String s : arr) {
//            log.info("s={}", s);
            String[] tokens = s.split("\\&");
//            Assert.isTrue(tokens.length==11,"should be 11 tokens: "+s+" found "+tokens.length);
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

    @PostConstruct
    public void postConstruct() {
        this.getDevelopmentCards();
    }
}
