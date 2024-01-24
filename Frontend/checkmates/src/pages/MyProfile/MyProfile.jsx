import React, {useEffect, useState} from 'react';
import './MyProfile.css';
import PlayIcon from "../../assets/images/Play_Icon.svg";
import FriendsIcon from "../../assets/images/friends_icon.svg";
import ChessExpert from "../../assets/images/chess_expert.png";
import "../../components/Buttons/ButtonComponent.css";
import {useFetchProfilePicture, useFetchUser} from "../../services/UserService";
import {useChangeFriendlistVisibility} from "../../services/FriendService";
import {useQueryClient} from "react-query";
import TestProfilePicture from "../../assets/images/PlayerIcon.svg";
import {useFetchClubsByMember} from "../../services/ChessClubService";
import {isUserExpert} from "../../services/ExpertThropyService";
import PlayHistorieComponent from "../../components/PlayHistorieComponent/PlayHistorieComponent";


const MyProfile = () => {
    // Zustand für die Sichtbarkeit der Freundesliste
    const userName = sessionStorage.getItem('userName');
    const queryClient = useQueryClient();
    const {data: myClubName} = useFetchClubsByMember(userName);
    const {data: userData,isLoading, isError} = useFetchUser(userName); // Hook, um Benutzerdaten zu laden
    const [isListPublic, setIsListPublic] = useState(true);
    const [isExpert, setIsExpert] = useState(false);




    useEffect(() => {
        if (userData && userData.friendlistVisible !== undefined) {
            setIsListPublic(userData.friendlistVisible);
        }
    }, [userData]); // Update state when userData is loaded

    useEffect(() => {
        // Update des Expertenstatus beim Laden der Komponente
        const expertStatus = isUserExpert(userName);
        setIsExpert(expertStatus);

    }, [userName]); // Abhängigkeit: userName

    const { mutate: changeVisibility } = useChangeFriendlistVisibility({
        onSuccess: () => {

        }
    });

    console.log(userName);
    console.log(isUserExpert(userName));
    const profilePictureUser = useFetchProfilePicture(userName);

    let profilePictureUserUrl = TestProfilePicture; // Standardbild
    if (profilePictureUser.isSuccess && profilePictureUser.data && profilePictureUser.data.images) {
        profilePictureUserUrl = `data:${profilePictureUser.data.type};base64,${profilePictureUser.data.images}`;
    }
    console.log(profilePictureUser);



    const toggleFriendListVisibility = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('appUser', userName);

        const response = await fetch("http://localhost:8080/friend/change-friendlist-visibility", {
            method: "PUT",
            body: formData,
        });
        const data = await response.json();
    };


    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (isError || !userData) {
        console.log(isError);
        return <div>Error loading profile or no user data available.</div>;
    }

    return(
        <div className='profile'>
            <h1>My Profile</h1>
            <div className='profile-data'>
                <h2>User: {userData.userName}</h2>
                <h3>My Club: {myClubName?.clubName}</h3>
                <p>
                    <img className="profile-Picture"
                         src={profilePictureUserUrl}
                         alt="Profile"/>

                </p>
                <p>Name: {userData.firstName} {userData.lastName}</p>
                <p>E-Mail: {userData.email}</p>
                <br/>
                <p><img className="playIcon" src= {PlayIcon} alt="PlayIcon"/><br/>
                    {userData.score} Points </p>
                <div>
                    {isExpert ? <div><img className="ChessExpertIcon" src= {ChessExpert} alt="ChessExpert"/></div> : <div>You are a Noob.</div>}
                </div>
                <br/>
                <p><img className="FriendsIcon" src= {FriendsIcon} alt="FriendsIcon"/>
                    <br/>
                    Your privacy settings

                </p>
                <button className="GeneralButton" onClick={toggleFriendListVisibility}
                        style={{marginLeft:'30%', marginTop:'2%', width: '40%'}}>
                    {isListPublic ? 'Public Friendlist' : 'Secret Friendlist'}
                </button>
                <br/>

                <div className="historylist">
                    <h2>Your playhistorie</h2>
                    <h3>The last 3 results...</h3>
                    <PlayHistorieComponent userName={userName}/>
                </div>

            </div>
        </div>


    )
}
export default MyProfile