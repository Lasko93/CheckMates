package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.chesspuzzle.ChessPuzzle;
import com.example.chessappgroupd.domain.chesspuzzle.CreationChessPuzzleDTO;
import com.example.chessappgroupd.domain.chesspuzzle.ExpertTrophy;
import com.example.chessappgroupd.domain.chesspuzzle.ProcessChessPuzzleDTO;
import com.example.chessappgroupd.domain.game.BoardStatus;
import com.example.chessappgroupd.repository.AppUserRepository;
import com.example.chessappgroupd.repository.ChessPuzzleRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ChessPuzzleControllerTest {
    @Autowired
    private ChessPuzzleService chessPuzzleService;

    @Autowired
    private ChessPuzzleRepository chessPuzzleRepository;

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

    private ExpertTrophy expertTrophy;

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

        ExpertTrophy expertTrophy = new ExpertTrophy(appUser);
        expertTrophyService.addExpertTrophy(expertTrophy);

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
    void allChessPuzzles() {
        this.chessPuzzleService.addChessPuzzle(chessPuzzle0);
        this.chessPuzzleService.addChessPuzzle(chessPuzzle1);

        assertEquals(chessPuzzleRepository.findAll(), chessPuzzleService.findAll());
    }

    @Test
//    @Disabled
    void findByID() throws Exception {
        chessPuzzleService.addChessPuzzle(chessPuzzle0);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/chesspuzzle/find-by-id/" + this.chessPuzzle0.getId()))
                .andExpect(status().isOk())
                .andReturn();

//        String expected = objectMapper.writeValueAsString(chessPuzzle0);
//        assertEquals(null, result.getResponse().getContentAsString());

        Long id = chessPuzzle0.getId();
        assertEquals(chessPuzzle0, chessPuzzleService.findById(id).orElseThrow(() -> new RuntimeException("chesspuzzle not found")));
    }

    @Test
//    @Disabled
    void deleteById() throws Exception {
        chessPuzzleService.addChessPuzzle(chessPuzzle0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/chesspuzzle/delete-by-id/{id}", this.chessPuzzle0.getId()))

                //        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //            .andExpect(MockMvcResultMatchers.content().string("chesspuzzle deleted successfully"));
    }

    @Test
 //   @Disabled
    void deleteAll() {
        chessPuzzleService.deleteAll();

        assertEquals(chessPuzzleRepository.findAll(), chessPuzzleService.findAll());
    }

    //working most likely?
    @Test
//    @Disabled
    void createChessPuzzle() throws Exception {
//        mockMvc.perform(post("/chesspuzzle/create-chesspuzzle")
//                        .param("fenNotations", "rnbq1rk1/pp1p1ppp/2p2n2/8/3P4/5N2/PPP2PPP/R1BQK2R b KQ - 1 7,rnbq1rk1/pp1p1ppp/2p2n2/8/3P4/5N2/PPP2PPP/R1BQK2R b KQ - 1 7")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());

        CreationChessPuzzleDTO creationChessPuzzleDTO = new CreationChessPuzzleDTO("rnbq1rk1/pp1p1ppp/2p2n2/8/3P4/5N2/PPP2PPP/R1BQK2R b KQ - 1 7,rnbq1rk1/pp1p1ppp/2p2n2/8/3P4/5N2/PPP2PPP/R1BQK2R b KQ - 1 7");
        // Convert DTO to JSON
        String jsonContent = new ObjectMapper().writeValueAsString(creationChessPuzzleDTO);

        mockMvc.perform(post("/chesspuzzle/create-chesspuzzle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))  // Set the JSON content as the request body
                .andExpect(status().isCreated());


        assertEquals(chessPuzzleRepository.findAll(), chessPuzzleService.findAll());


    }

    //working method!!
    @Test
//    @Disabled
    void getRandomChessPuzzle() throws Exception {
        chessPuzzleService.addChessPuzzle(chessPuzzle0);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/chesspuzzle/get-random-chesspuzzle"))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(chessPuzzle0);
        assertEquals(expected, result.getResponse().getContentAsString());
    }

    //working!
    @Test
    @Disabled
    void processChessPuzzleResult() throws Exception {

        chessPuzzleService.addChessPuzzle(chessPuzzle0);
        chessPuzzleService.addChessPuzzle(chessPuzzle1);
        chessPuzzleService.addChessPuzzle(chessPuzzle2);

        ProcessChessPuzzleDTO processChessPuzzleDTO = new ProcessChessPuzzleDTO(appUser1.getUsername(), chessPuzzle0.getId().toString());
        ProcessChessPuzzleDTO processChessPuzzleDT1 = new ProcessChessPuzzleDTO(appUser1.getUsername(), chessPuzzle1.getId().toString());
        ProcessChessPuzzleDTO processChessPuzzleDT2 = new ProcessChessPuzzleDTO(appUser1.getUsername(), chessPuzzle2.getId().toString());
        // Convert DTO to JSON
        String jsonContent = new ObjectMapper().writeValueAsString(processChessPuzzleDTO);
        String jsonContent1 = new ObjectMapper().writeValueAsString(processChessPuzzleDT1);
        String jsonContent2 = new ObjectMapper().writeValueAsString(processChessPuzzleDT2);

        mockMvc.perform(post("/chesspuzzle/process-answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))  // Set the JSON content as the request body
                .andExpect(status().isCreated());

        mockMvc.perform(post("/chesspuzzle/process-answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent1))  // Set the JSON content as the request body
                .andExpect(status().isCreated());

        mockMvc.perform(post("/chesspuzzle/process-answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent2))  // Set the JSON content as the request body
                .andExpect(status().isCreated());


//        mockMvc.perform(post("/chesspuzzle/process-answer")
//                        .param("appUserId", appUser1.getUserName())
//                        .param("chessPuzzleId", chessPuzzle0.getId().toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(post("/chesspuzzle/process-answer")
//                        .param("appUserId", appUser1.getUserName())
//                        .param("chessPuzzleId", chessPuzzle1.getId().toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(post("/chesspuzzle/process-answer")
//                        .param("appUserId", appUser1.getUserName())
//                        .param("chessPuzzleId", chessPuzzle2.getId().toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());


        String expected = objectMapper.writeValueAsString(expertTrophyService.findAll());
//        assertEquals(expected, result.getResponse().getContentAsString());

//        System.out.println(expertTrophyService.findAll());
        assertEquals(expected, expertTrophyService.findAll());
    }
}