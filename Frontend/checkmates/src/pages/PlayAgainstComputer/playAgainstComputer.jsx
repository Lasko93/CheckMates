import React, {useEffect, useRef, useState} from 'react';
import './playAgainstComputer.css'
import ChessboardComponent from "../../components/ChessboardComponent/ChessboardComponent";
import ButtonComponent from "../../components/Buttons/ButtonComponent";
import LobbyProfile from "../../components/LobbyComponents/LobbyProfileComponent/LobbyProfile";
import ProfilePicture from '../../assets/images/PlayerIcon.svg'
import {useFetchProfilePicture, useFetchUser} from "../../services/UserService";
import {toast} from "react-toastify";
import {useNavigate} from "react-router-dom";
import ChessComputerPicture from '../../assets/images/ChessComputer.svg'
import {getBestMoveFromBackend} from "../../services/ChessPuzzleService";

function PlayAgainstComputer() {

    const navigate = useNavigate();

    const [fen, setFen] = useState("");
    const timerIntervalRef = useRef(null);

    const currentUser = sessionStorage.getItem('userName');

    const myProfilePicture = useFetchProfilePicture(currentUser);
    const {data: currentUserData} = useFetchUser(currentUser);
    const [yourTurn, setYourTurn] = useState(true);
    const [currentMinutes, setCurrentMinutes] = useState(null);
    const [currentSeconds, setCurrentSeconds] = useState(null);
    const [firstMoveMade, setFirstMoveMade] = useState(false);
    const [points, setPoints] = useState(10);
    const [difficulty, setDifficulty] = useState(null);



    const saveGameData = (fen, minutes, seconds, points,difficulty, firstMoveMade) => {
        const gameData = {
            fen: fen,
            minutes: minutes,
            seconds: seconds,
            points: points,
            difficulty: difficulty,
            firstMoveMade: firstMoveMade
        }
        sessionStorage.setItem('gameDataComputer', JSON.stringify(gameData));
    }

    const ConvertGameData = () => {
        const gameDataString = sessionStorage.getItem('gameDataComputer');
        if (gameDataString) {
            return JSON.parse(gameDataString);
        }
        return null;
    };


    const loadGameData = () => {
        const savedGameData = ConvertGameData();
        if (savedGameData) {
            setCurrentMinutes(savedGameData.minutes);
            setCurrentSeconds(savedGameData.seconds);
            setFen(savedGameData.fen);
            setDifficulty(savedGameData.difficulty);
            setPoints(savedGameData.points);
            setFirstMoveMade(savedGameData.firstMoveMade);
        }
    }


    //Function to display time in the right format
    function formatTime(time) {
        return time < 10 ? `0${time}` : time;
    }


    useEffect(() => {
       if (sessionStorage.getItem('gameDataComputer')) {
           loadGameData();
       }
    }, []);


    //fetching the profile pictures and converting them into url to display
    let myProfilePictureURL =  myProfilePicture?.data?.images
        ? `data:${myProfilePicture.data.type};base64,${myProfilePicture?.data?.images}`
        : ProfilePicture;

    //Callback function to set the first move made to true
    const onFirstMove = () => {
        setFirstMoveMade(true);
        saveGameData(fen, currentMinutes, currentSeconds, points, difficulty, firstMoveMade)
    }

    const setFenCallback = (fen) => {
        setFen(fen);
        saveGameData(fen, currentMinutes, currentSeconds, points, difficulty, firstMoveMade);
    }

    //Decrease timer by 1 second
    useEffect(() => {
        if (firstMoveMade) {
            timerIntervalRef.current = setInterval(() => {
                if (currentSeconds > 0) {
                    setCurrentSeconds(currentSeconds - 1);
                } else if (currentMinutes > 0) {
                    setCurrentMinutes(currentMinutes - 1);
                    setCurrentSeconds(59);
                } else {
                    clearInterval(timerIntervalRef.current);
                    // Display toast here
                    toast.error("Time's up! You lost the game.");
                }
                saveGameData(fen, currentMinutes, currentSeconds, points, difficulty, firstMoveMade)
            }, 1000);

            return () => clearInterval(timerIntervalRef.current);
        }

    }, [currentMinutes, currentSeconds, firstMoveMade]);


    const handleEndGame = () => {
        sessionStorage.removeItem('gameDataComputer');
        clearInterval(timerIntervalRef.current);
        //handle points
        setTimeout(() => {
            navigate('/');
        }, 10000);
    }

    const handleGiveUpButton = () => {
    sessionStorage.removeItem('gameDataComputer');
    navigate('/')
    }

    const handleBotButton = async () => {
        const move = await getBestMoveFromBackend(fen, difficulty);
        console.log(move)
        toast.success(`ChatGPT says: ${move.bestMove} is the best next move` , {
            position: toast.POSITION.BOTTOM_CENTER,
            autoClose: 6500
        });
    }


    return (
        <>
            <div>
                <LobbyProfile
                    ContainerName="PlayerBlack"

                    ProfileImage={ChessComputerPicture}
                    PlayerName={"Computer"}
                    PlayerPoints={"Level " + difficulty}
                    TimerID="PlayerBlackTimer"
                    isCurrentTurn={!yourTurn}
                    showTimer={false}
                />
                <LobbyProfile
                    ContainerName="PlayerBrown"
                    ProfileImage={myProfilePictureURL }
                    PlayerName={currentUserData?.userName || "Waiting...."}
                    PlayerPoints={currentUserData?.score || 0 }
                    TimerID="PlayerWhiteTimer"
                    showTimer={true}
                    TimerMinutes={formatTime(currentMinutes)}
                    TimerSeconds={formatTime(currentSeconds)}
                    isCurrentTurn={yourTurn}
                />
            </div>
            <div className="ChessPositionerGameLobby">
                <ChessboardComponent
                    ChessboardSize="40vw"
                    setFen={setFenCallback}
                    fen={fen}
                    isCurrentUserWhite={true}
                    isWhitePlayTurn={yourTurn}
                    gameIsStarted={true}
                    playAgainstComputer={true}
                    onFirstMove={onFirstMove}
                    difficulty={difficulty}
                    handleEndGame={handleEndGame}
                    gameId={null}
                />
            </div>
            <div className="LobbyOptionsContainer">
                <div className="LobbyName">
                    <p>Computermatch</p>
                </div>
                        <ButtonComponent
                        specificClassName={"LobbyOptionsButtonsStarted"}
                        text="Ask ChatGPT"
                        buttonID="chatGPTButton"
                        onClick={handleBotButton}
                        id="chatGPTButton"
                        />

                        <ButtonComponent
                            specificClassName="LobbyOptionsButtonsStarted"
                            text="Give up"
                            buttonID="giveUpButton"
                            onClick={handleGiveUpButton}
                        />

            </div>
        </>
    );
}

export default PlayAgainstComputer;
