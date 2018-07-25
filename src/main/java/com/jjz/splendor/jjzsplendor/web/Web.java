package com.jjz.splendor.jjzsplendor.web;

import com.jjz.splendor.jjzsplendor.game.Game;
import com.jjz.splendor.jjzsplendor.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jjzabkar on 2018-07-24.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class Web {
    private final GameService gameService;

    @GetMapping("/play")
    public Object play(){
        Game g = gameService.newGame();
        boolean winner = g.playRound();
        while(winner ==false){
            winner = g.playRound();
        }
        return "done";
    }
}
