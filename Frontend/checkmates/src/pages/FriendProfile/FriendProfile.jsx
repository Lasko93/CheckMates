import React, {useEffect, useState} from 'react';
import './FriendProfile.css';
import PlayIcon from "../../assets/images/Play_Icon.svg";
import FriendsIcon from "../../assets/images/friends_icon.svg";

import {useParams} from "react-router-dom";
import {useFetchProfilePicture, useFetchUser} from "../../services/UserService";
import FriendlistComponent from "../../components/FriendlistComponent/FriendlistComponent"

import TestProfilePicture from "../../assets/images/PlayerIcon.svg";
import ChessExpert from "../../assets/images/chess_expert.png";
import {isUserExpert} from "../../services/ExpertThropyService";
import PlayHistorieComponent from "../../components/PlayHistorieComponent/PlayHistorieComponent";


const FriendProfile = () => {
    const {userName}  = useParams();
    console.log(userName)
    const { data: userData, isLoading, isError } = useFetchUser(userName);
    const [isExpert, setIsExpert] = useState(false);


    const profilePictureUser = useFetchProfilePicture(userName);

    useEffect(() => {
        // Update des Expertenstatus beim Laden der Komponente
        const expertStatus = isUserExpert(userName);
        setIsExpert(expertStatus);

    }, [userName]); // Abhängigkeit: userName



    let profilePictureUserUrl = TestProfilePicture; // Standardbild
    if (profilePictureUser.isSuccess && profilePictureUser.data && profilePictureUser.data.images) {
        profilePictureUserUrl = `data:${profilePictureUser.data.type};base64,${profilePictureUser.data.images}`;
    }

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (isError || !userData) {
        console.log(isError);
        return <div>Error loading profile or no user data available.</div>;
    }

// Füge eine Abhängigkeit von userData.friendlistVisible hinzu, um zu entscheiden, ob die Liste gerendert werden soll.
    const friendListShouldBeVisible = userData.friendlistVisible;

    return (
        <div className='profile'>
            <h1>{userData.userName}'s Profile</h1>

            <div className='profile-data'>

               <p>
                   <img className="profile-Picture"
                        src={profilePictureUserUrl}
                        alt="Profile"/>
                </p>
                <p>Name: {userData.firstName} {userData.lastName}</p>

                <br/>
                <p><img className="playIcon" src= {PlayIcon} alt="PlayIcon"/><br/>
                    {userData.score} Points </p>
                <br/>
                {isExpert ? <div><img className="ChessExpertIcon" src= {ChessExpert} alt="ChessExpert"/></div> : <div>{userData.userName} is a Noob.</div>}
                <br/>
                {friendListShouldBeVisible ? (
                    <div>
                        <img className="FriendsIcon" src={FriendsIcon} alt="FriendsIcon"/>
                        <h2>{userName}'s friends</h2>
                        <FriendlistComponent userName={userName}/>
                    </div>
                    ):(
                    <div>
                        <p>This user's friend list is private.</p>
                    </div>
                )}

                <br/>
                <div className="historylist">
                    <h2>{userName}'s playhistorie</h2>
                    <h3>The last 3 results...</h3>
                    <PlayHistorieComponent userName={userName}/>
                </div>
            </div>
        </div>
    );
};

export default FriendProfile;