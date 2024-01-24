import React from 'react'; // Hinzufügen von useState zum Import
import './MyFriendsPage.css';
import FriendsIcon from "../../assets/images/friends_icon.svg";
import FriendlistComponent from "../../components/FriendlistComponent/FriendlistComponent"


function MyFriendsPage() {
    const userName = sessionStorage.getItem('userName');

    return (
        <div className="Friendlist">
                <h1><img className="FriendsIcon" src={FriendsIcon} alt="FriendsIcon"/> My friends</h1>

            <FriendlistComponent userName={userName}/>
        </div>
    );
}
export default MyFriendsPage;