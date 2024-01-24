import React, {useEffect, useState} from 'react';
import './LeaderboardPage.css';
import LeaderBoard from '../../assets/images/leaderboard_icon.svg';
import {useFetchUsers} from "../../services/UserService";


function LeaderboardPage(){

    const{data:users, isLoading, isError} = useFetchUsers();
    const [sortedUsers, setSortedUsers] =useState([]);

    useEffect(() => {
        if(users) {
            // Benutzer werden in absteigender Reihenfolge nach den Punkten sortiert
            // Voraussetzung, dass jedes User-Objekt ein 'points'-Attribut hat
            const sorted = [...users].sort((a,b)=>b.score-a.score);
            setSortedUsers(sorted);
        }
    }, [users]);

    if(isLoading) return <div>Loading...</div>;
    if(isError) return <div> Error!</div>;

    return (
        <div className="Leaderboard">
            <div className="LeaderboardHeader">
                <img className="LeaderbordIcon" src={LeaderBoard} alt="LeaderbordeIcon"/>
                <h1>Leaderboard</h1>
            </div>
            <div className="LeaderboardList">
                {sortedUsers.map((user, index) => (
                    <div className="UserBox" key={user.userName}>
                        <span className="Rank">{index + 1}.</span>
                        <span className="Nickname" >{user.userName}</span>
                        <span className="Points">{user.score} Points</span>

                    </div>
                    ))}

            </div>
        </div>
    );
}

export default LeaderboardPage;