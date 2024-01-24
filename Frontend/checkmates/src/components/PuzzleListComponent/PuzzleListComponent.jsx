import React from 'react';
import './PuzzleListComponent.css';
import PuzzleBox from "./PuzzleBox";


const PuzzleListComponent = ({ puzzles, onPuzzleClick }) => {
    return (
        <div className="puzzle-list">
            {puzzles.map((puzzle, index) => (
                <PuzzleBox
                    key={puzzle.id}
                    puzzle={puzzle}
                    puzzleNumber={index +1}
                    onPuzzleClick={() => onPuzzleClick(puzzle.id)}/>


            ))}
        </div>
    );
};

export default PuzzleListComponent;