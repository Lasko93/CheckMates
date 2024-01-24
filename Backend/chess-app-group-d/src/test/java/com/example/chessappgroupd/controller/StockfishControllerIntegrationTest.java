package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.controller.StockfishController;
import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.game.*;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.GameRepository;
import com.example.chessappgroupd.service.AppUserService;
import com.example.chessappgroupd.service.GameService;
import com.example.chessappgroupd.service.TokenService;
import com.example.chessappgroupd.stockfish.StockfishWrapper;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StockfishControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;


    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    AppUser black;

    AppUser white;

    Game game;


    @BeforeEach
    void setUp() {
        // Mock necessary dependencies behavior
        this.white = new AppUser("white", "First", "Last",
                LocalDate.of(2010, 03, 03), "l@M.de", "233", true);

        this.black = new AppUser("black", "First", "Last",
                LocalDate.of(2010, 03, 03), "l@M.de", "233", true);

        this.game = new Game(white, new GameName("coolGame"), Status.PLAYING, new Timer(11, 11), new ScoreResult(10, 10));

        appUserService.addAppUser(white);
        appUserService.addAppUser(black);
        gameRepository.save(game);
    }

    @AfterEach
    void delete() {
        gameRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    void testCheckIfBestMove() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stockfish/check-if-best")
                        .param("fenPosition", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                        .param("answerString", "bestmove g1f3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string("true"));
    }

    @Test
    void testGetBestMove() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/stockfish/get-best-move")
                        .param("fenPosition", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                        .param("difficulty", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bestMove").value("g1f3"));

    }
    @Test
    void testGetAssistanceRegular() throws Exception {

        gameRepository.save(game);

            mockMvc.perform(MockMvcRequestBuilders.get("/stockfish/get-assistance")
                            .param("fenPosition", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                            .param("difficulty", "1")
                            .param("gameId", "1")
                            .param("color", "white")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());


        mockMvc.perform(MockMvcRequestBuilders.get("/stockfish/get-assistance")
                        .param("fenPosition", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                        .param("difficulty", "1")
                        .param("gameId", "1")
                        .param("color", "white")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bestMove").value("g1f3"));

        Assertions.assertEquals(gameRepository.findAll(), null);
    }

    @Test
    void testGetAssistanceThreshold() throws Exception {

        for (int i = 10; i > 1; i--) {
            mockMvc.perform(MockMvcRequestBuilders.get("/stockfish/get-assistance")
                            .param("fenPosition", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                            .param("difficulty", "1")
                            .param("gameId", "1")
                            .param("color", "white")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }


        mockMvc.perform(MockMvcRequestBuilders.get("/stockfish/get-assistance")
                        .param("fenPosition", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                        .param("difficulty", "1")
                        .param("gameId", "1")
                        .param("color", "white")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bestMove").value("g1f3"));

        Assertions.assertEquals(gameRepository.findAll(), null);
    }
}
