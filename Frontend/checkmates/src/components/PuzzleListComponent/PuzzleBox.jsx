import React from "react";
import './PuzzleBox.css'
import {Link} from "react-router-dom";


const PuzzleBox = ({ puzzle, puzzleNumber }) => {
    return (
        <div className="puzzle-box">
            <Link className="puzzlelink" to={`/playPuzzle/${puzzle.id}`}>
                Puzzle {puzzleNumber}
            </Link>
        </div>
    );
};

export default PuzzleBox;