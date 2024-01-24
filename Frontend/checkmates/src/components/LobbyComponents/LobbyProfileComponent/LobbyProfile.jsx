import React from 'react';
import "./LobbyProfile.css"

const LobbyProfile = ({ ContainerName, ProfileImage, PlayerName, PlayerPoints, isReady,TimerID,TimerMinutes,TimerSeconds, isCurrentTurn, showTimer }) => {
    const timerClass = isCurrentTurn ? "timer-active" : "timer-inactive";

    return (
        <div className={`${ContainerName}`}>
            {showTimer && (
            <div id={TimerID} className={`LobbyTimer ${timerClass}`}>
                <div className="TimerMinutes">
                        <p>{TimerMinutes}</p>
                </div>
                <div>
                    <p>
                        :
                    </p>
                </div>
                <div className="TimerSeconds">
                        <p>{TimerSeconds}</p>
                </div>
            </div>
                )}
            <div className={`OuterCard ${isReady ? 'ready-background' : ''}`}>
                <div className="CircledProfile">
                    <img src={ProfileImage} alt="Player Profile" />
                </div>
                <div className="NameAndPoints">
                    <div className="PlayerName" >
                        <p>{PlayerName}</p>
                    </div>
                    <div className="PlayerPoints">
                        <p>{PlayerPoints}</p>
                    </div>
                </div>
            </div>
        </div>
            )
}

export default LobbyProfile;