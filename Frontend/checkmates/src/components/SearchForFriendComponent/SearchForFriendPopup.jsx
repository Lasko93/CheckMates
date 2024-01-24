import React, {useEffect, useState} from "react";
import {useFetchUsers} from "../../services/UserService";
import "../SearchForPlayerComponent/SearchedPlayerComponent/SeachedPlayerInvite.css";
import DeclineIcon from "../../assets/images/decline_icon.svg";
import SearchBar from "../SearchForPlayerComponent/SearchBarComponent/SearchBar";
import SearchedFriendInvite from "./SearchedFriendComponent/SearchedFriendInviteComponent";
import {useSendFriendRequest} from "../../services/FriendService";

const SearchForFriendPopup = ({ isOpen, onClose, gameId }) => {

    const [searchInput, setSearchInput] = useState("");
    const [filteredUsers, setFilteredUsers] = useState([]);
    const { data: users, isLoading, isError } = useFetchUsers();
    const currentUser = sessionStorage.getItem('userName');


    // Update filtered users list, excluding the current user
    useEffect(() => {
        if (users) {
            const filtered = users.filter(user => user.userName !== currentUser);
            setFilteredUsers(filtered);
        }
    }, [users, currentUser]);

    // Update search input and filtered users list
    const handleSearchChange = (event) => {
        const value = event.target.value;
        setSearchInput(value);

        if (users) {
            // Filter users based on the search input
            const filtered = users.filter(user =>
                user.userName.toLowerCase().includes(value.toLowerCase()) &&
                user.userName !== currentUser // Exclude the current user
            );
            setFilteredUsers(filtered);
        }
    };

    const { mutate: sendFriendRequest, isLoading: isCreating, error: createError } = useSendFriendRequest();

    const handleInviteClick = (soonFriend) => {

        const sender = sessionStorage.getItem('userName');
        sendFriendRequest({ sender, soonFriend });
    };



    // Map through the userList to create SearchedPlayerInvite components
    const searchedPlayerInvites = filteredUsers.map((user, index) => (
        <SearchedFriendInvite
            key={user.userName}
            playerName={user.userName}
            chessClub={user.birthDay}
            elo={user.score}
            onInviteClick={handleInviteClick}
        />
    ));


    if (!isOpen) {
        return null;

    }

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (isError) {
        return <div>Error fetching users.</div>;
    }


    return (
        <div className="popup-container">
            <div className="SearchForPlayerPopup">
                <div className="ExitPopup" onClick={onClose}>
                    <img src={DeclineIcon} alt={DeclineIcon}/>
                </div>
                <h2 className="PopupHeadlines">Add someone as friend...</h2>
                <SearchBar value={searchInput} onChange={handleSearchChange}/>
                <div className="DisplayAllPlayers">
                    {searchedPlayerInvites}
                </div>
            </div>
        </div>);
};


export default SearchForFriendPopup;