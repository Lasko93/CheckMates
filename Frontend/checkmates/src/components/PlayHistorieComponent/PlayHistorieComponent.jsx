import React from 'react';
import './PlayHistorieComponent.css';
import {useQuery} from "react-query";
import {useNavigate} from "react-router-dom";


const fetchLastThreeGames = async (userName) => {
    try {
        const response = await fetch(`http://localhost:8080/api/v1/game/history/lastThree/player/${userName}`);
        console.log("Hellooooo",response);
        const responseBody = await response.text(); // Erstmal als Text holen

        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${responseBody}`);
        }

        try {
            const jsonData = JSON.parse(responseBody); // Versuchen, als JSON zu parsen
            return jsonData;
        } catch (jsonError) {
            throw new Error(`Error parsing JSON: ${jsonError.message}`);
        }
    } catch (error) {
        console.error('Fehler beim Abrufen der letzten drei Spiele:', error);
        throw error;
    }
};
const downloadPgn = async (gameId) => {
    try {
        const response = await fetch(`http://localhost:8080/pgn/download-gh/${gameId}`);
        console.log("PGN", response)
        const data = await response.blob();
        const url = window.URL.createObjectURL(data);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', `game_${gameId}.pgn`);
        document.body.appendChild(link);
        link.click();
        link.remove();
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error('Error downloading PGN file:', error);
    }
};

const PlayHistorieComponent=({userName})=> {
    const navigate = useNavigate();
    const {data: games, isLoading, isError} = useQuery(['lastThreeGames', userName], () =>fetchLastThreeGames(userName));
    <p>{games}</p>
    if(isLoading)return <div>Loading...</div>;
    if(isError) return <div>Error loading games</div>;
    const viewGame = (gameID)=> {
        navigate(`/ShowAgainPage/${gameID}`)
    }


    return(
        <div className='historielist'>
            {games && games.map ((game, index)=> (
                <div className="GameBox" key={index}>
                    <h3>{index + 1}</h3>
                    <p>White: {game.white.userName}</p>
                    <p>Black: {game.black.userName}</p>
                    <p>Winner: {game.whitePlayerWon ? game.white.userName : game.black.userName}</p>
                    <button onClick={() => viewGame(game.gameId)}>View Game</button>
                    <button onClick={() => downloadPgn(game.gameId)}>Export PGN</button>

                </div>


            ))}
        </div>


    );

};

export default PlayHistorieComponent;