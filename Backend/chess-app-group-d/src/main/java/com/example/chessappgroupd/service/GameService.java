package com.example.chessappgroupd.service;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.game.BoardStatus;
import com.example.chessappgroupd.domain.game.Game;
import com.example.chessappgroupd.domain.game.Status;
import com.example.chessappgroupd.domain.game.endGameDTO;
import com.example.chessappgroupd.domain.gamehistory.GameHistoryService;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameHistoryService gameHistoryService;

    @Autowired
    private AppUserRepository appUserRepository;

    private final Map<Long, ScheduledFuture<?>> whitePlayerTimers = new ConcurrentHashMap<>();
    private final Map<Long, ScheduledFuture<?>> blackPlayerTimers = new ConcurrentHashMap<>();
    private final Map<Long, ScheduledFuture<?>> pauseTimers = new ConcurrentHashMap<>();


    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void addGame(Game game) {
        gameRepository.save(game);
    }

    public Optional<Game> findById(Long id) {
        return gameRepository.findById(id);
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }

    public List<Game> findGameByWhiteUserName(String username) {
        return gameRepository.findGameByWhiteUserName(username);
    }

    public List<Game> findGameByBlackUserName(String username) {
        return gameRepository.findGameByBlackUserName(username);
    }

    public List<Game> findPendingGamesByWhiteId(String username) {
        return gameRepository.findPendingGamesByWhiteId(username);
    }

    public List<Game> findPlayingGamesByWhiteId(String username) {
        return gameRepository.findPlayingGamesByWhiteId(username);
    }

    public List<Game> findPlayingGamesByBlackId(String username) {
        return gameRepository.findPlayingGamesByBlackId(username);
    }

    @Transactional
    public Status updateStatus(Status status, Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        game.setStatus(status);

        if (status == Status.PLAYING) {
            startOrResumeTimer(gameId, "white");
        }
        if (status == Status.FINISHED) {
            stopTimer(gameId, "white");
            stopTimer(gameId, "black");

        }
        return game.getStatus();
    }

    @Transactional
    public void updateBoardStatus(Long gameId, String fen, Boolean checkMate, Boolean remis, String sanMove) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game with ID " + gameId + " not found"));
        game.setBoardStatus(new BoardStatus(fen));

        //YUU GAME HISTORY
        gameHistoryService.addBoardStatusToGameHistory(game, sanMove);//TODO: move muss noch hin

        //check if the game is remis
        if (remis) {
            endGame(gameId, game.getWhite().getUsername(), true);
            messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/endGame", new endGameDTO(game.getId().toString(), game.getWhite().getUsername(), true));
        }

        // Check if the game is finished
        else if (checkMate && !game.isWhitePlayerTurn()) {
            endGame(gameId, game.getWhite().getUsername(), false);
            messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/endGame", new endGameDTO(game.getId().toString(), game.getWhite().getUsername(), false));
        } else if (checkMate && game.isWhitePlayerTurn()) {
            endGame(gameId, game.getBlack().getUsername(), false);
            messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/endGame", new endGameDTO(game.getId().toString(), game.getBlack().getUsername(), false));
        }

        // Switch the turn and adjust timers accordingly
        if (!game.isWhitePlayerTurn()) {
            stopTimer(gameId, "black");
            startOrResumeTimer(gameId, "white");
        } else {
            stopTimer(gameId, "white");
            startOrResumeTimer(gameId, "black");
        }

        game.setWhitePlayerTurn(!game.isWhitePlayerTurn());
        gameRepository.save(game);
    }

    private void startOrResumeTimer(Long gameId, String playerColor) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        if (game.getStatus() != Status.PLAYING) return;

        Map<Long, ScheduledFuture<?>> targetTimers = playerColor.equals("white") ? whitePlayerTimers : blackPlayerTimers;
        ScheduledFuture<?> scheduledTask = targetTimers.get(gameId);
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = taskScheduler.scheduleAtFixedRate(() -> {
                updateGameTimer(game, playerColor);
            }, 1000);
            targetTimers.put(gameId, scheduledTask);
        }
    }

    @Transactional
    public void updateGameTimer(Game game, String playerColor) {

        Optional<Game> existingGame = gameRepository.findById(game.getId());
        if (existingGame.isEmpty()) {
            // Game is no longer in the database, stop the timer and exit the method
            stopTimer(game.getId(), playerColor);
            return;
        }

        //Decrease the timer
        if (playerColor.equals("white")) {
            game.getTimer().decreaseWhiteTimer();
        } else {
            game.getTimer().decreaseBlackTimer();
        }

        //Check if the timer has reached 0 adn initiate endgame if so
        if (game.getTimer().getWhiteTimerMinutes() == 0 && game.getTimer().getWhiteTimerSeconds() == 0) {
            endGame(game.getId(), game.getBlack().getUsername(), false);
            messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/endGame", new endGameDTO(game.getId().toString(), game.getBlack().getUsername(), false));
        } else if (game.getTimer().getBlackTimerMinutes() == 0 && game.getTimer().getBlackTimerSeconds() == 0) {
            endGame(game.getId(), game.getWhite().getUsername(), false);
            game.setStatus(Status.FINISHED);
            messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/endGame", new endGameDTO(game.getId().toString(), game.getWhite().getUsername(), false));
        }

        gameRepository.updateGameTimer(game.getId(), game.getTimer());
        messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/timer", game.getTimer());

    }


    public void stopTimer(Long gameId, String playerColor) {
        Map<Long, ScheduledFuture<?>> targetTimers = playerColor.equals("white") ? whitePlayerTimers : blackPlayerTimers;
        ScheduledFuture<?> scheduledTask = targetTimers.remove(gameId);
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
        }
    }


    //Needs to be adjusted to work with new points system
    @Transactional
    public void endGame(Long gameId, String winner, Boolean isDraw) {
        stopTimer(gameId, "white");
        stopTimer(gameId, "black");
        Game game = gameRepository.findById(gameId).orElseThrow();

        if (!isDraw) {
            //now whiteValue rest score is also withdrawn from black losing game
            if (winner.equals(game.getWhite().getUsername())) {
                AppUser white = game.getWhite();
                white.setScore(white.getScore() + game.getScoreResult().getValueWhite());
                AppUser black = game.getBlack();
                black.setScore(black.getScore() - game.getScoreResult().getValueWhite());
                appUserRepository.save(white);
                appUserRepository.save(black);
                //YUU GAME HISTORY
                gameHistoryService.setWinnerAsWhitePlayerIfWon(game, winner);
                //
            } else {
                //here also white gets black winning points withdrawn
                AppUser white = game.getWhite();
                white.setScore(white.getScore() - game.getScoreResult().getValueBlack());
                AppUser black = game.getBlack();
                black.setScore(black.getScore() + game.getScoreResult().getValueBlack());
                appUserRepository.save(white);
                appUserRepository.save(black);
                //YUU GAME HISTORY
                gameHistoryService.setWinnerAsWhitePlayerIfWon(game, winner);
                //
            }
        }

        taskScheduler.schedule(() -> {
            messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/gameStatus", Status.FINISHED);
            // Instead of delete set it to finished --> need to adjust get back to game and search only for games that are not finished
            deleteById(game.getId());//so lassen bitte!
        }, new Date(System.currentTimeMillis() + 2000)); // 10000ms = 10 seconds


        gameRepository.save(game);

    }




