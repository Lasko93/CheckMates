import React, {useState} from "react";
import './CreateLobbyPopup.css'
import ButtonComponent from "../../Buttons/ButtonComponent";
import {useNavigate} from 'react-router-dom';
import {useCreateGame} from '../../../services/GameService';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const CreateLobbyPopup = ({ isOpen, onClose }) => {
    const navigate = useNavigate();
    const [matchName, setMatchName] = useState('');
    const [time, setTime] = useState('5');
    const { mutate: createGame } = useCreateGame();

    const handleMatchNameChange = (event) => setMatchName(event.target.value);

    const handleSubmit = () => {
        if (matchName.trim() === "") {
            toast.error("Match name cannot be empty");
            return;
        }
        //Parse to Int and check if it is a number
        const parsedTime = parseInt(time, 10);
        if (isNaN(parsedTime)) {
            console.error('Invalid time');
            return;
        }
        const userName = sessionStorage.getItem('userName');
        const gameData = { username: userName, gameName: matchName, timer: parsedTime };

        createGame(gameData, {
            onSuccess: (data) => {
                navigate(`/lobby/${data.id}`);
                onClose();
            },
            onError: (error) => console.error('Failed to create the game', error)
        });

        setMatchName("");
        setTime("5");
    };

    if (!isOpen) return null;


    return (
        <div className="popup-container">

        <div className="popup">
            <h2 className="PopupHeadlines">Create a new match</h2>
            <div className="InputContainer">
                <div className="MatchNameContainer">
                    <input className="MatchNameInput"
                        type="text"
                        value={matchName}
                        onChange={handleMatchNameChange}
                        placeholder="Enter a match name...."
                    />
                </div>
                <div className="TimeSelectionContainer">
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


export default CreateLobbyPopup;