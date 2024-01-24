import React, {useEffect, useState} from "react";
import './ShowAgainPage.css';
import {useNavigate, useParams} from "react-router-dom";
import {useQueryClient} from "react-query";
import {useFetchGameById} from "../../services/GameService";
import {useFetchProfilePicture} from "../../services/UserService";
import ChessboardComponent from "../../components/ChessboardComponent/ChessboardComponent";
import LobbyProfile from "../../components/LobbyComponents/LobbyProfileComponent/LobbyProfile";
import ProfilePicture from '../../assets/images/PlayerIcon.svg';
import ButtonComponent from "../../components/Buttons/ButtonComponent";
import ReplayGameService from "../../services/ReplayGameService";
import {Chess} from 'chess.js';

function ShowAgainPage(){

    //Chessgame load pgn method to load the pgn game then access with chessgame.history to get the moves displayed
    const [chessGame, setGame] = useState(new Chess());

    const { gameId } = useParams();
    const [gameDetails, setGameDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const queryClient = useQueryClient();
    const { data: game, isLoading: isLoadingGame, isError: isErrorGame } = useFetchGameById(gameId);
    console.log("Black Player:",gameDetails?.black?.userName);
    const navigate = useNavigate();
    const [SearchPlayerPopup, setSearchPlayerPopup] = useState(false);
    const [fen, setFen] = useState("");
    const [fenIndex, setFenIndex] = useState(0);
    const [BlackPlayerReady, setBlackPlayerReady] = useState(false);
    const profilePictureWhite = useFetchProfilePicture(game?.white?.username);
    const profilePictureBlack= useFetchProfilePicture(game?.black?.username);
    const [PGNArray, setPGNArray] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await ReplayGameService.fetchGameById(gameId);
                console.log("API-Daten geladen:", data);
                setGameDetails(data);
            } catch (error) {
                console.error("API-Fehler:", error);
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [gameId]);


    useEffect(() => {
        if(PGNArray.length !== 0) {
            setPGNArray(JSON.parse(sessionStorage.getItem('PGNArray')))
        }
        console.log(PGNArray)
    },[]);

    //fetching the profile pictures and converting them into url to display
    let profilePictureWhiteUrl =  profilePictureWhite?.data?.images
        ? `data:${profilePictureWhite.data.type};base64,${profilePictureWhite?.data?.images}`
        : ProfilePicture;

    let profilePictureBlackUrl =  profilePictureBlack?.data?.images
        ? `data:${profilePictureBlack.data.type};base64,${profilePictureBlack?.data?.images}`
        : ProfilePicture;

    useEffect(() => {
        // Prüfen, ob gameDetails.fen vorhanden und länger als 0 ist
        if (gameDetails && gameDetails.fen && gameDetails.fen.length > 0) {
            // Wenn ja, verwende gameDetails.fen
            if (fenIndex < gameDetails.fen.length) {
                const fenString = gameDetails.fen[fenIndex]?.value || "";
                console.log("Aktuelle FEN-Notation aus gameDetails:", fenString);
                setFen(fenString);
            }
        } else {
            // Wenn nicht, verwende FENs aus PGNArray
            if (PGNArray && fenIndex < PGNArray?.length) {
                const fenString = PGNArray[fenIndex] || "";
                console.log("PGNARRAY", PGNArray);
                console.log("Aktuelle FEN-Notation aus PGNArray:", fenString);
                setFen(fenString);
            }
        }
    }, [gameDetails, PGNArray, fenIndex]);



    function showNextMove() {
        let maxIndex = gameDetails && gameDetails.fen ? gameDetails?.fen?.length - 1 : PGNArray?.length - 1;
        console.log("pgnarray:", PGNArray);
        if (fenIndex < maxIndex) {
            setFenIndex(fenIndex + 1);
        }
    }

    function showPreviousMove() {
        if (fenIndex > 0) {
            setFenIndex(fenIndex - 1);
        }
    }


    return(

        <div>

            <div className="header">
            <h1>Rewatch Game {gameId}</h1>
                <br/><br/><br/>
                <div>
                    <LobbyProfile
                        ContainerName="PlayerBlack2"
                        ProfileImage={profilePictureBlackUrl}
                        PlayerName={gameDetails?.black?.userName || "Black Player"}
                        PlayerPoints={gameDetails?.black?.score || 0}
                        isReady={BlackPlayerReady}
                    />
                    <LobbyProfile
                        ContainerName="PlayerBrown2"
                        ProfileImage={profilePictureWhiteUrl}
                        PlayerName={gameDetails?.white?.userName || "White Player"}
                        PlayerPoints={gameDetails?.white?.score || 0}
                    />
                </div>
                <div className="ChessPositionerGameLobby2">
                    <ChessboardComponent
                        ChessboardSize="40vw"
                        gameId={gameId}
                        playAgainstComputer={false}
                        fen={fen}
                    />
                </div>
                <div className="controls">
                {/* Steuerungselemente für Spielzüge */}
                    <ButtonComponent
                        specificClassName={`PrevMoveButton`}
                        text="Previous Move"
                        buttonID="PrevMoveButton"
                        onClick={showPreviousMove}
                        disabled={fenIndex <= 0}
                    />
                    <ButtonComponent
                        specificClassName={`NextMoveButton`}
                        text="Next Move"
                        buttonID="NextMoveButton"
                        onClick={showNextMove}
                        disabled={fenIndex >= (gameDetails && gameDetails.fen ? gameDetails.fen.length - 1 : PGNArray.length - 1)}

                    />

                </div>
            </div>
        </div>

    );
}
export default ShowAgainPage;