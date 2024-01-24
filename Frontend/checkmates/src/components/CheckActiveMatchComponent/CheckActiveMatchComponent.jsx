import React, {useEffect, useState} from "react";
import './CheckActiveMatchComponent.css'

//Components
import ButtonComponent from "../Buttons/ButtonComponent";

//Services and Hooks
import {
    checkUserInGamesAndGetId,
    useDeleteGame,
    useFetchGameById,
    useUpdateGameRemoveBlack
} from "../../services/GameService";
import {useNavigate} from "react-router-dom";
import webSocketService from "../../services/WebSocketService";

const CreateActiveMatchPopup = ({ isOpen, onClose, text, id, inGame }) => {
    const navigate = useNavigate();
    const username = sessionStorage.getItem('userName');
    const { mutate: deleteGame } = useDeleteGame();
    const { mutate: removeBlackPlayer } = useUpdateGameRemoveBlack();
    const [activeGameId, setActiveGameId] = useState(null);
    const { data: gameData, isLoading: isGameLoading } = useFetchGameById(activeGameId);

    useEffect(() => {
        // Define an async function inside useEffect
        const fetchGameId = async () => {
            try {
                const id = await checkUserInGamesAndGetId(username);
                setActiveGameId(id);
            } catch (error) {
                console.error("Error fetching game ID:", error);
                // Handle any errors, such as setting a state for error messages
            }
        };

        // Call the async function
        fetchGameId();

    }, [username, activeGameId, gameData]);


    const handleDeleteGame = async () => {
        if (!isGameLoading && gameData) {
            if (gameData.black?.username === username) {
                if(gameData?.status!=="PLAYING") {
                    removeBlackPlayer(gameData.id, {
                        onSuccess: () => console.log('Black player removed'),
                        onError: (error) => console.error('Error removing black player:', error),
                    });
                }
                else {
                    sendEndGameUpdate(id, gameData?.white?.username, false, true);
                }

            } else if (gameData.white?.username === username) {

                if (gameData?.status!=="PLAYING") {
                    deleteGame(gameData.id);
                    sendGameStatusUpdate("PENDING", gameData.id)
                }
                else {
                    sendEndGameUpdate(id, gameData?.black?.username, false, true);
                }

            }
        }
        onClose();
        navigate('/');
    };

    // Handler to continue with the active game
    const handleContinue = async () => {
        if(!inGame) {
            await webSocketService.disconnect();
            const activeGameId = await checkUserInGamesAndGetId(username);
            if (activeGameId) {
                navigate(`/lobby/${id}`);
            }
        }
            onClose();


    };

    const sendEndGameUpdate = (gameId, winner, isDraw, gaveUp) => {
        const endGameUpdate = {
            gameId: gameId,
            winner: winner,
            isDraw: isDraw,
            gaveUp: gaveUp
        };
        webSocketService.sendMessage(`/app/game/${id}/endGame`, JSON.stringify(endGameUpdate));
    }
    const sendGameStatusUpdate = (statusValue, gameId) => {
        const statusUpdate = {
            status: statusValue,
            gameId: gameId
        };

        webSocketService.sendMessage(`/app/${gameId}/game.updateStatus`, JSON.stringify(statusUpdate));
    };

    // Early return if popup is not open
    if (!isOpen) {
        return null;
    }

    return (
        <div className="popup-container">
            <div className="popup_isActive">
                <h2 className="PopupHeadlines">{text}</h2>
                <div className="PopupButtons">
                    <ButtonComponent text="Continue" buttonID="ContinueButton" onClick={handleContinue} />
                    <ButtonComponent text="Exit Lobby" buttonID="GiveUpButton" onClick={handleDeleteGame} />
                </div>
            </div>
        </div>
    );
};

export default CreateActiveMatchPopup;