/*  Not working yet

    @Transactional
    public void pauseOrResumeGame(Long gameId, String playerColor, boolean pause) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        game.setGamePaused(pause);
        gameRepository.save(game);
        if (pause) {
            handlePause(gameId, playerColor, game);
        } else {
            handleResume(gameId, playerColor, game);
        }
    }

    private void handlePause(Long gameId, String playerColor, Game game) {
        if ((playerColor.equals("white") && game.getWhitePlayerPauseCount() >= 2) ||
                (playerColor.equals("black") && game.getBlackPlayerPauseCount() >= 2)) {
            throw new IllegalStateException("Maximum pauses reached");
        }

        stopTimer(gameId, playerColor);
        game.setGamePaused(true);
        incrementPauseCount(game, playerColor);
        schedulePauseEnd(gameId, playerColor);
    }

    private void handleResume(Long gameId, String playerColor, Game game) {
        ScheduledFuture<?> pauseTimer = pauseTimers.remove(gameId);
        if (pauseTimer != null && !pauseTimer.isCancelled()) {
            pauseTimer.cancel(true);
        }
        game.setGamePaused(false);
        startOrResumeTimer(gameId, playerColor);
    }

    private void incrementPauseCount(Game game, String playerColor) {
        if (playerColor.equals("white")) {
            game.setWhitePlayerPauseCount(game.getWhitePlayerPauseCount() + 1);
        } else {
            game.setBlackPlayerPauseCount(game.getBlackPlayerPauseCount() + 1);
        }
    }

    private void schedulePauseEnd(Long gameId, String playerColor) {
        ScheduledFuture<?> pauseTimer = taskScheduler.schedule(() -> {
            handleResume(gameId, playerColor, gameRepository.findById(gameId).orElseThrow());
        }, new Date(System.currentTimeMillis() + 120000)); // 120000ms = 2 minutes

        pauseTimers.put(gameId, pauseTimer);
    }

*/


}
