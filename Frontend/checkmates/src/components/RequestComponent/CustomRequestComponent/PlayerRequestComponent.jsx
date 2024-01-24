import React from "react";
import "./PlayerRequestComponent.css"
import acceptIcon from "../../../assets/images/accept_icon.svg"
import declineIcon from "../../../assets/images/decline_icon.svg"

const PlayerRequestComponent = ({InvitingPlayer, InviteText, acceptButton, declineButton}) => {
    return(
        <div className="PlayerRequestComponentContainer">
            <div className="InvitingPlayer">
                <p>{InvitingPlayer}</p>
            </div>
            <div className="RequestText">
                <p>{InviteText}</p>
            </div>
            <div className="RequestButtons">
                <div className="ButtonGreyBackground">
                    <img src={acceptIcon} alt={acceptIcon} onClick={acceptButton}/>
                </div>
                <div className="ButtonGreyBackground" onClick={declineButton}>
                    <img src={declineIcon} alt={declineIcon}/>
                </div>

            </div>
        </div>
    )
}


export default PlayerRequestComponent;