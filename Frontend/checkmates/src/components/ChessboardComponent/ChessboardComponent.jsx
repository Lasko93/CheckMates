import React, {useEffect, useState} from 'react';
import {Chess} from 'chess.js';
import './ChessboardComponent.css'
import {toast} from "react-toastify";

//Import BlackChessPieces
import RB from '../../assets/images/ChessPieces_black/r.svg';
import NB from '../../assets/images/ChessPieces_black/n.svg';
import BB from '../../assets/images/ChessPieces_black/b.svg';
import QB from '../../assets/images/ChessPieces_black/q.svg';
import KB from '../../assets/images/ChessPieces_black/k.svg';
import PB from '../../assets/images/ChessPieces_black/p.svg';

//Import BrownChessPieces
import RW from '../../assets/images/ChessPieces_brown/R.svg';
import NW from '../../assets/images/ChessPieces_brown/N.svg';
import BW from '../../assets/images/ChessPieces_brown/B.svg';
import QW from '../../assets/images/ChessPieces_brown/Q.svg';
import KW from '../../assets/images/ChessPieces_brown/K.svg';
import PW from '../../assets/images/ChessPieces_brown/P.svg';
import webSocketService from "../../services/WebSocketService";
import {getBestMoveFromBackend, validateMoveWithBackend} from "../../services/ChessPuzzleService";
import {updateExpertTrophy} from "../../services/ExpertThropyService";
import {useParams} from "react-router-dom";


const initialBoard = [
    ['R_B', 'N_B', 'B_B', 'Q_B', 'K_B', 'B_B', 'N_B', 'R_B'],
    ['P_B', 'P_B', 'P_B', 'P_B', 'P_B', 'P_B', 'P_B', 'P_B'],
    ['', '', '', '', '', '', '', ''],
    ['', '', '', '', '', '', '', ''],
    ['', '', '', '', '', '', '', ''],
    ['', '', '', '', '', '', '', ''],
    ['P_W', 'P_W', 'P_W', 'P_W', 'P_W', 'P_W', 'P_W', 'P_W'],
    ['R_W', 'N_W', 'B_W', 'Q_W', 'K_W', 'B_W', 'N_W', 'R_W']
];


const getPieceImage = (piece) => {
    switch (piece) {
        case 'R_B': return RB;
        case 'N_B': return NB;
        case 'B_B': return BB;
        case 'Q_B': return QB;
        case 'K_B': return KB;
        case 'P_B': return PB;
        case 'R_W': return RW;
        case 'N_W': return NW;
        case 'B_W': return BW;
        case 'Q_W': return QW;
        case 'K_W': return KW;
        case 'P_W': return PW;
        default: return '';
    }
};


//Modal for the promotion choice
const PromotionChoice = ({ onSelect, onClose, playerTurn }) => {
    return (
        <div className="promotion-modal-backdrop" onClick={onClose}>
            <div className="promotion-dialog" onClick={e => e.stopPropagation()}>
                {['q', 'r', 'b', 'n'].map(piece => (
                    <img
                        key={piece}
                        src={getPieceImage(`${piece.toUpperCase()}_${playerTurn}`)}
                        alt={piece}
                        onClick={() => onSelect(piece)}
                    />
                ))}
            </div>
        </div>
    );
};



