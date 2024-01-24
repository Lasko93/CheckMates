// ChatMessage.jsx
import React from 'react';

const ChatMessage = ({ msg, isCurrentUser, onDelete, onEditRequest }) => {
    // Show options only if the message is sent by the current user and hasn't been read
    const showOptions = isCurrentUser && !msg.read;

    return (
        <div className={`message ${isCurrentUser ? 'sent' : 'received'}`}>
            <span className="sender-name">{msg.sender}</span>
            <div className="message-content">
                {msg.editing ? (
                    <input type="text" value={msg.content}  />
                ) : (
                    <p>{msg.content}</p>
                )}
                {showOptions && (
                    <div className="message-options">
                        <button onClick={() => onEditRequest(msg.id)}>Edit</button>
                        <button onClick={() => onDelete(msg.id)}>Remove</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ChatMessage;