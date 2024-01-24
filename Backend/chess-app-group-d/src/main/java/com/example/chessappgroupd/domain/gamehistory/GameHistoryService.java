package com.example.chessappgroupd.domain.gamehistory;

import com.example.chessappgroupd.domain.appUser.AppUserDTO;
import com.example.chessappgroupd.domain.game.Game;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.GameRepository;
import com.example.chessappgroupd.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameHistoryService {
    private final GameRepository gameRepository;
    private final GameHistoryRepository gameHistoryRepository;
    private final AppUserRepository appUserRepository;
    @Value("${app.user.username.placeholder}")
    private String USER_PLACEHOLDER;
    private static final String GAME_HISTORY_NOT_EXISTS = "Game history doesn't exist! KEYWORD: %s";
    private static final String GAME_HISTORY_DELETED = "Game history with Id '%s' was successfully deleted!";
    private static final String GAME_HISTORY_PLAYER_DELETED = "All game histories of player %s was successfully deleted!";
    private static final String M_ADDED_SUCCESSFULLY_M =
            """

            ...........................  %s  .............................
            
            """;
    private static final String M_CREATED_SUCCESSFULLY_M =
            """

            !!!!!!!!!!!!!!!!!!!!!!!!!!!  %s  !!!!!!!!!!!!!!!!!!!!!!!!!!!
            
            """;
    private static final String M_LAST_GAMES_M =
            """

            ___________________________  %s  ___________________________
            
            """;
    private static final String M_LAST_MOVES_M =
            """

            ___________________________  %s  ___________________________
            
            """;
    private static final String CREATED_SUCCESSFULLY = "New game history was created! ID: %s, WHITE: %s, BLACK: %s";
    private static final String ADDED_SUCCESSFULLY = "New BoardStatus was added to game history! ID: %s, WHITE: %s, BLACK: %s";
    private static final String LAST_MOVES = "New BoardStatus was added to game history! ID: %s, WHITE: %s, BLACK: %s";
    private static final String LAST_GAMES = "Searched last three games of %s";
    private GameHistoryDTO convertToDTO(GameHistory gameHistory) {
        return GameHistoryDTO.getDetailsFromGameHistoryDTO(gameHistory);
    }
    public List<GameHistoryDTO> findAll() {
        return gameHistoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public GameHistoryDTO findByGameHistoryId(Long gameId){
            if (gameHistoryRepository.existsByGameHistory(gameId)) {
                return convertToDTO(gameHistoryRepository.findAllGameHistoryByGameHistoryId(gameId));
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(GAME_HISTORY_NOT_EXISTS,gameId));
    }
    public List<GameHistoryDTO> findAllGameHistoryByPlayerUserName(String userName){
        if(appUserRepository.existsByUserName(userName)){
                if (gameHistoryRepository.existsByPlayerUserName(userName)){
                    return gameHistoryRepository.findAllGameHistoryByPlayerUserName(userName).stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList());
                }
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(GAME_HISTORY_NOT_EXISTS,userName));
        }
        throw new UsernameNotFoundException(String.format(AppUserService.USER_NOT_FOUND_MSG, userName));
    }
    //WICHTIG FRONTEND
    public List<GameHistoryDTO> findLastThreeGameHistoriesByPlayerUserName(String userName){
        if(appUserRepository.existsByUserName(userName)){
            if (gameHistoryRepository.existsByPlayerUserName(userName)){
                //Log
                System.out.printf(M_LAST_GAMES_M, String.format(LAST_GAMES, userName));
                return gameHistoryRepository.findLastThreeGameHistoriesByPlayerUserName(userName).stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(GAME_HISTORY_NOT_EXISTS,userName));
        }
        throw new UsernameNotFoundException(String.format(AppUserService.USER_NOT_FOUND_MSG, userName));
    }
    public String deleteOneGameHistoryByGameHistoryId(Long gameId){
            if (gameHistoryRepository.existsByGameHistory(gameId)) {
                gameRepository.deleteById(gameId);
                gameHistoryRepository.deleteById(gameId);
                return String.format(GAME_HISTORY_DELETED, gameId);
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(GAME_HISTORY_NOT_EXISTS,gameId));
    }
    public String deleteOnlyGameHistoryByPlayerUserName(String userName) {
        if(userName.equals(USER_PLACEHOLDER)) {
            return "Data of user with username: '" + userName + "' have to stay in database! Don't delete it's data!";
        }
        if (appUserRepository.existsByUserName(userName)) {
            if (gameHistoryRepository.existsByPlayerUserName(userName)) {
                gameRepository.deleteByPlayerUserName(userName);
                saveGameHistoryWithOneValidUserAndPlaceholder(userName);
                gameHistoryRepository.deleteGameHistoryWithTwoPlaceholder(USER_PLACEHOLDER);
                return String.format(GAME_HISTORY_PLAYER_DELETED, userName);

            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(GAME_HISTORY_NOT_EXISTS, userName));
        }
        throw new UsernameNotFoundException(String.format(AppUserService.USER_NOT_FOUND_MSG, userName));
    }
    private void saveGameHistoryWithOneValidUserAndPlaceholder(String userName) {
        GameHistory historyWithOneValidPlayer = new GameHistory();
        List<GameHistory> histories = gameHistoryRepository.findAllGameHistoryByPlayerUserName(userName);
        for(GameHistory gameHistory : histories) {
            if (gameHistory.getWhite().equals(appUserRepository.findAppUser(userName))) {
                historyWithOneValidPlayer.setGameId(gameHistory.getGameId());
                historyWithOneValidPlayer.setWhite(appUserRepository.findAppUser(USER_PLACEHOLDER));
                historyWithOneValidPlayer.setBlack(gameHistory.getBlack());
                historyWithOneValidPlayer.setWhitePlayerWon(gameHistory.getWhitePlayerWon());
                historyWithOneValidPlayer.setFen(gameHistory.getFen());
                historyWithOneValidPlayer.setMoves(gameHistory.getMoves());
                gameHistoryRepository.save(historyWithOneValidPlayer);
            }else if (gameHistory.getBlack().equals(appUserRepository.findAppUser(userName))) {
                historyWithOneValidPlayer.setGameId(gameHistory.getGameId());
                historyWithOneValidPlayer.setWhite(gameHistory.getWhite());
                historyWithOneValidPlayer.setBlack(appUserRepository.findAppUser(USER_PLACEHOLDER));
                historyWithOneValidPlayer.setWhitePlayerWon(gameHistory.getWhitePlayerWon());
                historyWithOneValidPlayer.setFen(gameHistory.getFen());
                historyWithOneValidPlayer.setMoves(gameHistory.getMoves());
                gameHistoryRepository.save(historyWithOneValidPlayer);
            }
        }
    }
    public void setWinnerAsWhitePlayerIfWon(Game game, String winner){
        //man darf davon ausgehen, dass das Game, dessen history existiert und der User auch
        GameHistory history = gameHistoryRepository.findAllGameHistoryByGameHistoryId(game.getId());
        if (history!=null) {
            history.setWhitePlayerWon(winner.equals(game.getWhite().getUsername()));
            gameHistoryRepository.save(history);
        }


    }
    private void createGameHistory(Game game, String sanMove){
        GameHistory history = new GameHistory();
        history.setGameId(game.getId());
        history.setWhite(game.getWhite());
        history.setBlack(game.getBlack());
        history.setWhitePlayerWon(null);
        history.getFen().add(game.getBoardStatus());
        history.getMoves().add(sanMove);
        //log
        System.out.printf(M_LAST_MOVES_M, String.format(LAST_MOVES, game.getId(), AppUserDTO.getSafeDetailsFromUserDTO(game.getWhite()), AppUserDTO.getSafeDetailsFromUserDTO(game.getBlack())));
        gameHistoryRepository.save(history);
    }
    public void addBoardStatusToGameHistory(Game game, String sanMove){
        if (gameHistoryRepository.existsByGameHistory(game.getId())) {
            GameHistory history = gameHistoryRepository.findAllGameHistoryByGameHistoryId(game.getId());//id of game and history are the same
            history.getFen().add(game.getBoardStatus());
            //log
            System.out.printf(M_ADDED_SUCCESSFULLY_M, String.format(ADDED_SUCCESSFULLY, game.getId(), AppUserDTO.getSafeDetailsFromUserDTO(game.getWhite()), AppUserDTO.getSafeDetailsFromUserDTO(game.getBlack())));
            history.getMoves().add(sanMove);
            //log
            System.out.printf(M_LAST_MOVES_M, String.format(LAST_MOVES, game.getId(), AppUserDTO.getSafeDetailsFromUserDTO(game.getWhite()), AppUserDTO.getSafeDetailsFromUserDTO(game.getBlack())));
            gameHistoryRepository.save(history);
        }else {
            createGameHistory(game,sanMove);
            //log
            System.out.printf(M_CREATED_SUCCESSFULLY_M, String.format(CREATED_SUCCESSFULLY, game.getId(), AppUserDTO.getSafeDetailsFromUserDTO(game.getWhite()), AppUserDTO.getSafeDetailsFromUserDTO(game.getBlack())));
        }
    }
}
