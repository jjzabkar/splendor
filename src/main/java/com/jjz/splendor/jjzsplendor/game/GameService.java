package com.jjz.splendor.jjzsplendor.game;

import com.google.common.collect.ImmutableList;
import com.jjz.splendor.jjzsplendor.model.DevelopmentCard;
import com.jjz.splendor.jjzsplendor.model.Player;
import com.jjz.splendor.jjzsplendor.players.RandomActionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@RequiredArgsConstructor
@Component
public class GameService {
    private final CardService cardService;

    @PostConstruct
    public void postConstruct() throws InterruptedException {
        Game g = this.newGame();
        boolean winner = g.playRound();
        while(winner ==false){
            winner = g.playRound();
//            Thread.sleep(2000);
        }
    }


    public Game newGame(){
        Player p1 = new RandomActionStrategy();
        Player p2 = new RandomActionStrategy();
        List<Player> players = ImmutableList.of(p1, p2);
        List<DevelopmentCard> cards = cardService.getDevelopmentCards();
        Game g = new Game(players, cards);
        for(Player p : players){
            p.setGame(g);
        }
        return g;
    }
}
