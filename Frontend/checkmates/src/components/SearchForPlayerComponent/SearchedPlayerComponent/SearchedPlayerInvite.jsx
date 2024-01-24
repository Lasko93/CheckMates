import React from "react";
import './SeachedPlayerInvite.css'
import InviteButtonComponent from "../InviteButtonComponent";

const SearchedPlayerInvite = ({playerName, chessClub, elo, onInviteClick }) => {
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
               <InviteButtonComponent onClick={() => onInviteClick(playerName)} initialText="Invite Player" specificClassName="InviteButtons" />
           </div>

       </div>
    );
}

export default SearchedPlayerInvite