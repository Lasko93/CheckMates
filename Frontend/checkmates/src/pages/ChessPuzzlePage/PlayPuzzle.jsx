import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import './PlayPuzzle.css';
import ChessboardComponent from "../../components/ChessboardComponent/ChessboardComponent";
import {usePuzzleById} from "../../services/ChessPuzzleService";
import PuzzleIcon from "../../assets/images/puzzle_icon.svg";


const PlayPuzzle = () => {
    const { puzzleId } = useParams();
    console.log("Puzzle ID:", puzzleId)
    const { data: puzzle, isLoading, isError } = usePuzzleById(puzzleId);
    console.log("Puzzle data:", puzzle); // Überprüfen Sie, ob das Puzzle-Objekt korrekt geladen wird
    const [currentPlayer, setCurrentPlayer] = useState('White');
    // State für den aktuellen FEN-String
    const [currentFen, setCurrentFen] = useState('');
    const [lastMove, setLastMove] = useState(null);


    // Effekt-Hook, um den FEN-Status zu aktualisieren, wenn das Puzzle geladen wird
    // Aktualisiert den FEN-String, wenn das Puzzle geladen wird oder wenn 'location.state.fen' verfügbar ist
    useEffect(() => {
        if (puzzle) {
            setCurrentFen(puzzle);
            console.log("Puzzle FEN:", puzzle); // Überprüfen Sie die FEN-Notation
            const player = puzzle.split(' ')[1] === 'w' ? 'White' : 'Black';
            setCurrentPlayer(player);

        }
    }, [puzzle]);

    if (isLoading) return <div>Loading...</div>;
    if (isError) return <div>Error loading puzzle</div>;
    if (!puzzleId) return <div>Puzzle ID not found</div>;

    console.log('FEN String:', currentFen);

    return (
    <div className="PlayPuzzle">
        <div className="PlayPuzzleHeader">
            <img className="PuzzleIcon" src={PuzzleIcon} alt="Puzzle Icon" />
            <h1>Show me your best move!</h1>
        </div>
        <div className="CurrentPlayer">
            <h2>Current Turn: {currentPlayer}</h2>
        </div>
        <div className="Puzzle">
            {currentFen ? (
                <>
                    <ChessboardComponent
                        fen={currentFen}
                        ChessboardSize="40vw"
                        isPuzzle={true}
                        setLastMove={setLastMove}
                        gameIsStarted={true}
                        isCurrentUserWhite={true}
                        isWhitePlayTurn={true}
                        playAgainstComputer={false}
                    />

                </>
            ) : (
                <div>No puzzle loaded</div>
            )}
        </div>
    </div>
);
};

export default PlayPuzzle;