const ChessboardComponent = ({ ChessboardSize, gameId, fen, whitePlayerTurn, isCurrentUserWhite, isCurrentUserBlack, gameIsStarted, isPuzzle, playAgainstComputer, onFirstMove,setFen, difficulty, handleEndGame }) => {


    const [game, setGame] = useState(new Chess());
    const [board, setBoard] = useState(initialBoard);
    const [selectedSquare, setSelectedSquare] = useState(null);
    const [lastMove, setLastMove] = useState(null);
    const [showPromotionDialog, setShowPromotionDialog] = useState(false);
    const [promotionMove, setPromotionMove] = useState(null);
    const { puzzleId } = useParams();
    const userName = sessionStorage.getItem('userName');
    const [isBoardEnabled, setIsBoardEnabled] = useState(true);



    // render the board
    const updateBoard = () => {
        const newBoard = Array(8).fill(null).map(() => Array(8).fill(''));
        game.board().forEach((row, rowIndex) => {
            row.forEach((piece, colIndex) => {
                if (piece) {
                    newBoard[rowIndex][colIndex] = `${piece.type.toUpperCase()}_${piece.color.toUpperCase()}`;
                }
            });
        });
        setBoard(newBoard);
    };

    //ensure that the board is updated when you made a move
    useEffect(() => {
        updateBoard();
    }, [game]);


    useEffect(() => {
        if (fen) {
            const newGame = new Chess(fen);
            setGame(newGame);
            updateBoard(newGame);
        }
    }, [fen]);

    //check which piece is selected
    const isSelected = (rowIndex, colIndex) => {
        const square = `${String.fromCharCode(97 + colIndex)}${8 - rowIndex}`;
        return selectedSquare === square;
    };


    //handle the promotion choice
    const handlePromotionChoice = (piece) => {
        if (promotionMove) {
            game.move({
                from: promotionMove.from,
                to: promotionMove.to,
                promotion: piece.toLowerCase()
            });
            setShowPromotionDialog(false);
            setPromotionMove(null);
            setGame(new Chess(game.fen()));
            updateBoard();


            //Send the new board status to the server
            const boardUpdate = {
                fen: game.fen(),
                gameId: gameId,
                whitePlayerTurn: whitePlayerTurn,
                check: game.inCheck(),
                checkMate: game.isCheckmate(),
                remis: game.isDraw(),
                sanMove: game.history({verbose: true}).pop().san
            }
            console.log("Board Update: ", boardUpdate)
            webSocketService.sendMessage(`/app/game/${gameId}/updateBoardStatus`, JSON.stringify(boardUpdate));

            setSelectedSquare(null);
        }
    };



    //actual move of a piece and game logic
    const handleSquareClick = (rowIndex, colIndex) => {
        if (!isBoardEnabled) {
            // Prevent any moves if the board is not enabled
            return;
        }

        const square = `${String.fromCharCode(97 + colIndex)}${8 - rowIndex}`;

        if (isPuzzle && selectedSquare && game.get(square) && game.get(square).color === game.turn()) {
            const move = {
                from: selectedSquare,
                to: square
            };
            setLastMove(move);
        }

        //If game is not started yet, do not allow to move
        if (!gameIsStarted || !isBoardEnabled) {
            return;
        }

         //handle the turn selection with props of GameLobby
        if (((whitePlayerTurn && !isCurrentUserWhite) || (!whitePlayerTurn && !isCurrentUserBlack)) && !playAgainstComputer && !isPuzzle) {
            // Not the player's turn, do not allow to move
            toast.error("It's not your turn!");
            return;
        }

        if (playAgainstComputer && game.turn() !== 'w') {
            toast.info("It's not your turn!");
            return;
        }

        if (selectedSquare) {
            // Saveup the boardstate before the move is made to reset for puzzle
            const firstPuzzleMove = game.fen();


            //Check if its a promotion move --> Open the modal if it is
            if (checkIfPromotionMove(game.move, rowIndex, colIndex)) {
                return;
            }

            if (selectedSquare === square) {
                setSelectedSquare(null);

            } else {
                // logic for the move of a piece
                try {



                    const move = game.move({
                        from: selectedSquare,
                        to: square,
                    });
                    if (move !== null) {
                        const myMove = "bestmove "+move.from+move.to;

                        if (playAgainstComputer)   {
                            handleComputerMatch();

                        }

                        else if (isPuzzle) {
                            handlePuzzleMoveSuccess(move, firstPuzzleMove, myMove);

                        }
                        else {
                            handleMoveSuccess(move);
                        }
                    }

                    //Catch the error if the move is invalid and show toast
                } catch (error) {
                    if (game.inCheck()){
                        toast.error("Invalid move attempted: You are in check!", {
                            position: toast.POSITION.BOTTOM_CENTER
                        });
                        setSelectedSquare(null); // Reset selection
                    }
                    else {
                        toast.error(`Invalid move attempted: ${selectedSquare} to ${square}`, {
                            position: toast.POSITION.BOTTOM_CENTER
                        });
                        setSelectedSquare(null); // Reset selection
                    }
                }
            }
        } else {
            if (game.get(square) && game.get(square).color === game.turn()) {
                setSelectedSquare(square);
            }
        }
    };

    const handleComputerMatch = async () => {
        checkGameStatus(game);
        onFirstMove();
        setSelectedSquare(null);
        updateBoard();

        try {
            const move = await getBestMoveFromBackend(game.fen(), difficulty);
            game.move(move.bestMove);
            updateBoard();

        }

     catch (error) {

     }

     finally {
            const gameEnded = checkGameStatus(game);
            if (!gameEnded) {
                setFen(game.fen());
            }

        }

    }


    const checkGameStatus = (game) => {
        if (game.isCheckmate()) {
            toast.error('Game over - Checkmate');
            setIsBoardEnabled(false);
            handleEndGame();
            return true; // Indicate that the game has ended
        } else if (game.isDraw()) {
            toast.error('Game over - Draw');
            setIsBoardEnabled(false);
            handleEndGame();
            return true; // Indicate that the game has ended
        } else if (game.inCheck()) {
            toast.info('Check!');
        }
        return false; // Indicate that the game is still ongoing
    };




    const checkIfPromotionMove = (move,rowIndex,colIndex) => {
        const from = selectedSquare;
        const to = `${String.fromCharCode(97 + colIndex)}${8 - rowIndex}`;
        const movingPiece = game.get(from);
        if (movingPiece && movingPiece.type === 'p' &&
            ((movingPiece.color === 'w' && rowIndex === 0) ||
                (movingPiece.color === 'b' && rowIndex === 7))) {
            // Store the move and show promotion dialog
            setPromotionMove({from, to});
            setShowPromotionDialog(true);
        }
    }

    const handleMoveSuccess = (move) => {
        const fen = game.fen();
        const boardUpdate = {
            fen: fen,
            gameId: gameId,
            whitePlayerTurn: whitePlayerTurn,
            check: game.inCheck(),
            checkMate: game.isCheckmate(),
            remis: game.isDraw(),
            sanMove: game.history({verbose: true}).pop().san
        };
        console.log("Board Update: ", boardUpdate);
        webSocketService.sendMessage(`/app/game/${gameId}/updateBoardStatus`, JSON.stringify(boardUpdate));
        setSelectedSquare(null);
    };




    const handlePuzzleMoveSuccess = (move, firstPuzzleMove, myMove) => {
        updateBoard(game.fen());
        setSelectedSquare(null);
        const secondPuzzleFEN = game.fen();
        console.log("Second Puzzle Move: ", secondPuzzleFEN);

        // insert Puzzle code check here!!!
        getBestMove(firstPuzzleMove, myMove).then(
            (response) => {
                console.log("Response: ", response);
                if (response === true) {
                    toast.info('Correct move!');
                    updateExpertTrophy(userName, puzzleId);
                    console.log(updateExpertTrophy(userName));

                    setIsBoardEnabled(false);
                } else {
                    toast.error('Incorrect move. Try again!');
                    game.load(firstPuzzleMove);
                    updateBoard();
                }
            }
        )
    }

    const getBestMove = async (firstFEN,secondFEN) => {
        return await validateMoveWithBackend(firstFEN, secondFEN);
    }

    const handleCloseModal = () => {
        setShowPromotionDialog(false);
        setPromotionMove(null);
    };



    return (
        <div className="chessboardContainer" style={{ width: ChessboardSize, height: ChessboardSize }}>
            {showPromotionDialog && (
                <PromotionChoice
                    onSelect={handlePromotionChoice}
                    onClose={handleCloseModal}
                    playerTurn={game.turn().toUpperCase()}
                />
            )}
            <div className="chessboard">
                {board.map((row, rowIndex) => (
                    row.map((piece, colIndex) => {
                        const pieceImage = getPieceImage(piece);
                        const isSelectedPiece = isSelected(rowIndex, colIndex);
                        return (
                            <div
                                key={`${rowIndex}-${colIndex}`}
                                className={`chess-square ${isSelectedPiece ? 'selected-piece' : ''}`}
                                onClick={() => handleSquareClick(rowIndex, colIndex)}
                            >
                                {pieceImage && <img src={pieceImage} alt={piece} />}
                            </div>
                        );
                    })
                ))}
            </div>
        </div>
    );
};

export default ChessboardComponent;