import './ChessClubs.css';
import {useFetchAllClubs, useFetchClubsByMember, useLeaveClub} from "../../services/ChessClubService";
import ButtonComponent from "../../components/Buttons/ButtonComponent";
import ChessClubPopup from "../../components/ChessClubPopup/ChessClubPopup";
import {useState} from "react";
import ClubList from "./clubList";

const ChessClubs = () => {

    const {data: chessClubs} = useFetchAllClubs();
    const userName = sessionStorage.getItem('userName');
    const {mutate: leaveClub} = useLeaveClub();
    const {data: myClubName} = useFetchClubsByMember(userName);
    console.log("My Club Name: ");
    console.log(myClubName?.clubName);


    console.log("Chess Clubs: ");
    console.log(chessClubs);



    const [showPopup, setShowPopup] = useState(false);


    const togglePopup = () => {
        setShowPopup(!showPopup);
    }


    const handleLeaveClub = () => {
        const clubRequest = {
            clubName: myClubName?.clubName,
            userName: userName
        }
        leaveClub(clubRequest);
    }

    return (
        <div>
            {showPopup && <ChessClubPopup closePopup={togglePopup}/>}
                <div align="center" id="ChessClubTitle">
                    <p>Chess Clubs</p>
                </div>
                <div className="ClubListContainer">
                    <ClubList clubs={chessClubs || []} specificClass="ClubList"/>
                </div>
                <div className="ChessClubButtons">
                    <ButtonComponent text="Leave Chessclub" specificClassName="ChessClubButton" onClick={handleLeaveClub}/>
                    <ButtonComponent text="Create Chessclub" onClick={togglePopup} specificClassName="ChessClubButton"/>
                </div>

        </div>

    );
}

export default ChessClubs;
