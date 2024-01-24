import React, {useState} from "react";
import './PlayVsComputer.css'
import ButtonComponent from "../Buttons/ButtonComponent";
import 'react-toastify/dist/ReactToastify.css';
import {useNavigate} from "react-router-dom";
import {toast} from "react-toastify";

const PlayVsComputerPopUp = ({ isOpen, onClose }) => {

    const [time, setTime] = useState(null);
    const [difficulty, setDifficulty] = useState(null);
    const navigate = useNavigate();


    const handleSubmit = () => {
        if (time === null || difficulty === null) {
            toast.error('Please select a time and difficulty')
            return;
        }
        const gameData = {
            fen: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            minutes: time,
            seconds: 0,
            points: 10,
            difficulty: difficulty,
            firstMoveMade: false
        }
        sessionStorage.setItem('gameDataComputer', JSON.stringify(gameData));
        navigate('/playAgainstComputer')
    }



    if (!isOpen) return null;


    return (
        <div className="popup-container">

            <div className="popup">
                <h2 className="PopupHeadlines">Create a new computer match</h2>
                <div className="InputContainer">
                    <div className="DifficultySelection">
                        <h3 id="MinutesText"> Difficulty </h3>
                        <ButtonComponent
                            specificClassName="TimeButtons"
                            text="Beginner"
                            buttonID="Beginner"
                            onClick={() => setDifficulty("1")}
                            style={{backgroundColor: difficulty === "1" ? "#546D81" : "#14202E"}}
                        />
                        <ButtonComponent
                            specificClassName="TimeButtons"
                            text="Advanced"
                            buttonID="Advanced"
                            onClick={() => setDifficulty("2")}
                            style={{backgroundColor: difficulty === "2" ? "#546D81" : "#14202E"}}
                        />
                        <ButtonComponent
                            specificClassName="TimeButtons"
                            text="ChatGPT"
                            buttonID="ChatGPT"
                            onClick={() => setDifficulty("3")}
                            style={{backgroundColor: difficulty === "3" ? "#546D81" : "#14202E"}}
                        />
                    </div>
                    <div className="TimeSelectionComputer">
                        <h3 id="MinutesText"> Minutes </h3>
                        <div className="PopupTimeButtonContainer">
                            <ButtonComponent
                                specificClassName="TimeButtons"
                                text="05"
                                buttonID="05Minutes"
                                onClick={()=> setTime("05")}
                                style={{backgroundColor: time === "05" ? "#546D81" : "#14202E"}}
                            />
                            <ButtonComponent
                                specificClassName="TimeButtons"
                                text="10"
                                buttonID="10Minutes"
                                onClick={()=>setTime("10")}
                                style={{backgroundColor: time === "10" ? "#546D81" : "#14202E"}}
                            />
                            <ButtonComponent
                                specificClassName="TimeButtons"
                                text="15"
                                buttonID="15Minutes"
                                onClick={()=>setTime("15")}
                                style={{backgroundColor: time === "15" ? "#546D81" : "#14202E"}}
                            />
                        </div>
                    </div>
                </div>
                <div className="PopupButtons">
                    <ButtonComponent text="Cancel" buttonID="ExitPopup" onClick={onClose} />
                    <ButtonComponent text="Submit" buttonID="CreateMatch" onClick={handleSubmit} />
                </div>
            </div>
        </div>
    );
};


export default PlayVsComputerPopUp;