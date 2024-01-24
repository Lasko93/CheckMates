import React, {useEffect, useState} from 'react';
import './HomePage.css'

//Components
import ChessboardComponent from "../../components/ChessboardComponent/ChessboardComponent";
import ButtonComponent from "../../components/Buttons/ButtonComponent";
import CreateActiveMatchPopup from "../../components/CheckActiveMatchComponent/CheckActiveMatchComponent";
import {checkUserInGamesAndGetId, useFetchGameById} from "../../services/GameService";
import CreateLobbyPopup from "../../components/LobbyComponents/CreateLobbyComponent/CreateLobbyPopup";
import {toast} from "react-toastify";

//Services and Hooks
import CustomRequest from "../../components/RequestComponent/CustomRequestComponent/CustomRequest";
import PlayVsComputerPopUp from "../../components/PlayVsComputerPopUp/PlayVsComputerPopUp";
import {useNavigate} from "react-router-dom";
import webSocketService from "../../services/WebSocketService";


function HomePage() {


    const username = sessionStorage.getItem('userName');

    // State for toggling popups
    const [activeMatchPopup, setActiveMatchPopup] = useState(false);
    const [lobbyPopup, setLobbyPopup] = useState(false);
    const [playVsComputer, setPlayVsComputer] = useState(false);
    const navigate = useNavigate();
    const [activeGameId, setActiveGameId] = useState(null);
    const { data: gameData, isLoading: isGameLoading } = useFetchGameById(activeGameId);

    useEffect(() => {
        let endGameSubscription;

        const handleEndGameUpdate = (message) => {
            if (message.draw) {
                toast.info("The game ended in a draw")

            }
            else if (message.gaveUp) {
                if (message.winner === username) {
                    toast.success("You won the game because your opponent gave up")
                }
                else {
                    toast.error("You lost the game because you gave up")
                }
            }
            else if(message.winner===username) {
                toast.success("You won the game")
            }
            else {
                toast.error("You lost the game")
            }
        }

        const fetchGameIdAndConnectWebSocket = async () => {
            try {
                if (!activeGameId) {
                    const id = await checkUserInGamesAndGetId(username);
                    if (id) {
                        setActiveGameId(id);


                    webSocketService.connect(
                        () => {
                            console.log('WebSocket connected');
                            endGameSubscription = webSocketService.subscribe(`/topic/game/${id}/endGame`, handleEndGameUpdate);
                        },
                        error => console.error('WebSocket error', error)
                    );}
                }

            } catch (error) {
                console.error("Error fetching game ID:", error);
                }
        };

        fetchGameIdAndConnectWebSocket();

        return () => {
            if (endGameSubscription) {
                webSocketService.unsubscribe(endGameSubscription);
            }
            webSocketService.disconnect();
        };
    }, [username]);



    const togglePlayVsComputerPopUp = () => {
        setPlayVsComputer(!playVsComputer);
    }

    const toggleActiveMatchPopup = () => {
        setActiveMatchPopup(!activeMatchPopup);
    };

    const toggleLobbyPopup = () => {
        setLobbyPopup(!lobbyPopup);
    };

    // Handler for Play button
    const handlePlayButton = async () => {
        if (!username) {
            toast.error('Please log in to play online');
            return;
        }

        const hasActiveMatches = await checkUserInGamesAndGetId(username);

        if (hasActiveMatches) {
            toggleActiveMatchPopup();
        } else {
            toggleLobbyPopup();
        }
    };

    const handlePlayVsComputer = () => {
        if (!username) {
            toast.error('Please log in to play against the computer');
            return;
        }

        if (sessionStorage.getItem('gameDataComputer')) {
            toast.info('You are already in a game against the computer');
            navigate('/playAgainstComputer');
        }

        togglePlayVsComputerPopUp();
        
    }

    return (
        <>
            <PlayVsComputerPopUp isOpen={playVsComputer} onClose={togglePlayVsComputerPopUp}/>
            <CustomRequest UserWithRequests={username} />
            <CreateActiveMatchPopup
                isOpen={activeMatchPopup}
                onClose={toggleActiveMatchPopup}
                text="You are still in a Lobby..."
                id={activeGameId}
            />
            <CreateLobbyPopup isOpen={lobbyPopup} onClose={toggleLobbyPopup} />
            <div className="ChessPositioner">
                <ChessboardComponent ChessboardSize="33vw" />
            </div>
            <div className="HomePageTitle">
                <h1>Connect, Challenge, and Conquer: Online Chess with Friends</h1>
            </div>
            <div className="StartButtons">
                <ButtonComponent text="Play Online" buttonID="PlayOnlineButton" onClick={handlePlayButton} />
                <ButtonComponent text="Challenge Computer" buttonID="ChallengeComputer" onClick={handlePlayVsComputer} />
            </div>
        </>
    );
}

export default HomePage;