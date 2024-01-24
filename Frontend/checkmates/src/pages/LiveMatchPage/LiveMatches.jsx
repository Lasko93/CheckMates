import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import "../../components/Buttons/ButtonComponent.css";
import "./liveMatch.css"

const LiveMatches = () => {
    const [matches, setMatches] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchGames = async () => {
            try {
                const response = await fetch(`http://localhost:8080/game/find-all`);
                if (!response.ok) {
                    throw new Error('Failed to fetch live matches');
                }
                const liveMatches = await response.json();
                setMatches(liveMatches);
            } catch (error) {
                console.error('Error fetching live matches:', error);
            }
        };
        fetchGames();
    }, []);

    const goToMatch = (id) => {
        navigate(`/lobby/${id}`);
    };

    return (
        <div className='profile'>
            <h1>Live Chess Matches</h1>
            <ul className='matches-list'>
                {matches.length > 0 ? (
                    matches.map(match => (
                        <li key={match.id} className="match-item">
                                <div className="match-name"> {match.gameName.value} </div>
                            <button onClick={() => goToMatch(match.id)} className="go-to-match-btn">
                                Go to Match
                            </button>
                        </li>
                    ))
                ) : (
                    <li>No live matches available.</li>
                )}
            </ul>
        </div>
    );
};

export default LiveMatches;
