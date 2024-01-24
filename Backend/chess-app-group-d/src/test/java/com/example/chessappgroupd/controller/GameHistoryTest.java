package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.game.*;
import com.example.chessappgroupd.domain.gamehistory.GameHistory;
import com.example.chessappgroupd.domain.gamehistory.GameHistoryDTO;
import com.example.chessappgroupd.domain.gamehistory.GameHistoryRepository;
import com.example.chessappgroupd.domain.gamehistory.GameHistoryService;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.GameRepository;
import com.example.chessappgroupd.service.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GameHistoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameHistoryRepository gameHistoryRepo;

    @Autowired
    private GameHistoryService gameHistoryService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AppUserService appUserService;

    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;


    private AppUser appUser1;
    private AppUser appUser2;
    private AppUser appUser3;
    private AppUser appUser4;

    private GameHistory gameHistory1;
    private GameHistory gameHistory2;
    private GameHistory gameHistory3;
    private GameHistory gameHistory4;

    private List<BoardStatus> fen;
    private List<String> moves;

    GameHistoryDTO gameHistoryDTO;

    @BeforeEach
    void setUp() {
        // Mock necessary dependencies behavior
        this.appUser1 = new AppUser("white", "First", "Last",
                LocalDate.of(2010, 03, 03), "l@M.de", "233", true);

        this.appUser2 = new AppUser("black", "First", "Last",
                LocalDate.of(2010, 03, 03), "l@M.de", "233", true);

        this.appUser3 = new AppUser("yellow", "First", "Last",
                LocalDate.of(2010, 03, 03), "l@M.de", "233", true);

        this.appUser4 = new AppUser("pink", "First", "Last",
                LocalDate.of(2010, 03, 03), "l@M.de", "233", true);

        this.game1 = new Game(appUser1, new GameName("coolGame1"), Status.PLAYING, new Timer(11, 11), new ScoreResult(10, 10));

        this.game2 = new Game(appUser2, new GameName("coolGame2"), Status.PLAYING, new Timer(11, 11), new ScoreResult(10, 10));

        this.game3 = new Game(appUser3, new GameName("coolGame3"), Status.PLAYING, new Timer(11, 11), new ScoreResult(10, 10));

        this.game4 = new Game(appUser4, new GameName("coolGame4"), Status.PLAYING, new Timer(11, 11), new ScoreResult(10, 10));

        appUserRepository.save(appUser1);

        appUserRepository.save(appUser2);

        appUserRepository.save(appUser3);

        appUserRepository.save(appUser4);

        gameRepository.save(game1);

        gameRepository.save(game2);

        gameRepository.save(game3);

        gameRepository.save(game4);

        BoardStatus boardStatus1 = new BoardStatus("fen1");

        BoardStatus boardStatus2 = new BoardStatus("fen2");

        List<BoardStatus> fenList = Arrays.asList(boardStatus1, boardStatus2);

        List<String> movesList = Arrays.asList("move1", "move2", "move3");


        gameHistory1 = new GameHistory(
                game1.getId(),
                appUser1,
                appUser2,
                false,
                fenList,
                movesList);


        gameHistoryRepo.save(gameHistory1);

    }

    @AfterEach
    void delete() {
        gameHistoryRepo.deleteAll();
        gameRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    //Testing if all histories can be accessed and return ok
    @Test
    void testFindAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/game/history/"))
                .andExpect(status().isOk())
                .andReturn();
    }

    //Testing if non existent-History returns bad request
    @Test
    void testFindByGameHistoryNonExistent() throws Exception {
        // Assuming gameId exists in the database
        Long gameId = Long.valueOf("33");

        // Perform GET request to "/api/v1/game/history/{gameId}"
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/game/history/{gameId}", gameId))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    //Testing if Appuser1 has history that can be found by username
    @Test
    void testFindGameHistoryByPlayerUserName() throws Exception {
        // Assuming userName exists in the database
        String userName = "white";

        // Perform GET request to "/api/v1/game/history/player/{userName}"
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/game/history/player/{userName}", userName))
                .andExpect(status().isOk())
                .andReturn();
    }

    //Testing History for Appuser1 last 3 games
    @Test
    void testFindLastThreeGameHistoriesByPlayerUserName() throws Exception {
        // Assuming userName exists in the database
        String userName = "white";

        // Perform GET request to "/api/v1/game/history/lastThree/player/{userName}"
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/game/history/lastThree/player/{userName}", userName))
                .andExpect(status().isOk())
                .andReturn();
    }

    //Testing if non-existent History and ApppUser accessed by id returns bad request
    @Test
    void testFindLastThreeGameHistoriesByPlayerUserNameNonExistent() throws Exception {
        // Assuming userName exists in the database
        String userName = "purple";

        // Perform GET request to "/api/v1/game/history/lastThree/player/{userName}"
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/game/history/lastThree/player/{userName}", userName))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

//    @Test
//    void testDeleteGameHistoryByGameHistoryId() throws Exception {
//        // Assuming gameId exists in the database
//        Long gameId = 1L;
//
//        // Perform DELETE request to "/api/v1/game/history/{gameId}/delete"
//        mockMvc.perform(delete("/api/v1/game/history/{gameId}/delete", gameId))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testDeleteAllGameHistoryByPlayerUserName() throws Exception {
//        // Assuming userName exists in the database
//        String userName = "white";
//
//        // Perform DELETE request to "/api/v1/game/history/player/{userName}/delete"
//        mockMvc.perform(delete("/api/v1/game/history/player/{userName}/delete", userName))
//                .andExpect(status().isOk());
//    }


}
