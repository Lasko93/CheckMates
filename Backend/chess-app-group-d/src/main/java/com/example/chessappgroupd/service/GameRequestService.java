package com.example.chessappgroupd.service;

import com.example.chessappgroupd.domain.appUser.AppUser;
import com.example.chessappgroupd.domain.game.GameRequest;
import com.example.chessappgroupd.domain.game.RequestStatus;
import com.example.chessappgroupd.repository.GameRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//altered linkage to handwritten find by id

@Service
public class GameRequestService {
    @Autowired
    private GameRequestRepository gameRequestRepository;

    public List<GameRequest> findAll() {
        return gameRequestRepository.findAll();
    }

    public Optional<GameRequest> findById(Long id) {
        return gameRequestRepository.findById(id);
    }

    public void addGameRequest(GameRequest gameRequest) {
        gameRequestRepository.save(gameRequest);
    }

    public GameRequest addGameRequesti(GameRequest gameRequest) {
        gameRequestRepository.save(gameRequest);
        return gameRequest;
    }

//    public GameRequest createGameRequest(AppUser whiteUser) {
//        GameRequest gameRequest = new GameRequest(whiteUser);
//        gameRequestRepository.save(gameRequest);
//        return gameRequest;
//    }

//    public GameRequest acceptGameRequest(Long requestId, AppUser blackUser) {
//        Optional<GameRequest> optionalGameRequest = gameRequestRepository.findById(requestId);
//
//        if (optionalGameRequest.isPresent()) {
//            GameRequest gameRequest = optionalGameRequest.get();
//
//            if (gameRequest.getRequestStatus() == RequestStatus.PENDING) {
//                gameRequest.setBlack(blackUser);
//                gameRequest.setStatus(RequestStatus.ACCEPTED);
//                gameRequestRepository.save(gameRequest);
//                return gameRequest;
//            }
//        }
//        return null;
//    }

    public void deleteGameRequest(Long id) {
        gameRequestRepository.deleteById(id);
    }

    public List<GameRequest> findGameRequestsByWhiteUserName(String username) {
        return gameRequestRepository.findGameRequestsByWhiteUserName(username);
    }

    public List<GameRequest> findGameRequestsByBlackUserName(String username) {
        return gameRequestRepository.findGameRequestsByBlackUserName(username);
    }

    public List<GameRequest> findPendingGameRequestsByWhiteId(String username) {
        return gameRequestRepository.findPendingGameRequestsByWhiteId(username);
    }

    public List<GameRequest> findPendingGameRequestsByBlackId(String username) {
        return gameRequestRepository.findPendingGameRequestsByBlackId(username);
    }
}
