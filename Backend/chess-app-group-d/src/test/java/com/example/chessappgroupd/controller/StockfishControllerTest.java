package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.game.BoardStatus;
import com.example.chessappgroupd.stockfish.StockfishBestMoveDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StockfishControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    BoardStatus boardStatus0;
    BoardStatus boardStatus1;

    String fenPosition = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1";
    String fenPosition1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    @BeforeEach
    void setUp() {
        String fenPosition = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1";
        String fenPosition1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

        this.boardStatus0 = new BoardStatus(fenPosition);
        this.boardStatus1 = new BoardStatus(fenPosition1);
    }

    @AfterEach
    void tearDown() {
    }

    //works!
    @Test
//    @Disabled
    void checkIfBestMove() throws Exception {
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
//                        .get("/stockfish/check-if-best")
//                        .param("fenPosition", fenPosition)
//                        .param("answerString", "bestmove d2d4")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isAccepted())
//                .andReturn();
////
////        String expected = objectMapper.writeValueAsString(chessPuzzle0);
////        assertEquals(expected, result.getResponse().getContentAsString());


        StockfishBestMoveDTO stockfishBestMoveDTO = new StockfishBestMoveDTO(fenPosition, "bestmove d2d4");

        String jsonContent = new ObjectMapper().writeValueAsString(stockfishBestMoveDTO);
//
//        mockMvc.perform(post("/stockfish/check-if-best")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonContent))  // Set the JSON content as the request body
//                .andExpect(status().isAccepted())
//                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/stockfish/check-if-best")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isAccepted())
                .andReturn();
    }
}