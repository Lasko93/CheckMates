package com.example.chessappgroupd.domain.pgn;

import com.example.chessappgroupd.domain.game.BoardStatus;
import com.example.chessappgroupd.domain.gamehistory.GameHistory;
import com.example.chessappgroupd.domain.gamehistory.GameHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class pgnService {

    @Autowired
    private pgnRepository pgnRepository;

    @Autowired
    private GameHistoryRepository gameHistoryRepository;

    public pgnFile saveFile(MultipartFile file, String username) throws Exception {
        String originalFileName = file.getOriginalFilename();
        boolean isPgnFile = originalFileName != null && originalFileName.toLowerCase().endsWith(".pgn");

        if (isPgnFile) {
            pgnFile pgn = pgnFile.builder()
                    .fileName(originalFileName)
                    .username(username)
                    .content(file.getBytes())
                    .build();
            return pgnRepository.save(pgn);
        } else {
            throw new Exception("File is not a valid PGN file.");
        }
    }


    public pgnFile getHistoryFile(Long id) {
        GameHistory gameHistory = gameHistoryRepository.findAllGameHistoryByGameHistoryId(id);
        StringBuilder pgnBuilder = new StringBuilder();
        String white = gameHistory.getWhite().getUsername();
        int whiteElo = gameHistory.getWhite().getScore();
        String black = gameHistory.getBlack().getUsername();
        int blackElo = gameHistory.getBlack().getScore();
        String event = black + " vs " + white;
        int round = gameHistory.getFen().size();
        pgnBuilder.append("[Event \"").append(event).append("\"]\n");
        pgnBuilder.append("[Site \"").append("checkmates.com").append("\"]\n");
        pgnBuilder.append("[Turns \"").append(round).append("\"]\n");
        pgnBuilder.append("[White \"").append(white).append("\"]\n");
        pgnBuilder.append("[Black \"").append(black).append("\"]\n");
        pgnBuilder.append("[WhiteElo \"").append(whiteElo).append("\"]\n");
        pgnBuilder.append("[BlackElo \"").append(blackElo).append("\"]\n");
        if(gameHistory.getWhitePlayerWon()) {
            pgnBuilder.append("[Result \"").append("1-0").append("\"]\n\n");
        }
        else {
            pgnBuilder.append("[Result \"").append("0-1").append("\"]\n\n");
        }
        int i = 1;
        for(String move: gameHistory.getMoves()) {
            if(i%2 == 1) {
                pgnBuilder.append(i + ". ").append(move + " ");
                i++;
            }
            else {
                pgnBuilder.append(move + " ");
                i++;
            }
        }
        if(gameHistory.getWhitePlayerWon()) {
            pgnBuilder.append("1-0");
        }
        else {
            pgnBuilder.append("0-1");
        }
        pgnFile file = pgnFile.builder()
                .fileName(event + new Date())
                .content(pgnBuilder.toString().getBytes())
                .build();

        return file;
    }

    public pgnFile getFile(Long id) {
        return pgnRepository.findByFileId(id);
    }

    public void deleteFile(Long id) {
        if(pgnRepository.existsById(id)) {
            pgnRepository.deleteById(id);
        }
    }

    public List<pgnFile> getMyPGN (String username) {
        return pgnRepository.findByUsername(username);
    }
}
