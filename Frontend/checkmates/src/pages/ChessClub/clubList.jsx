import React from 'react';
import './Clublist.css';
import ButtonComponent from "../../components/Buttons/ButtonComponent";
import {useJoinClub} from "../../services/ChessClubService";
import {useNavigate} from "react-router-dom";

const ClubList = ({ clubs, specificClass }) => {

    const navigate = useNavigate();
    const userName = sessionStorage.getItem('userName');
    const {mutate: joinClub} = useJoinClub();

    const handleJoinClub = (club) => {

        const clubRequest = {
            clubName: club.clubName,
            userName: userName
        }
        joinClub(clubRequest);
    }

    const isMember = (club) => {
        return club.members.some(member => member.userName === userName);
    }


    const handleClubNameClick = (club) => {
        navigate(`/chessClub/${club.clubName}`);
    }

    return (
        <div className={specificClass}>
            {clubs.map((club, index) => (
                <div className={`ClubListItem ${isMember(club) ? 'ClubListItemMember' : ''}`} key={index}>
                    <div className="ChessClubName" onClick={() => handleClubNameClick(club)}>
                        {club.clubName}
                    </div>
                    <div className="ChessClubDescription">
                        Member in this Club: {club.members.length}
                    </div>
                    <div className="ListButtonContainer">
                        <ButtonComponent text="Join Club" className="ListButtons" onClick={() => handleJoinClub(club)}/>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default ClubList;
