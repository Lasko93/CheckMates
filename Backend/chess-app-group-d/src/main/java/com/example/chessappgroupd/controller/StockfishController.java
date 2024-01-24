package com.example.chessappgroupd.controller;

import com.example.chessappgroupd.domain.game.Game;
import com.example.chessappgroupd.domain.game.ScoreResult;
import com.example.chessappgroupd.service.GameService;
import com.example.chessappgroupd.stockfish.AssistanceDto;
import com.example.chessappgroupd.stockfish.StockfishWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stockfish")
@CrossOrigin(origins = "http://localhost:3000")
public class StockfishController {

    @Autowired
    private GameService gameService;


//    Path RelativePath = Paths.get("");
//    String stockfishPath = RelativePath.toAbsolutePath().toString() + "\\src\\main\\java\\com\\example\\chessappgroupd\\stockfish\\stockfish-windows-x86-64-modern";

//    StockfishWrapper stockfish = new StockfishWrapper();

    StockfishWrapper stockfishWrapper;

    //if this returns Accepted then use processChessPuzzleResult method in ChessPuzzleController to store correct answer if
    // before @RequestParam String fenPosition, @RequestParam String answerString
    @GetMapping("/check-if-best")
    public ResponseEntity<?> checkIfBestMove(@RequestParam String fenPosition, @RequestParam String answerString) {


        this.stockfishWrapper = new StockfishWrapper();
        try {
            // Use the stockfishWrapper object to get the best move
            String bestMoveFromStockfish = stockfishWrapper.getBestMove(fenPosition, 1); // alter depth as needed
            System.out.println("bestMoveFromStockfish: " + bestMoveFromStockfish);

            // Extract only the move part from the bestMoveFromStockfish string
            String bestMove = "bestmove " + bestMoveFromStockfish.split(" ")[1];
            System.out.println("Extracted move: " + bestMove);

            System.out.println("answerString: " + answerString);

            String[] words = bestMoveFromStockfish.split(" ");
            String firstTwoWords = words.length >= 2 ? words[0] + " " + words[1] : bestMoveFromStockfish;

            // Compare the strings
            if (answerString.equals(bestMove)) {
                return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(false, HttpStatus.I_AM_A_TEAPOT);
            }
        } catch (Exception e) {
            // exception
            return new ResponseEntity<>("Error processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-best-move")
    public ResponseEntity<?> getBestMove(@RequestParam String fenPosition, @RequestParam String difficulty) {
        this.stockfishWrapper = new StockfishWrapper();
        try {
            String bestMoveFromStockfish = stockfishWrapper.getBestMove(fenPosition, Integer.parseInt(difficulty));
            String[] words = bestMoveFromStockfish.split(" ");
            String move = words.length >= 2 ? words[1] : bestMoveFromStockfish;

            // Wrapping the move in a JSON object
            return ResponseEntity.ok(Map.of("bestMove", move));
        } catch (Exception e) {
            // Handle exception and return appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }

    }

    @GetMapping("/get-assistance")
    public ResponseEntity<AssistanceDto> getAssistance(@RequestParam String fenPosition, @RequestParam String difficulty,
                                                       @RequestParam String gameId, @RequestParam String color) {
        try {
            Long gameIdLong = Long.parseLong(gameId);
            Game game = gameService.findById(gameIdLong).orElse(null);

            if (game != null) {
                ScoreResult scoreResult = game.getScoreResult();

                if ("black".equals(color)) {
                    if (scoreResult.getValueBlack() > 1) {
                        //valid turn, black still can win 1 point after questioning
                        int newBlackValue = Math.max(scoreResult.getValueBlack() - 1, 0);
                        scoreResult.setValueBlack(newBlackValue);
                    } else {
                        //invalid, black already has 1 point and cannot ask again
                        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                    }
                } else {
                    if (scoreResult.getValueWhite() > 1) {
                        //valid turn, white still can win 1 point after questioning
                        int newWhiteValue = Math.max(scoreResult.getValueWhite() - 1, 0);
                        scoreResult.setValueWhite(newWhiteValue);
                    } else {
                        //invalid, white already has 1 point and cannot ask again
                        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                    }
                }

                // save the updated game back to the database
                gameService.addGame(game);

                // proceed to get the best move
                ResponseEntity<?> bestMoveResponse = getBestMove(fenPosition, difficulty);
                if (bestMoveResponse.getStatusCode() == HttpStatus.OK) {
                    Map<?, ?> responseBody = (Map<?, ?>) bestMoveResponse.getBody();
                    String bestMove = (String) responseBody.get("bestMove");

                    AssistanceDto assistanceDto = new AssistanceDto();
                    assistanceDto.setBestove(bestMove);
                    assistanceDto.setPointsBlack(String.valueOf(scoreResult.getValueBlack()));
                    assistanceDto.setPointsWhite(String.valueOf(scoreResult.getValueWhite()));

                    return ResponseEntity.ok(assistanceDto);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
