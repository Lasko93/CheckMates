import React, {useState} from 'react';
import './ChessClubPopup.css';
import ButtonComponent from "../Buttons/ButtonComponent";
import {useJoinClub} from "../../services/ChessClubService";

const ChessClubPopup = ({ closePopup }) => {

    const userName = sessionStorage.getItem('userName');
    const [ChessClubName, setChessClubName] = useState("")

    const handleChessClubName = (event) => setChessClubName(event.target.value);
    const {mutate: joinClub} = useJoinClub();


    const handleCreateNewChessClub = () => {

        const clubRequest = {
            clubName: ChessClubName,
            userName: userName
        }
        joinClub(clubRequest);

        closePopup();

    };

    return (
        <div className="ChessClubPopup">
            <div className="ChessClubPopupInner">
                <h2>Create New Chess Club</h2>
                <input className="MatchNameInput" id="ChessClubNameInput"
                       type="text"
                       value={ChessClubName}
                       onChange={handleChessClubName}
                       placeholder="Enter a chessclub name...."
                />
                <div className="PopupButtonsClub">
                    <ButtonComponent
                        onClick={handleCreateNewChessClub}
                        specificClassName="ChessclubButtonsPopUp"
                        text="Create"

                    />
                    <ButtonComponent
                        onClick={closePopup}
                        text="Cancel"
                        specificClassName="ChessclubButtonsPopUp"
                    />
                </div>

            </div>
        </div>
    );
}

export default ChessClubPopup;
