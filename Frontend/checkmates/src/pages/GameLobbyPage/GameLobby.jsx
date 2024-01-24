import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import './GameLobby.css'
import ChessboardComponent from "../../components/ChessboardComponent/ChessboardComponent";
import ButtonComponent from "../../components/Buttons/ButtonComponent";
import LobbyProfile from "../../components/LobbyComponents/LobbyProfileComponent/LobbyProfile";
import SearchForPlayerPopup from "../../components/SearchForPlayerComponent/SearchForPlayerPopup";
import ProfilePicture from '../../assets/images/PlayerIcon.svg'
import {useFetchProfilePicture} from "../../services/UserService";
import {
    getAssistance,
    useDeleteGame,
    useFetchGameById,
    useUpdateGameRemoveBlack,
    useUpdateTime
} from "../../services/GameService";
import CheckActiveMatchComponent from "../../components/CheckActiveMatchComponent/CheckActiveMatchComponent";
import {toast} from "react-toastify";
import {useQueryClient} from "react-query";
import webSocketService from "../../services/WebSocketService";


function GameLobby() {
    const { gameId } = useParams();
    const queryClient = useQueryClient();
    const { data: game } = useFetchGameById(gameId);
    const navigate = useNavigate();
    const [SearchPlayerPopup, setSearchPlayerPopup] = useState(false);
    const [fen, setFen] = useState("");
    const [MatchTimer, setMatchTimer] = useState("5");
    const [BlackPlayerReady, setBlackPlayerReady] = useState(false);
    const [actualGameStatus, setActualGameStatus] = useState(game?.status);
    const [activeMatchPopup, setActiveMatchPopup] = useState(false);
    const currentUser = sessionStorage.getItem('userName');
    const isCurrentUserBlack = game && game?.black && game.black?.username === currentUser;
    const isCurrentUserWhite = game && game?.white && game.white?.username === currentUser;
    //ToDo: Implement Streamer Mode
    //Insert Check if current user is Streaming --> if yes --> just disable everything like buttons and chessboard
    const profilePictureWhite = useFetchProfilePicture(game?.white?.username);
    const profilePictureBlack= useFetchProfilePicture(game?.black?.username);

    const {mutate: removeBlackPlayer } = useUpdateGameRemoveBlack();
    const [whiteTimerMinutes, setWhiteTimerMinutes] = useState(0);
    const [whiteTimerSeconds, setWhiteTimerSeconds] = useState(0);
    const [blackTimerMinutes, setBlackTimerMinutes] = useState( 0);
    const [blackTimerSeconds, setBlackTimerSeconds] = useState(0);
    const [whitePlayerTurn, setWhitePlayerTurn] = useState(game?.whitePlayerTurn);
    const [gameIsStarted, setGameStarted] = useState(false);
    const [ranOutOfTime, setRanOutOfTime] = useState("");
    const [isViewer, setIsViewer] = useState(false);

    /* //Declarations for pausing the game
    const [isGamePaused, setIsGamePaused] = useState(false);
    const [whitePlayerPauseCount, setWhitePlayerPauseCount] = useState(0);
    const [blackPlayerPauseCount, setBlackPlayerPauseCount] = useState(0);
*/

    //Function to display time in the right format
    function formatTime(time) {
        return time < 10 ? `0${time}` : time;
    }

    useEffect(() => {
        webSocketService.disconnect();
    }, []);


    //fetching the profile pictures and converting them into url to display
    let profilePictureWhiteUrl =  profilePictureWhite?.data?.images
        ? `data:${profilePictureWhite.data.type};base64,${profilePictureWhite?.data?.images}`
        : ProfilePicture;

    let profilePictureBlackUrl =  profilePictureBlack?.data?.images
        ? `data:${profilePictureBlack.data.type};base64,${profilePictureBlack?.data?.images}`
        : ProfilePicture;

    // popup for whiteplayer to handle lobbyclossure
    const toggleActiveMatchPopup = () => {
        setActiveMatchPopup(!activeMatchPopup);
    };

    // poopup for playerinvites
    const togglePopup = () => {
        setSearchPlayerPopup(!SearchPlayerPopup);
    };


    //GameStatus for Backend
    const GameStatus = {
        PLAYING: 'PLAYING',
        FINISHED: 'FINISHED',
        PENDING: 'PENDING',
        STOPPED: 'STOPPED',
        ACCEPTED: 'ACCEPTED',
        READY: 'READY'
    };

    useEffect(() => {
        if (game && currentUser) {
            const isPlayer =
                (game.black && game.black.username === currentUser) ||
                (game.white && game.white.username === currentUser);
            setIsViewer(!isPlayer);
        }
    }, [game, currentUser]);





    //Handle Display and rerendering of GameData
    useEffect(() => {
        //ensure that boardstatus is set to the right value on rejoin
        if (game && game?.boardStatus?.value) {
            setFen(game?.boardStatus?.value);
        }

        if (game && game.timer) {
            setWhiteTimerMinutes(game?.timer?.whiteTimerMinutes || 0);
            setWhiteTimerSeconds(game?.timer?.whiteTimerSeconds || 0);
            setBlackTimerMinutes(game?.timer?.blackTimerMinutes || 0);
            setBlackTimerSeconds(game?.timer?.blackTimerSeconds || 0);
        }
        if (game && game?.status===GameStatus.PLAYING) {
            setGameStarted(true);
        }

       /* syncing the pause counter
       if (game) {
            setBlackPlayerPauseCount(game?.whitePlayerPauseCount);
            setWhitePlayerPauseCount(game?.blackPlayerPauseCount);
            console.log(game?.whitePlayerPauseCount);
            console.log(game?.blackPlayerPauseCount);
        }*/

        if (game?.status) {
            setActualGameStatus(game.status);
        }

        if (game?.whitePlayerTurn) {
            setWhitePlayerTurn(game.whitePlayerTurn);
        }

        }, [game]);


    //HandleWebsocket
    useEffect(() => {

//////////////////////////////////////Handling all of the websocket messages received//////////////////////////////////
        //Handle endgameUpdates
        const handleEndGameUpdate = (message) => {
        if (message.draw) {
            toast.info("The game ended in a draw")

        }
       else if (message.gaveUp) {
            if (message.winner === currentUser) {
                toast.success("You won the game because your opponent gave up")
            }
            else {
                toast.error("You lost the game because you gave up")
            }
        }
       else if(message.winner===currentUser) {
                toast.success("You won the game")
            }
       else {
                toast.error("You lost the game")
            }
        }

        //Handle GameStatusUpdate from WebsocketServer
        const handleGameStatusUpdate = (message) => {
            if (message === "READY") {
                setBlackPlayerReady(true);
                setActualGameStatus(message);
                if (isCurrentUserWhite) {
                    if (game?.black?.username) {
                        toast.success(`${game?.black?.username} is ready`)
                    }
                    else
                        {
                            toast.success("Black Player is ready")
                        }
                }
                //if accepted is sent from the controller --> Player withdrawn the ready status
            } else if (message === "ACCEPTED") {
                setBlackPlayerReady(false);
                setActualGameStatus(message);
                if (isCurrentUserWhite) {
                    toast.error(`${game?.black?.username} has withdrawn his ready`)
                }
            }

            //use gamestarted to hide/ display Buttons and reset the ready for background
            else if (message === "PLAYING") {
                setGameStarted(true)
                setBlackPlayerReady(false);
            }

            //used for kicking people
            else if (message === "PENDING" && isCurrentUserBlack && game?.black?.username===currentUser) {
                toast.info("You have been removed from the game.");
                navigate('/');
            }

            //Cleanup and check if someone ran out of time
            else if(message === "FINISHED") {
                if (ranOutOfTime !== "") {
                    if (ranOutOfTime === currentUser) {
                        toast.error("You lost the game because you ran out of time")
                    }
                    else {
                        toast.success("You won the game because your opponent ran out of time")
                    }

                }
                navigate("/");
            }
        };

        //handle GameBoardUpdate from WebsocketServer
        const handleGameBoardUpdate = (gameBoardUpdate) => {
            console.log("This is my GameBoardUpdate")
            console.log(gameBoardUpdate)
            //set the board after a move to the fen
            setFen(gameBoardUpdate?.fen);
            //swap turns for displaying the timer right
            setWhitePlayerTurn(gameBoardUpdate?.whitePlayerTurn);

            if (gameBoardUpdate?.checkMate){
                toast.info("Checkmate!");

            }
            else if (gameBoardUpdate?.check){
                toast.info("Check!");
            }
        };



       //handle GameTimerUpdate from WebsocketServer
        const handleGameTimerUpdate= (message) => {
            setWhiteTimerMinutes(message?.whiteTimerMinutes);
            setWhiteTimerSeconds(message?.whiteTimerSeconds);
            setBlackTimerMinutes(message?.blackTimerMinutes);
            setBlackTimerSeconds(message?.blackTimerSeconds);

                if (message?.whiteTimerMinutes === 0 && message?.whiteTimerSeconds === 1) {
                    setRanOutOfTime(game?.white?.username);

                }
                else if (message?.blackTimerMinutes === 0 && message?.blackTimerSeconds === 1) {
                    setRanOutOfTime(game?.black?.username);

            }

        }

/*      not working yet
        //handle PauseOrResume from WebsocketServer
        const handlePauseOrResume = (message) => {
            setIsGamePaused(!message?.pause);

            if (message?.pause) {
                if (message.playerColor === "white") {
                    toast.info(game?.white?.username + " has paused the game for 2 minutes")
                } else {
                    toast.info(game?.black?.username + " has paused the game for 2 minutes")
                }
            }

             if (!message?.pause) {

                 if (message.playerColor === "white") {
                     toast.info(game?.white?.username + " has resumed the game")
                 } else {
                     toast.info(game?.black?.username + " has resumed the game")
                 }
             }
            }
*/


        //Connect the websocket Service and subscribe
        if (gameId) {
            webSocketService.connect(
                () => {
                    console.log('WebSocket connected');
                    const StatusSubscription = webSocketService.subscribe(`/topic/game/${gameId}/gameStatus`, handleGameStatusUpdate);
                    webSocketService.subscribe(`/topic/game/${gameId}/updates`, handleGameBoardUpdate);
                    webSocketService.subscribe(`/topic/game/${gameId}/timer`, handleGameTimerUpdate)
                    webSocketService.subscribe(`/topic/game/${gameId}/endGame`, handleEndGameUpdate)
                  //  const pauseOrResumeSubscription = webSocketService.subscribe(`/topic/game/${gameId}/pauseOrResume`, handlePauseOrResume);

                    return () =>
                        webSocketService.unsubscribe(StatusSubscription);
                },
                error => console.error('WebSocket error', error)
            );
        }


        return () => {
            webSocketService.disconnect();
        };
    }, [actualGameStatus, isCurrentUserWhite, game?.black?.username, ranOutOfTime]);


///////////////////////////////////////Defining Sending Websocket Methods///////////////////////////////////////////////
    //Send GameStatusUpdate to WebsocketServer
    const sendGameStatusUpdate = (statusValue, gameId) => {
        const statusUpdate = {
            status: statusValue,
            gameId: gameId
        };

        webSocketService.sendMessage(`/app/${gameId}/game.updateStatus`, JSON.stringify(statusUpdate));
    };

    const sendEndGameUpdate = (gameId, winner, isDraw, gaveUp) => {
        const endGameUpdate = {
            gameId: gameId,
            winner: winner,
            isDraw: isDraw,
            gaveUp: gaveUp
        };
        webSocketService.sendMessage(`/app/game/${gameId}/endGame`, JSON.stringify(endGameUpdate));
    }
/*
    const sendPauseOrResumeUpdate = (gameId, playerColor, pause) => {
        const pauseOrResumeUpdate = {
            gameId: gameId,
            playerColor: playerColor,
            pause: pause
        };
        webSocketService.sendMessage(`/app/game/${gameId}/pauseOrResume`, JSON.stringify(pauseOrResumeUpdate));
    }
*/


///////////////////////////////Defining the button functionalities///////////////////////////////////////////////////////

    //Handle StartGameButton
    const startGame =  () => { // Make the function async
        console.log(MatchTimer);
        if(BlackPlayerReady) {
            sendGameStatusUpdate(GameStatus.PLAYING, gameId);
        }
        else
            {
                toast.error("Black Player is not ready yet");
            }
        };

    //Handle GetReadyButton for black player
    const handleGetReadyButton = () => {
        if (actualGameStatus=== GameStatus.READY) {
            sendGameStatusUpdate(GameStatus.ACCEPTED,gameId);
        }
        else {
            sendGameStatusUpdate(GameStatus.READY,gameId);
        }
    }

    //Handle Leave Lobby
    const handleLeaveButton = () => {
        if (isCurrentUserBlack) {
            handleLeaveAsBlack();
        } else if (isCurrentUserWhite) {
            handleLeaveAsWhite();
        } else {
            // Handle case where the current user is neither black nor white --> Not necessary yet maybe for Stream?!
        }
    };

    //Handle Leave Lobby as Black
    const handleLeaveAsBlack = () => {
        removeBlackPlayer(gameId, {
            onSuccess: () => {
                queryClient.invalidateQueries(['game', gameId]);
                sendGameStatusUpdate(GameStatus.PENDING,gameId);
                setBlackPlayerReady(false);
                navigate('/');
            },
            onError: (error) => {

                console.error('Error removing black player:', error);
            },
        });
    };

    //Handle Leave Lobby as White
    const handleLeaveAsWhite = async () => {
        toggleActiveMatchPopup();
            }



    //Handle Kick Black Player
    const kickBlackPlayer = () => {
        if (!game || !game.black) {
           toast.error("There is no black player to kick")

        }
        else {
            removeBlackPlayer(gameId, {
                onSuccess: () => {

                    sendGameStatusUpdate(GameStatus.PENDING,gameId);
                    setBlackPlayerReady(false);
                    queryClient.invalidateQueries(['game', gameId]);
                },
                onError: (error) => {
                    console.error('Error kicking black player:', error);
                },
            });
        }
    };


    const handleGiveUpButton = () => {
        if(isCurrentUserBlack){
            sendEndGameUpdate(gameId, game?.white?.username, false, true);
        }
        else {
            sendEndGameUpdate(gameId, game?.black?.username, false, true);
        }
        sendGameStatusUpdate(GameStatus.FINISHED,gameId);
    }

/* Not working yet
    const handlePauseResume = () => {
        if (currentUser === game?.white?.username && whitePlayerPauseCount >= 2 && !isGamePaused) {
            toast.error("You can only pause the game 2 times")
            return;
        }
        if (currentUser === game?.black?.username && blackPlayerPauseCount >= 2 && !isGamePaused) {
            toast.error("You can only pause the game 2 times")
            return;
        }
        const playerColor = isCurrentUserWhite ? "white" : "black";
        sendPauseOrResumeUpdate(gameId,playerColor, isGamePaused);
    };
*/
    const handleBotButton = async () => {
        const color = isCurrentUserWhite ? "white" : "black";
        if (isCurrentUserBlack && !whitePlayerTurn || isCurrentUserWhite && whitePlayerTurn) {
            const BestMoveDTO = await getAssistance(fen, 3, gameId, color);
            if (BestMoveDTO) {
                const points = color==="white"? BestMoveDTO?.pointsWhite : BestMoveDTO?.pointsBlack;
                toast.success(<div>Best move: {BestMoveDTO?.bestove}<br />your winning points have decreased to {points}</div> , {
                    position: toast.POSITION.BOTTOM_CENTER,
                    autoClose: 6500
                });
            }

        }
      else {
          toast.error("It's not your turn")
        }
    }

    return (
        <div className="GameLobbyContentWrapper">
            <SearchForPlayerPopup isOpen={SearchPlayerPopup} onClose={togglePopup} gameId={gameId}/>
            <CheckActiveMatchComponent
                isOpen={activeMatchPopup}
                onClose={toggleActiveMatchPopup}
                text="Are you sure? Lobby will be destroyed...."
                inGame={true}
            />
            <div>
                <LobbyProfile
                    ContainerName="PlayerBlack"
                    ProfileImage={profilePictureBlackUrl}
                    PlayerName={game?.black?.username || "Waiting for Black Player..."}
                    PlayerPoints={game?.black?.score || 0}
                    isReady={BlackPlayerReady}
                    TimerID="PlayerBlackTimer"
                    TimerMinutes={formatTime(blackTimerMinutes)}
                    TimerSeconds={formatTime(blackTimerSeconds)}
                    isCurrentTurn={!whitePlayerTurn}
                    showTimer={true}
                   />
                <LobbyProfile
                    ContainerName="PlayerBrown"
                    ProfileImage={profilePictureWhiteUrl }
                    PlayerName={game?.white?.username || "Waiting...."}
                    PlayerPoints={game?.white?.score || 0 }
                    TimerID="PlayerWhiteTimer"
                    TimerMinutes={formatTime(whiteTimerMinutes)}
                    TimerSeconds={formatTime(whiteTimerSeconds)}
                    isCurrentTurn={whitePlayerTurn}
                    showTimer={true}
                />
            </div>
            <div className="ChessPositionerGameLobby">
                <ChessboardComponent
                    ChessboardSize="40vw"
                    gameId={gameId}
                    fen={fen}
                    whitePlayerTurn={whitePlayerTurn}
                    isCurrentUserWhite={isCurrentUserWhite}
                    isCurrentUserBlack={isCurrentUserBlack}
                    gameIsStarted={gameIsStarted}
                    playAgainstComputer={false}
                    isBoardEnabled={!isViewer}
                />
            </div>
            <div className="LobbyOptionsContainer">
                <div className="LobbyName">
                    <p>{game?.gameName?.value || "This Lobby is closed"}</p>
                </div>
                {/* GameTimer, but updating it won't work yet
                {!gameIsStarted &&(
                    <div className="TimeSettings">
                        <p id="LobbyMinutes"> Minutes </p>
                        <input className="MatchTimeInput"
                               type="number"
                               value={formatTime(MatchTimer)}
                               onChange={HandleMatchTimeOnChange}
                               placeholder={formatTime(MatchTimer)}
                               min="01"
                               disabled={!isCurrentUserWhite}
                        />
                    </div>
                )}*/}


                {!isViewer && !gameIsStarted &&(
                    <ButtonComponent
                        specificClassName={`LobbyOptionsButtons ${isCurrentUserBlack ? 'SmallButton' : ''}`}
                        text="Leave Lobby"
                        buttonID="DestroyLobbyButton"
                        onClick={handleLeaveButton}
                    />
                )}
                {/* Render black Buttons */}
                {!isViewer && !isCurrentUserWhite && !gameIsStarted &&(
                    <>
                        <ButtonComponent
                            specificClassName={`LobbyOptionsButtons ${isCurrentUserBlack ? 'SmallButton' : ''}`}
                            text="Get Ready"
                            buttonID="GetReadyButton"
                            onClick={handleGetReadyButton}
                        />
                    </>
                )}

                {/* Render white Buttons */}
                {!isCurrentUserBlack && !gameIsStarted && !isViewer &&(
                    <>
                        <ButtonComponent
                            specificClassName="LobbyOptionsButtons"
                            text="Kick Player"
                            buttonID="kickPlayer"
                            onClick={kickBlackPlayer}
                        />
                        <ButtonComponent
                            onClick={togglePopup}
                            specificClassName="LobbyOptionsButtons"
                            text="Invite a Player"
                            buttonID="LobbyInviteButton"
                        />
                        <ButtonComponent
                            specificClassName="LobbyOptionsButtons"
                            text="Start Game"
                            buttonID="StartGameButton"
                            onClick={startGame}
                        />
                    </>)}
                {/* Render GameStarted Buttons Buttons */}
                {gameIsStarted && !isViewer &&(
                    <>{/*
                    <ButtonComponent
                        specificClassName="LobbyOptionsButtonsStarted"
                        text={isGamePaused ? "Resume Game" : "Pause Game"}
                        buttonID="PauseResumeButton"
                        onClick={handlePauseResume}
                        disabled={isGamePaused ? (isCurrentUserWhite ? whitePlayerPauseCount >= 2 : blackPlayerPauseCount >= 2) : false}
                    />*/}
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

                        {/*}
                         <ButtonComponent
                            specificClassName="LobbyOptionsButtonsStarted"
                            text="Checkmate"
                            buttonID="checkMateButton"
                            onClick={testCheckmate}
                        />*/}
                    </>
                    )}
            </div>
        </div>
    );
}

export default GameLobby;
