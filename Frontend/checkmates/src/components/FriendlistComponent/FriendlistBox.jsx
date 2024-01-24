import React from "react";
import './FriendlistBox.css'
import {Link} from "react-router-dom";

const FriendlistBox = ({ friend, onStartPrivateChat, onAddToGroupChat }) => {
    return (
        <div className="friend-box">
            <div className="friendusername"> {friend.userName} </div>
            <div className="friendnames">{friend.firstName} {friend.lastName} </div>
            <Link className="profillink" to={`/profile/${friend.userName}`}>View Profile</Link>

            {/* Button to start a private chat */}
            <button onClick={() => onStartPrivateChat(friend)} className="chat-button">
                Start Private Chat
            </button>

            {/* Button to add to group chat */}
            <button onClick={() => onAddToGroupChat(friend)} className="group-chat-button">
                Add to Group Chat
            </button>
        </div>
    );
};

export default FriendlistBox;
