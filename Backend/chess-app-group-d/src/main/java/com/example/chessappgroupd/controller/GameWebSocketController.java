package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.game.*;
import com.example.chessappgroupd.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameWebSocketController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/{gameId}/game.updateStatus")
    @SendTo("/topic/game/{gameId}/gameStatus")
    public Status updateGameStatus(GameStatusUpdateDTO statusUpdate) {
        Status convertedStatus = Status.valueOf(statusUpdate.getStatus());
        Long convertedGameId = Long.parseLong(statusUpdate.getGameId());
        return  gameService.updateStatus(convertedStatus, convertedGameId);
    }

    @MessageMapping("/game/{gameId}/updateBoardStatus")
    @SendTo("/topic/game/{gameId}/updates")
    public BoardStatusUpdateDTO updateBoardStatus(BoardStatusUpdateDTO boardStatusUpdateDTO) {
        System.out.println("This is my BoardstatusObject");
        System.out.println(boardStatusUpdateDTO.getWhitePlayerTurn());
        gameService.updateBoardStatus(Long.parseLong(boardStatusUpdateDTO.getGameId()), boardStatusUpdateDTO.getFen(), boardStatusUpdateDTO.getCheckMate(), boardStatusUpdateDTO.getRemis(), boardStatusUpdateDTO.getSanMove());
        return new BoardStatusUpdateDTO(boardStatusUpdateDTO.getGameId(), !boardStatusUpdateDTO.getWhitePlayerTurn(), boardStatusUpdateDTO.getFen(), boardStatusUpdateDTO.getCheckMate(), boardStatusUpdateDTO.getRemis(), boardStatusUpdateDTO.getSanMove());
    }

    @MessageMapping("/game/{gameId}/endGame")
    @SendTo("/topic/game/{gameId}/endGame")
    public endGameDTO endGame(endGameDTO endGameDTO) {
        gameService.endGame(Long.parseLong(endGameDTO.getGameId()), endGameDTO.getWinner(), endGameDTO.isDraw());
        return endGameDTO;
    }

/*  Not working yet
    @MessageMapping("/game/{gameId}/pauseOrResume")
    @SendTo("/topic/game/{gameId}/pauseOrResume")
    public PauseOrResumeDTO pauseOrResumeGame(PauseOrResumeDTO dto) {

        gameService.pauseOrResumeGame(Long.parseLong(dto.getGameId()), dto.getPlayerColor(), dto.isPause());

        return (new PauseOrResumeDTO(dto.getGameId(), dto.getPlayerColor(), dto.isPause()));
    }
*/
}
