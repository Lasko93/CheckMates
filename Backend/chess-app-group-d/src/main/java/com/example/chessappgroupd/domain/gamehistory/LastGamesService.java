/*
DEPRECATED



package com.example.chessappgroupd.domain.gamehistory;

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
public class LastGamesService {
    private final LastGamesRepository lastGamesRepository;
    private final GameRepository gameRepository;
    private final GameHistoryRepository gameHistoryRepository;
    private final AppUserRepository appUserRepository;
    private static final String LAST_GAMES_NOT_EXISTS = "Last Games List of player %s doesn't exist!";
    //private static final String CLUB_MEMBERS_NOT_EXISTS = "There are zero Members in %s!";
    private static final String LAST_GAMES_DELETED = "Last Games List of player %s was successfully deleted!";
    private static final String CREATED_SUCCESSFULLY = "%s created a new club called %s!";
    private LastGamesDTO convertToDTO(LastGames lastGames) {
        return LastGamesDTO.getDetailsFromLastGamesDTO(lastGames);
    }
    public List<LastGamesDTO> findAll() {
        return lastGamesRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public LastGamesDTO findByLastGamesId(String userName){
        if(appUserRepository.existsByUserName(userName)){
            if(lastGamesRepository.existsByPlayerUserName(userName)) {
                return convertToDTO(lastGamesRepository.findByLastGamesId(userName));
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(LAST_GAMES_NOT_EXISTS, userName));
        }
        throw new UsernameNotFoundException(String.format(AppUserService.USER_NOT_FOUND_MSG, userName));
    }
    //constraints? game, game history
    public String deleteLastGamesByPlayerUserName(String userName){
        if (lastGamesRepository.existsByPlayerUserName(userName)) {
            gameRepository.deleteByPlayerUserName(userName);
            gameHistoryRepository.deleteByPlayerUserName(userName);
            lastGamesRepository.deleteLastGamesByPlayerUserName(userName);
            return String.format(LAST_GAMES_DELETED, userName);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(LAST_GAMES_NOT_EXISTS, userName));
    }
    private void createLastGame(String userName){
        LastGames last = new LastGames();
        last.setUserName(userName);
        last.setLastGames(gameHistoryRepository.findByGameHistoryPlayerUserName(userName));
        lastGamesRepository.save(last);
    }
    //app user find all placeholder nicht anzeigen
    public void addGameHistoryToLastGamesList(String userName) {
        //liste darf nur die drei letzten spiele eines users beinhalten
        //Game History muss geaddet werden, erst wenn game zu Ende ist
        int MAX_LIST_SIZE = 3;
        if (gameHistoryRepository.existsByPlayerUserName(userName)) {
            LastGames last = lastGamesRepository.findByLastGamesId(userName);
            last.setLastGames(gameHistoryRepository.findTop3GameHistoriesByPlayerUserName(userName));
        }
        createLastGame(userName);
    }
}
*/
