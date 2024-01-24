package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.chesspuzzle.ChessPuzzle;
import com.example.chessappgroupd.domain.chesspuzzle.ExpertTrophy;
import com.example.chessappgroupd.domain.game.BoardStatus;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.service.ChessPuzzleService;
import com.example.chessappgroupd.service.ExpertTrophyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExpertTrophyControllerTest {

    @Autowired
    private ChessPuzzleService chessPuzzleService;

    @Autowired
    private ExpertTrophyService expertTrophyService;

    @Autowired
    private AppUserRepository appUserRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AppUser appUser;

    private AppUser appUser1;

    private ChessPuzzle chessPuzzle0;

    private ChessPuzzle chessPuzzle1;

    private ChessPuzzle chessPuzzle2;

    private ExpertTrophy expertTrophy0;

    private ExpertTrophy expertTrophy1;


    BoardStatus boardStatus3 = new BoardStatus("rnbq1rk1/pp1p1ppp/2p2n2/8/3P4/5N2/PPP2PPP/R1BQK2R b KQ - 1 7");

    @BeforeEach
    public void create() {

        appUser = new AppUser("peppaPIG", "Peppa", "Pig"
                , LocalDate.of(1999, 9, 12),
                "peppapig@toennies.com", "schweinshachse12345?", true);
        appUser1 = new AppUser("stewielovesbrian", "Stewie"
                , "Griffin", LocalDate.of(2003, 4, 19)
                , "stewiegriffin@lovesbrian.com", "rupert12345?", true);

        appUserRepository.save(appUser);
        appUserRepository.save(appUser1);

        BoardStatus boardStatus0 = new BoardStatus("rnbq1rk1/pp1p1ppp/2p2n2/8/3P4/5N2/PPP2PPP/R1BQK2R b KQ - 1 7");
        BoardStatus boardStatus1 = new BoardStatus("r2qk2r/p4ppp/2pp4/8/4n3/4P3/PPP2PPP/RNBQK1NR w KQkq - 0 1");
        BoardStatus boardStatus2 = new BoardStatus("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");

        expertTrophy0 = new ExpertTrophy(appUser);
        expertTrophy1 = new ExpertTrophy(appUser1);

        chessPuzzle0 = new ChessPuzzle(boardStatus0);
        chessPuzzle1 = new ChessPuzzle(boardStatus1);
        chessPuzzle2 = new ChessPuzzle(boardStatus2);
    }

    @AfterEach
    public void delete() {
        this.expertTrophyService.deleteAll();
        this.chessPuzzleService.deleteAll();
        this.appUserRepository.deleteAll();
    }

    @Test
//    @Disabled
    void allExpertTrophies() throws Exception {
        expertTrophyService.addExpertTrophy(expertTrophy0);
        expertTrophyService.addExpertTrophy(expertTrophy1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/expert-trophy/find-all"))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(expertTrophyService.findAll());
        assertEquals(expected, result.getResponse().getContentAsString());

//        String id = expertTrophy0.getId();
//        assertEquals(expertTrophy0, expertTrophyService.findById(id).orElseThrow(() -> new RuntimeException("expert-trophy not found")));
    }

    @Test
//    @Disabled
    void findByID() throws Exception {
        expertTrophyService.addExpertTrophy(expertTrophy0);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/expert-trophy/find-by-id/" + this.expertTrophy0.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(expertTrophy0);
//        assertEquals(expected, result.getResponse().getContentAsString());

        String id = expertTrophy0.getId();
        assertEquals(expertTrophy0, expertTrophyService.findById(id).orElseThrow(() -> new RuntimeException("expertTrophy not found")));
    }

    @Test
//    @Disabled
    void deleteById() throws Exception {
        expertTrophyService.addExpertTrophy(expertTrophy0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/expert-trophy/delete-by-id/{id}", this.expertTrophy0.getId()))
                .andExpect(status().isOk());
//                .andExpect(MockMvcResultMatchers.content().string("expertTrophy deleted successfully"));
    }

    @Test
//    @Disabled
    void deleteAll() throws Exception {
        expertTrophyService.addExpertTrophy(expertTrophy0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/expert-trophy/delete-by-id/{id}", this.expertTrophy0.getId()))
                .andExpect(status().isOk());
//                .andExpect(MockMvcResultMatchers.content().string("expert-trophy deleted successfully"));
    }
}