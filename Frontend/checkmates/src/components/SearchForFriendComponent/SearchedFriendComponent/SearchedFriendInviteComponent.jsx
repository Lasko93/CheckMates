import React from "react";
import '../../SearchForPlayerComponent/SearchedPlayerComponent/SeachedPlayerInvite.css'
import InviteFriendButtonComponent from "../InviteFriendButtonComponent";

const SearchedFriendInvite = ({playerName, chessClub, elo, onInviteClick }) => {
    return (
        <div className="PlayerInvite">
            <div className="SearchingDivs">
                <p>{playerName}</p>
            </div>
            <div className="SearchingDivs">
                <p>{chessClub}</p>
            </div>
            <div className="SearchingDivs">
                <p>{elo}</p>
            </div>
            <div className="InviteButtonsDiv">
                <InviteFriendButtonComponent onClick={() => onInviteClick(playerName)} initialText="Add Friend" specificClassName="InviteButtons" />
            </div>

        </div>
    );
}

export default SearchedFriendInvite